package com.ishoal.ws.buyer.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerCreditInfoService;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.BuyerCreditInfo;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.adapter.BuyerCreditInfoRequestAdapter;
import com.ishoal.ws.common.dto.BuyerCreditInfoDto;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@RestController
@RequestMapping("/ws/creditapplicationform")
public class BuyerCreditInfoController {

    @Value("${shoal.mail.copyReceiverEmail1}")
    private String copyReceiverEmail1;
    
    @Value("${shoal.mail.copyReceiverEmail2}")
    private String copyReceiverEmail2;
    
    @Value("${shoal.mail.copyReceiverEmail3}")
    private String copyReceiverEmail3;
    
    @Value("${shoal.url.documentUrl}")
    private String documentUrl;
    
   // private static final String API_KEY = "86acd7d6d243b1d239f7a2a24501e06e"; //my api key
    private static final String API_KEY = "9af57c93d5d323795bfccdd5d6b4a570"; //silverwing api key
    private static final Logger logger = LoggerFactory.getLogger(BuyerCreditInfoController.class);
    private final BuyerCreditInfoRequestAdapter buyerCreditRequestAdapter = new BuyerCreditInfoRequestAdapter();
    private ManageBuyerCreditInfoService manageBuyerCreditInfoService;
    private final ManageBuyerProfileService manageBuyerProfileService;
    public BuyerCreditInfoController(ManageBuyerCreditInfoService manageBuyerCreditInfoService,
            ManageBuyerProfileService manageBuyerProfileService) {
        this.manageBuyerCreditInfoService = manageBuyerCreditInfoService;
        this.manageBuyerProfileService=manageBuyerProfileService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addCreditInfoDetails(@RequestBody BuyerCreditInfoDto creditDetails){
        logger.info("creating a new creditInfoDetails ",creditDetails);

        PayloadResult<BuyerCreditInfo> result = manageBuyerCreditInfoService
                .saveCreditInfoDetail(buyerCreditRequestAdapter.adapt(creditDetails));
     /*   PayloadResult<BuyerVendorCredit> result1 = buyerVendorCreditService
                .addVendorCreditDetails(result.getPayload());*/
        if (result.isSuccess()) {
            return ResponseEntity.ok(buyerCreditRequestAdapter.adapt(result.getPayload()));
        } else {
            logger.warn("Failed to update the buyer profile form. Reason: " + result.getError());
            return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> fetchProfile(User user) {
        logger.info("Fetch buyer profile form for user {}", user.getEmailAddress());
        ResponseEntity<?> val ;
        PayloadResult<BuyerProfile> result = manageBuyerProfileService
                .fetchProfile(FetchBuyerProfileRequest.aFetchBuyerProfileRequest().user(user).build());

        PayloadResult<BuyerCreditInfo> result1 = manageBuyerCreditInfoService
                .fetchBuyerCreditInfoDetails(result.getPayload().getId());
        if (result.isSuccess()) {
            val = ResponseEntity.ok(buyerCreditRequestAdapter.adapt(result1.getPayload()));
        } else {
            logger.warn("Failed to fetch the buyer profile form. Reason: " + result.getError());
            val = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
        }
        return val;
    }


    @RequestMapping(method = RequestMethod.GET, value="saveSupplierCreditFacility")
    public ResponseEntity<?> saveSupplierCreditFacility(User user) throws IOException {
        logger.info("save Supplier Credit Facility {}", user.getEmailAddress());
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(100, TimeUnit.SECONDS); 
        builder.readTimeout(100, TimeUnit.SECONDS); 
        builder.writeTimeout(100, TimeUnit.SECONDS);
        builder.proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress("192.168.0.251",8080)));
        client = builder.build();
        String signerEmail=user.getUsername();    //user.getEmailAddress();
        String partyName=user.getForename();
        String copyReceiverMail1=copyReceiverEmail1;
        String copyReceiverMail2=copyReceiverEmail2;        //ashton's email
        String copyReceiverMail3=copyReceiverEmail3; 
        String document_Url=documentUrl;
        /*String copyReceiverEmailID1="ashton@casol.com";
        String copyReceiverEmailID2="lisa-marie.edwards@westcoast.co.uk";        //ashton's email
        String copyReceiverEmailID3="risk@westcoast.co.uk";     */       //westcoast email
        MediaType mediaType = MediaType.parse("application/json;utf-8");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType,"{\r\n" + 
                " \"envelope_title\":\"Westcoast Supplier Credit Facility Application\",\r\n" + 
                " \"envelope_documents\":[{\r\n" + 
                " \"document_title\":\"Westcoast_Supplier_Credit_Facility_Application\",\r\n" + 
                " \"document_url\":\""+document_Url+"\"\r\n" + 
                " }],\r\n" + 
                " \"envelope_parties\": [\r\n" + 
                "   {\r\n" + 
                "    \"party_name\":\""+partyName+"\",\r\n" + 
                "    \"party_email\":\""+signerEmail+"\",\r\n" + 
                "    \"party_role\":\"signer1\",\r\n" + 
                "    \"party_message\":\"Please sign this!\"\r\n" + 
                "    },\r\n" + 
                "    {\r\n" + 
                "    \"party_name\":\"Ashton Squires\",\r\n" + 
                "    \"party_email\":\""+copyReceiverMail1+"\",\r\n" + 
                "    \"party_role\":\"copy\",\r\n" + 
                "    \"party_message\":\"Please sign this!\"\r\n" + 
                "    },\r\n" + 
                "    {\r\n" + 
                "    \"party_name\":\"Lisa Marie Edwards\",\r\n" + 
                "    \"party_email\":\""+copyReceiverMail2+"\",\r\n" + 
                "    \"party_role\":\"copy\",\r\n" + 
                "    \"party_message\":\"Please sign this!\"\r\n" + 
                "    },\r\n" + 
                "    {\r\n" + 
                "    \"party_name\":\"Risk\",\r\n" + 
                "    \"party_email\":\""+copyReceiverMail3+"\",\r\n" + 
                "    \"party_role\":\"copy\",\r\n" + 
                "    \"party_message\":\"Please sign this!\"\r\n" + 
                "    }\r\n" + 
                "     ]\r\n" + 
                      "}");
        Request request = new Request.Builder()
                .url("https://api.signable.co.uk:443/v1/envelopes")
                .post(body)
                .addHeader("Authorization",Credentials.basic(API_KEY, "*"))         
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response.code());
        okhttp3.ResponseBody responseBody=response.body();
        JSONObject jsObj=new JSONObject(responseBody.string());  
        System.out.println(jsObj);
        return null;
    }
}