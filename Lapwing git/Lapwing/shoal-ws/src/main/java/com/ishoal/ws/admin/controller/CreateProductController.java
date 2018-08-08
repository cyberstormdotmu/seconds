package com.ishoal.ws.admin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.core.persistence.adapter.ProductEntityAdapter;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.products.CreateProductService;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.admin.dto.AdminPriceBandDto;
import com.ishoal.ws.admin.dto.AdminProductDto;
import com.ishoal.ws.admin.dto.adapter.AdminProductDtoAdapter;
import com.ishoal.ws.buyer.dto.MoneyDto;
import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSpecDto;
import com.ishoal.ws.buyer.dto.ProductVatRateDto;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin/manageProducts")
public class CreateProductController {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductController.class);

    private AdminProductDtoAdapter adminProductDtoAdapter = new AdminProductDtoAdapter();
    private ProductEntityAdapter productEntityAdapter = new ProductEntityAdapter();
    
    private CreateProductService createProductService;
    private ProductService productService;
    private OrderSeekService orderSeekService;
    public CreateProductController(CreateProductService createProductService, ProductService productService,OrderSeekService orderSeekService) {
        this.createProductService = createProductService;
        this.productService = productService;
        this.orderSeekService = orderSeekService;
        
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AdminProductDto> findProducts() {
        logger.info("Admin request to find all Product");
        return adminProductDtoAdapter.adapt(productService.findAll());
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestBody AdminProductDto product) throws URISyntaxException {

        logger.info("creating a new product ", product);
        boolean isProductExists = createProductService.getProduct(ProductCode.from(product.getCode()));
        if (!isProductExists) {
            Product savedProduct = createProductService.createProduct(adapt(product));
            return ResponseEntity.created(new URI("/ws/products/" + product.getCode())).body(adapt(savedProduct));
        } else {
            logger.info("Product with product code" +product.getCode()+ "is already exists. Please Enter Unique Product Code");
            return ResponseEntity.badRequest().body(ErrorInfo.badRequest("Product with product code " +product.getCode()+ " is already exists. Please enter unique product code."));
        }

    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@RequestBody AdminProductDto product) throws URISyntaxException {
        logger.info("updateing a  product ", product);
        boolean isProductExists = createProductService.getProductById(Long.parseLong(product.getId()));
        if (isProductExists) {
            PayloadResult<Product> result = createProductService.updateProduct(adapt(product));
            if (result.isSuccess()) {
                logger.info("Successfully Edited product");
                return ResponseEntity.created(new URI("/ws/products/" + product.getCode())).body(adapt(result.getPayload()));
            } else {
                logger.warn("Failed to update the product form Reason: " + result.getError());
                return generateErrorResponse(result);
            }
        } else {
            logger.info("Product with product code" +product.getCode()+ "is not Update. Please Try Again");
            return ResponseEntity.badRequest().body(ErrorInfo.badRequest("Product with product code " +product.getCode()+ " is not update. Please Try Again After Some Time."));
        }

    }
    
    private ResponseEntity<?> generateErrorResponse(PayloadResult<Product> result) {
        if (result.getErrorType().equals(ErrorType.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorInfo.badRequest(result.getError()));
        } else {
            return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAddress(@RequestParam(value = "code", required = false) String code) {
        logger.info("Product id for delete",code);
        ResponseEntity<?> response;
        ProductEntity product = productEntityAdapter.adapt(productService.getProduct(ProductCode.from(code)));
        List<OrderLineEntity> orderline = orderSeekService.findByProduct(product);
        if (product!=null && orderline==null) {      
            productService.deleteProduct(product.getId());  
            logger.info("Product with product code" +code+ "deleted");
               response = ResponseEntity.ok().build();
           } 
        else {
            logger.info("Product with product code" +code+ "Could not be deleted");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Can not be deleted.An Order has already been placed for this Product"));
           }   
           return response;                
    }

    @RequestMapping(method = RequestMethod.POST, value = "uploadFile")
    public ResponseEntity<?> uploadBulkProductFile(@RequestParam("file") MultipartFile multipart) throws IOException {

    ResponseEntity<?> resultEntity = null;

    File file = convert(multipart);
    String filename = file.getName();
    String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    String excelExtension1 = "xls";
    String excelExtension2 = "xlsx";

    if (extension.equalsIgnoreCase(excelExtension1) || extension.equalsIgnoreCase(excelExtension2)) {

        Vector productDataHolder = read(file, 0);
        Vector productSpecsDataHolder = read(file, 1);
        Vector productImagesDataHolder = read(file, 2);
        Vector priceBandsDataHolder = read(file, 3);

        List<String[]> productDetailsList = getAllFileDetails(productDataHolder);
        List<String[]> productSpecsList = getAllFileDetails(productSpecsDataHolder);
        List<String[]> productImagesList = getAllFileDetails(productImagesDataHolder);
        List<String[]> priceBandDataList = getAllFileDetails(priceBandsDataHolder);

        if (productDetailsList != null && productDetailsList.size() > 1) {
            String[] failedProductsArr = new String[productDetailsList.size()];

            List<AdminProductDto> productDtoList = new ArrayList<AdminProductDto>();
            for (int i = 1; i < productDetailsList.size(); i++) {

                try{
                    // Evaluate First sheet for product details
                    String[] tempProductDetails = productDetailsList.get(i);
                    List<String> categories = new ArrayList<String>();
                    categories.add(tempProductDetails[4]);

                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                    DateTime offerStartDate = formatter.parseDateTime(tempProductDetails[9]);
                    DateTime offerEndDate = formatter.parseDateTime(tempProductDetails[10]);

                    // Evaluate second sheet for product specifications
                    List<ProductSpecDto> specifications = new ArrayList<ProductSpecDto>();
                    for (int j = 1; j < productSpecsList.size(); j++) {
                        String[] tempProductSpecsDetails = productSpecsList.get(j);

                        if (tempProductDetails[3].equalsIgnoreCase(tempProductSpecsDetails[0])) {
                            ProductSpecDto productSpecDto = ProductSpecDto.aProductSpec()
                                    .name(tempProductSpecsDetails[1]).description(tempProductSpecsDetails[2]).build();
                            specifications.add(productSpecDto);
                        }
                    }

                    // Evaluate second sheet for product images
                    List<ProductImageDto> productImagesDtoList = new ArrayList<ProductImageDto>();
                    for (int j = 1; j < productImagesList.size(); j++) {
                        String[] tempProductImages = productImagesList.get(j);
                        int priorityOrder = 1;
                        if (tempProductDetails[3].equalsIgnoreCase(tempProductImages[0])) {
                            ProductImageDto productImageDto = ProductImageDto.aProductImageDto().order(priorityOrder)
                                    .url(tempProductImages[1]).description(tempProductImages[2]).build();
                            productImagesDtoList.add(productImageDto);
                            priorityOrder++;
                        }
                    }

                    // Evaluate second sheet for product price bands
                    List<AdminPriceBandDto> productPriceBandDtoList = new ArrayList<AdminPriceBandDto>();
                    for (int j = 1; j < priceBandDataList.size(); j++) {
                        String[] tempPriceBand = priceBandDataList.get(j);

                        if (tempProductDetails[3].equalsIgnoreCase(tempPriceBand[0])) {

                            if (tempPriceBand[1].equalsIgnoreCase("YES")) {
                                AdminPriceBandDto priceBandDto = AdminPriceBandDto.adminPriceBand()
                                        .minVolume(Double.valueOf(tempPriceBand[2]).longValue())
                                        .maxVolume(null)
                                        .buyerPrice(new MoneyDto(new BigDecimal(tempPriceBand[4])))
                                        .vendorPrice(new MoneyDto(new BigDecimal(tempPriceBand[5])))
                                        .shoalMargin(new MoneyDto(new BigDecimal(tempPriceBand[6])))
                                        .distributorMargin(new MoneyDto(new BigDecimal(tempPriceBand[7]))).build();
                                productPriceBandDtoList.add(priceBandDto);
                            } else {
                                AdminPriceBandDto priceBandDto = AdminPriceBandDto.adminPriceBand()
                                        .minVolume(Double.valueOf(tempPriceBand[2]).longValue())
                                        .maxVolume(Double.valueOf(tempPriceBand[3]).longValue())
                                        .buyerPrice(new MoneyDto(new BigDecimal(tempPriceBand[4])))
                                        .vendorPrice(new MoneyDto(new BigDecimal(tempPriceBand[5])))
                                        .shoalMargin(new MoneyDto(new BigDecimal(tempPriceBand[6])))
                                        .distributorMargin(new MoneyDto(new BigDecimal(tempPriceBand[7]))).build();
                                productPriceBandDtoList.add(priceBandDto);
                            }

                        }
                    }

                    boolean processNotif = false;
                    boolean submitNotif = false;

                    if (tempProductDetails[13] != null && tempProductDetails[13].trim() != ""
                            && tempProductDetails[13].trim().equalsIgnoreCase("TRUE")) {
                        processNotif = true;
                    }

                    if (tempProductDetails[14] != null && tempProductDetails[14].trim() != ""
                            && tempProductDetails[14].trim().equalsIgnoreCase("TRUE")) {
                        submitNotif = true;
                    }

                    AdminProductDto adminProductDto = AdminProductDto.anAdminProduct().vendorName(tempProductDetails[1])
                            .name(tempProductDetails[2]).code(tempProductDetails[3]).categories(categories)
                            .vatRate(ProductVatRateDto.aVatRateDto().code(tempProductDetails[5])
                                    .rate(new BigDecimal("0")).build())
                            .description(tempProductDetails[6]).stock(Long.parseLong(tempProductDetails[7]))
                            .maximumPurchaseLimit(Long.parseLong(tempProductDetails[15]))
                            .termsAndConditions(tempProductDetails[8]).review(tempProductDetails[11])
                            .suitability((tempProductDetails[12])).processNotification(processNotif)
                            .submitNotification(submitNotif).offerStartDate(offerStartDate).offerEndDate(offerEndDate)
                            .specifications(specifications).images(productImagesDtoList)
                            .priceBands(productPriceBandDtoList).build();

                    productDtoList.add(adminProductDto);

                }catch(Exception e){
                    e.printStackTrace();
                    logger.info("Error While saving product. ");
                }
            }

            List<Product> productList = new ArrayList<Product>();
            productList = adaptForMultipleProducts(productDtoList);
            PayloadResult<String[]> payload = null;

            if (productList != null && productList.size() > 0) {

                int resultCount = 0, failedFlagCount = 0, successFlagCount = 0, existedFlagCount = 0;
                String[] failedProducts = new String[productList.size()];
                String[] addedProducts = new String[productList.size()];
                String[] existedProducts = new String[productList.size()];

                for (int k = 0; k < productList.size(); k++) {

                    try {

                        boolean isProductExists = createProductService.getProduct(productList.get(k).getCode());
                        if (!isProductExists) {
                            Product savedProduct = createProductService.createProduct(productList.get(k));
                            if (savedProduct != null) {
                                logger.info("Product with code saved successfully : "
                                        + productList.get(k).getCode().toString());
                                resultCount++;
                                addedProducts[successFlagCount] = productList.get(k).getCode().toString();
                                successFlagCount++;
                            }
                        } else {
                            logger.info("Product with code is already exists : "
                                    + productList.get(k).getCode().toString());
                            existedProducts[existedFlagCount] = productList.get(k).getCode().toString();
                            existedFlagCount++;
                        }

                    } catch (Exception e2) {
                        logger.info("Error while saving product with code : "
                                + productList.get(k).getCode().toString());
                        failedProducts[failedFlagCount] = productList.get(k).getCode().toString();
                        failedFlagCount++;
                    }

                }

                if (resultCount == productList.size()) {
                    logger.info("All products saved successfully.");
                    payload = PayloadResult.success(addedProducts);
                    resultEntity = ResponseEntity.ok(payload.getPayload());
                } else if (existedFlagCount > 0) {

                    String errorMsg = "";
                    for (int flag = 0; flag < existedProducts.length; flag++) {
                        errorMsg = existedProducts[flag] + " ";
                    }

                    logger.info("Some products with code(s) ( " + errorMsg
                            + " ) are already exists. Please change product code(s) to save.");
                    payload = PayloadResult.error("Some products with code(s) ( " + errorMsg
                            + " ) are already exists. Please change product code(s) to save.");
                    resultEntity = ResponseEntity.badRequest()
                            .body(ErrorInfo.badRequest("Some products with code(s) ( " + errorMsg
                                    + " ) are already exists. Please change product code(s) to save."));
                } else {

                    String errorMsg = "";
                    for (int flag = 0; flag < failedProducts.length; flag++) {
                        errorMsg = failedProducts[flag] + " ";
                    }

                    logger.info("Error while saving some products with code(s) ( " + errorMsg
                            + " ). Please check log file for more information.");
                    payload = PayloadResult.error("Error while saving some products with code(s) ( " + errorMsg
                            + " ). Please check log file for more information.");
                    resultEntity = ResponseEntity.badRequest()
                            .body(ErrorInfo.badRequest("Error while saving some products with code(s) ( " + errorMsg
                                    + " ). Please check log file for more information."));

                }

            } else {
                resultEntity = ResponseEntity.badRequest()
                        .body(ErrorInfo.badRequest("Empty File or Invalid Data."));
            }

        }else{
            resultEntity = ResponseEntity.badRequest()
                    .body(ErrorInfo.badRequest("Empty File or Invalid Data."));
        }
    }



    return resultEntity;
}

private File convert(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.flush();
    fos.close();
    return convFile;
}

@SuppressWarnings("unchecked")
public static Vector read(File file, int indexNumber) {
    Vector cellVectorHolder = new Vector();
    try {
        Workbook myWorkBook = WorkbookFactory.create(file);
        Sheet mySheet = myWorkBook.getSheetAt(indexNumber);

        Iterator rowIter = mySheet.rowIterator();
        while (rowIter.hasNext()) {
            XSSFRow myRow = (XSSFRow) rowIter.next();
            Iterator cellIter = myRow.cellIterator();
            Vector cellStoreVector = new Vector();
            while (cellIter.hasNext()) {
                XSSFCell myCell = (XSSFCell) cellIter.next();
                cellStoreVector.addElement(myCell);
            }

            if (cellStoreVector.size() > 0) {
                cellVectorHolder.addElement(cellStoreVector);
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cellVectorHolder;
}

public static List<String[]> getAllFileDetails(Vector dataHolder) {

    List<String[]> dataArrList = new ArrayList<String[]>();

    for (int i = 0; i < dataHolder.size(); i++) {
        Vector cellStoreVector = (Vector) dataHolder.elementAt(i);
        String[] rowDataArr = new String[cellStoreVector.size()];
        for (int j = 0; j < cellStoreVector.size(); j++) {
            XSSFCell myCell = (XSSFCell) cellStoreVector.elementAt(j);
            String st = myCell.toString();
            String tempStr = st.substring(0);
            rowDataArr[j] = tempStr;

        }
        dataArrList.add(rowDataArr);
    }
    return dataArrList;
}

    private Product adapt(AdminProductDto product) {
    
        return adminProductDtoAdapter.adapt(product);
    }
    
    private List<Product> adaptForMultipleProducts(List<AdminProductDto> productDtoList) {
    
        return adminProductDtoAdapter.adaptForMultipleProducts(productDtoList);
    }
    
    private AdminProductDto adapt(Product product) {
    
        return adminProductDtoAdapter.adapt(product);
    }
}
