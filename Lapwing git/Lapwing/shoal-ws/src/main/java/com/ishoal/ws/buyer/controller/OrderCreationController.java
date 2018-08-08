package com.ishoal.ws.buyer.controller;

import static com.ishoal.ws.common.util.UriHelper.uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.NewOrderRequest;
import com.ishoal.core.orders.OrderConfirmationService;
import com.ishoal.core.orders.OrderCreationService;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.adapter.PlaceOrderRequestDtoAdapter;
import com.ishoal.ws.buyer.dto.validator.PlaceOrderRequestValidator;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.ishoal.ws.session.BuyerSession;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Configuration
@RestController
@RequestMapping("/ws/orders")
public class OrderCreationController {
	private static final Logger logger = LoggerFactory.getLogger(OrderCreationController.class);

	@Value("${shoal.westcoast.sftp.username}")
	private String username;
	
	@Value("${shoal.mail.API_KEY}")
	private String apiKey;
	
	@Value("${shoal.westcoast.sftp.url}")
	private String url;
	
	@Value("${shoal.westcoast.sftp.port}")
	private String port;
	
	@Value("${shoal.mail.username}")
	private String adminEmail;
	
	@Value("${shoal.mail.name}")
	private String adminName;
	
	@Value("${shoal.westcoast.sftp.password}")
	private String password;
	
	@Value("${shoal.westcoast.sftp.uploadpath}")
	private String uploadpath;
	
	private final OrderCreationService orderService;

	private final PlaceOrderRequestDtoAdapter placeOrderRequestDtoAdapter;
	private final PlaceOrderRequestValidator placeOrderRequestValidator;
	private final OrderSummaryDtoAdapter orderSummaryDtoAdapter;
	private final OrderConfirmationService orderConfirmationService;

	public OrderCreationController(OrderCreationService orderService,
			PlaceOrderRequestDtoAdapter placeOrderRequestDtoAdapter,
			PlaceOrderRequestValidator placeOrderRequestValidator, OrderSummaryDtoAdapter orderSummaryDtoAdapter,
			OrderConfirmationService orderConfirmationService) {
		this.orderService = orderService;
		this.placeOrderRequestDtoAdapter = placeOrderRequestDtoAdapter;
		this.placeOrderRequestValidator = placeOrderRequestValidator;
		this.orderSummaryDtoAdapter = orderSummaryDtoAdapter;
		this.orderConfirmationService = orderConfirmationService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequestDto placeOrderRequestDto, BuyerSession session,
			User user) {
		logger.info("Place Order for user [{}]", user);

		Result result = placeOrderRequestValidator.validate(placeOrderRequestDto, session.getBasket());
		if (result.isSuccess()) {
			NewOrderRequest newOrderRequest = this.placeOrderRequestDtoAdapter.adapt(placeOrderRequestDto, user);

			PayloadResult<Order> createResult = this.orderService.create(newOrderRequest, user);

			if (createResult.isSuccess()
					&& createResult.getPayload().getPaymentStatus().compareTo(PaymentStatus.PAID) == 0) {
				OrderReference reference = createResult.getPayload().getReference();
				orderConfirmationService.confirm(reference, orderConfirmationService.getVersion(reference));
			}

			if (createResult.isSuccess()) {
				Order order = createResult.getPayload();
				logger.info("Created order with reference [{}] for user [{}]", order.getReference(), user);
				
				logger.info("Creating XML for order.");
                boolean isXMLGenerated = generateOrderXML(placeOrderRequestDto,user, order.getReference().toString());
				
                if(isXMLGenerated){
                    logger.info("XML created successfully.");
                }else{
                    logger.info("Error while creating XML.");
                }
                
				return ResponseEntity.created(uri("/ws/orders/" + order.getReference().asString()))
						.body(orderSummaryDtoAdapter.adapt(order));
			}
			result = createResult;

			/*
			 * if (finalResult.isSuccess()) { return
			 * ResponseEntity.ok().build(); } else { return
			 * ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.
			 * getError())); }
			 */
		}

		logger.warn("Failed to create the order. Reason: " + result.getError());
		return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
	}
	
	@Bean
	private boolean generateOrderXML(PlaceOrderRequestDto placeOrderRequestDto, User user, String orderReference){
        
        boolean resultFlag = false;
        
        try {

            DocumentBuilderFactory dbFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = ((org.w3c.dom.Document) doc).createElement("OrderRequest");
            doc.appendChild(rootElement);

            // setting attribute to element
            Attr xmlnsAttr = doc.createAttribute("xmlns:xsi");
            xmlnsAttr.setValue("http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttributeNode(xmlnsAttr);

            Attr xsiAttr = doc.createAttribute("xsi:noNamespaceSchemaLocation");
            xsiAttr.setValue("ORDER.xsd");
            rootElement.setAttributeNode(xsiAttr);

            /* ------------------------ Version Tag -------------------------- */
            
            // Version Tag
            Element version = doc.createElement("Version");
            version.appendChild(doc.createTextNode(""));
            rootElement.appendChild(version);

            
            
            /* ------------------------ TransactionHeade Tag -------------------------- */
            
            // TransactionHeade Tag
            Element transactionHeader = doc.createElement("TransactionHeader");
            rootElement.appendChild(transactionHeader);
            
            // Inner tag of TransactionHeade Tag
            Element senderID = doc.createElement("SenderID");
            senderID.appendChild(doc.createTextNode(user.getWestcoastAccountNumber() + "SW" ));
            transactionHeader.appendChild(senderID);
            
            Element receiverID = doc.createElement("ReceiverID");
            receiverID.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(receiverID);
            
            Element loginID = doc.createElement("LoginID");
            loginID.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(loginID);
            
            Element passwordField = doc.createElement("Password");
            passwordField.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(passwordField);
            
            Element company = doc.createElement("Company");
            company.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(company);
            
            Element documentID = doc.createElement("DocumentID");
            documentID.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(documentID);
            
            Element transactionID = doc.createElement("TransactionID");
            transactionID.appendChild(doc.createTextNode(""));
            transactionHeader.appendChild(transactionID);

            
            
            /* ------------------------ Order Tag -------------------------- */
            
            // Order Tag
            Element order = doc.createElement("Order");
            rootElement.appendChild(order);
            
            // Inner tags of Order tag
            // OrderHeaderInformation tag
            Element orderHeaderInformation = doc.createElement("OrderHeaderInformation");
            order.appendChild(orderHeaderInformation);
            
            // Inner tags of OrderHeaderInfo tag
            Element shipToSuffix = doc.createElement("ShipToSuffix");
            shipToSuffix.appendChild(doc.createTextNode(""));
            orderHeaderInformation.appendChild(shipToSuffix);
            
            // Inner tags of OrderHeaderInfo tag
            Element addressingInformation = doc.createElement("AddressingInformation");
            orderHeaderInformation.appendChild(addressingInformation);
            
            Element shipTo = doc.createElement("ShipTo");
            addressingInformation.appendChild(shipTo);
            
            // Address Tag
            Element address = doc.createElement("Address");
            shipTo.appendChild(address);
            
            Element shipToAttention = doc.createElement("ShipToAttention");
            shipToAttention.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getOrganisationName()));
            address.appendChild(shipToAttention);
            
            Element shipToAddress1 = doc.createElement("ShipToAddress1");
            shipToAddress1.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getStreetAddress()));
            address.appendChild(shipToAddress1);
            
            Element shipToAddress2 = doc.createElement("ShipToAddress2");
            shipToAddress2.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getDepartmentName()));
            address.appendChild(shipToAddress2);
            
            Element shipToAddress3 = doc.createElement("ShipToAddress3");
            shipToAddress3.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getBuildingName()));
            address.appendChild(shipToAddress3);
            
            Element shipToCity = doc.createElement("ShipToCity");
            shipToCity.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getPostTown()));
            address.appendChild(shipToCity);
            
            Element shipToProvince = doc.createElement("ShipToProvince");
            shipToProvince.appendChild(doc.createTextNode(""));
            address.appendChild(shipToProvince);
            
            Element shipToPostalCode = doc.createElement("ShipToPostalCode");
            shipToPostalCode.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getPostcode()));
            address.appendChild(shipToPostalCode);
            
            Element shipToCountry = doc.createElement("ShipToCountry");
            shipToCountry.appendChild(doc.createTextNode(placeOrderRequestDto.getDeliveryAddress().getLocality()));
            address.appendChild(shipToCountry);
            
            
            // Contact Details Tag
            Element contactDetails = doc.createElement("ContactDetails");
            shipTo.appendChild(contactDetails);
            
            Element telephoneNumber = doc.createElement("TelephoneNumber");
            telephoneNumber.appendChild(doc.createTextNode(user.getMobileNumber()));
            contactDetails.appendChild(telephoneNumber);
            
            Element faxNumber = doc.createElement("FaxNumber");
            faxNumber.appendChild(doc.createTextNode(""));
            contactDetails.appendChild(faxNumber);
            
            Element eMail = doc.createElement("EMail");
            eMail.appendChild(doc.createTextNode(user.getUsername()));
            contactDetails.appendChild(eMail);
            
            Element notifyEMail = doc.createElement("NotifyEMail");
            notifyEMail.appendChild(doc.createTextNode(user.getUsername()));
            contactDetails.appendChild(notifyEMail);
            
            Element ContactName = doc.createElement("ContactName");
            ContactName.appendChild(doc.createTextNode(user.getForename() + " " + user.getSurname()));
            contactDetails.appendChild(ContactName);
            
                        
            Element customerPO = doc.createElement("CustomerPO");
            customerPO.appendChild(doc.createTextNode("12345"));
            addressingInformation.appendChild(customerPO);
            
            Element endUserPO = doc.createElement("EndUserPO");
            endUserPO.appendChild(doc.createTextNode(""));
            addressingInformation.appendChild(endUserPO);
            
            
            // Inner tags of OrderHeaderInfo tag
            Element processingOptions = doc.createElement("ProcessingOptions");
            orderHeaderInformation.appendChild(processingOptions);
            
            // Inner tags of processingOptions tag
            Element carrierCode = doc.createElement("CarrierCode");
            carrierCode.appendChild(doc.createTextNode(""));
            processingOptions.appendChild(carrierCode);
            
            Element carrierCodeValue = doc.createElement("CarrierCodeValue");
            carrierCodeValue.appendChild(doc.createTextNode(""));
            processingOptions.appendChild(carrierCodeValue);
            
            Element orderDueDate = doc.createElement("OrderDueDate");
            orderDueDate.appendChild(doc.createTextNode(""));
            processingOptions.appendChild(orderDueDate);
            
            // Inner Tags of Shipment Options
            Element shipmentOptions = doc.createElement("ShipmentOptions");
            processingOptions.appendChild(shipmentOptions);
            
            Element backOrderFlag = doc.createElement("BackOrderFlag");
            backOrderFlag.appendChild(doc.createTextNode(""));
            shipmentOptions.appendChild(backOrderFlag);
            
            Element splitShipmentFlag = doc.createElement("SplitShipmentFlag");
            splitShipmentFlag.appendChild(doc.createTextNode(""));
            shipmentOptions.appendChild(splitShipmentFlag);
            
            // OrderLineInformation tag
            Element orderLineInformation = doc.createElement("OrderLineInformation");
            order.appendChild(orderLineInformation);
            
            if(placeOrderRequestDto.getLines() == null && placeOrderRequestDto.getLines().size() == 0){
            
             // Inner tags of OrderLineInfo tag
                Element productLine = doc.createElement("ProductLine");
                orderLineInformation.appendChild(productLine);
                
                Element sku = doc.createElement("SKU");
                sku.appendChild(doc.createTextNode(""));
                productLine.appendChild(sku);
                
                Element alternateSKU = doc.createElement("AlternateSKU");
                alternateSKU.appendChild(doc.createTextNode(""));
                productLine.appendChild(alternateSKU);
                
                Element quantity = doc.createElement("Quantity");
                quantity.appendChild(doc.createTextNode(""));
                productLine.appendChild(quantity);

                Element fixedPrice = doc.createElement("FixedPrice");
                fixedPrice.appendChild(doc.createTextNode(""));
                productLine.appendChild(fixedPrice);
                
                Element customerLineNumber = doc.createElement("CustomerLineNumber");
                customerLineNumber.appendChild(doc.createTextNode(""));
                productLine.appendChild(customerLineNumber);
                
                Element lineComment = doc.createElement("LineComment");
                lineComment.appendChild(doc.createTextNode(""));
                productLine.appendChild(lineComment);
                
            
            }else{
                
                for(int i = 0; i < placeOrderRequestDto.getLines().size(); i++){
                 // Inner tags of OrderLineInfo tag
                    Element productLine = doc.createElement("ProductLine");
                    orderLineInformation.appendChild(productLine);
                    
                    Element sku = doc.createElement("SKU");
                    sku.appendChild(doc.createTextNode(placeOrderRequestDto.getLines().get(i).getProductCode() + "#ABU"));
                    productLine.appendChild(sku);
                    
                    Element alternateSKU = doc.createElement("AlternateSKU");
                    alternateSKU.appendChild(doc.createTextNode(""));
                    productLine.appendChild(alternateSKU);
                    
                    Element quantity = doc.createElement("Quantity");
                    quantity.appendChild(doc.createTextNode(String.valueOf(placeOrderRequestDto.getLines().get(i).getQuantity())));
                    productLine.appendChild(quantity);

                    Element fixedPrice = doc.createElement("FixedPrice");
                    fixedPrice.appendChild(doc.createTextNode(placeOrderRequestDto.getLines().get(i).getUnitPrice().toString()));
                    productLine.appendChild(fixedPrice);
                    
                    Element customerLineNumber = doc.createElement("CustomerLineNumber");
                    customerLineNumber.appendChild(doc.createTextNode(""));
                    productLine.appendChild(customerLineNumber);
                    
                    Element lineComment = doc.createElement("LineComment");
                    lineComment.appendChild(doc.createTextNode(""));
                    productLine.appendChild(lineComment);
                }
                
            }
            
            // CommentLine tag
            Element commentLine = doc.createElement("CommentLine");
            order.appendChild(commentLine);
            
            // Inner tags of CommentLine tag
            Element commentText1 = doc.createElement("CommentText");
            commentText1.appendChild(doc.createTextNode(""));
            commentLine.appendChild(commentText1);
            
            Element commentText2 = doc.createElement("CommentText");
            commentText2.appendChild(doc.createTextNode(""));
            commentLine.appendChild(commentText2);
            
            Element commentText3 = doc.createElement("CommentText");
            commentText3.appendChild(doc.createTextNode(""));
            commentLine.appendChild(commentText3);
            
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source source = new DOMSource(doc);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(source, (javax.xml.transform.Result) outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            
            JSch jsch = new JSch();
            Session session = null;

            session = jsch.getSession(username, url, Integer.parseInt(port));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            logger.info("Creating file name : ORDER-" + orderReference + ".xml");
            String fileName = "ORDER-" + orderReference + ".xml";
            sftpChannel.put(is, uploadpath + fileName);
            sftpChannel.exit();
            session.disconnect();
            resultFlag = true;
            
            // Generate XML
            /*StreamResult result = new StreamResult(new File("D:\\ORDER.XML"));
            transformer.transform(source, result);
            resultFlag = true;
            
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);*/

        } catch (JSchException e) {
            e.printStackTrace();
            logger.info("Error : " + e);
            resultFlag = false;
        } catch (SftpException e) {
            e.printStackTrace();
            logger.info("Error : " + e);
            resultFlag = false;
        }catch(Exception e){
            e.printStackTrace();
            logger.info("Error : " + e);
            resultFlag = false;
        }
        
        return resultFlag;
    }
	
}