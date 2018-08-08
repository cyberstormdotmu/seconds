package com.ishoal.ws.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.User;
import com.ishoal.core.user.UserService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.ishoal.ws.session.BuyerSession;;

@RestController
@RequestMapping("/ws/admin/buyerorder")
public class AdminPlaceBuyerOrderController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPlaceBuyerOrderController.class);
    
    private final UserService userService;
    private final ManageBuyerProfileService manageBuyerProfileService;
    
    public AdminPlaceBuyerOrderController(UserService userService, ManageBuyerProfileService manageBuyerProfileService){
        this.userService = userService;
        this.manageBuyerProfileService = manageBuyerProfileService;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/searchBuyer")
    public ResponseEntity<?> findUserByCriteria(@RequestParam(value = "firstName", required = false) String forename,
            @RequestParam(value = "lastName", required = false) String surname,
            @RequestParam(value = "emailAddress", required = false) String username){
        
        ResponseEntity<?> response;
        List<User> userList = new ArrayList<>();
        try{
            userList = userService.getUserListByCriteria(forename, surname, username);
            
            if(userList != null && userList.size() > 0){
                logger.info("Matched User(s) found.");
                response = ResponseEntity.ok(userList);
            }else{
                logger.info("No such User found.");
                response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("No such User found."));
            }
            
        }catch (Exception e) {
            logger.info("Error while fetching User list.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Error while fetching User list."));
        }
        return response;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/selectBuyer")
    public ResponseEntity<?> setBuyerToPlaceOrder(BuyerSession buyerSession,@RequestParam(value = "username") String username){
        
        ResponseEntity<?> response;
        try{
            
            if(buyerSession.removeUser()){
                logger.info("Removed user from session.");
            }else{
                logger.info("No user in session.");
            }
            
            User user = userService.findByUsernameIgnoreCase(username);
            buyerSession.addBuyerToSession(user);
            /*BuyerProfileController buyerProfileController = new BuyerProfileController(manageBuyerProfileService);
            buyerProfileController.updateBuyerInSession(buyerSession);*/
            response = ResponseEntity.ok(user);
        }catch (Exception e) {
            logger.info("Error while fetching User list.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Error while fetching User list."));
        }
        return response;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/clearBuyer")
    public ResponseEntity<?> clearSelectedBuyer(BuyerSession buyerSession){
        
        ResponseEntity<?> response;
        try{
            
            boolean result = buyerSession.removeUser(); 
            
            if(result){
                logger.info("Removed user from session.");
            }else{
                logger.info("No user in session.");
            }
            
            response = ResponseEntity.ok().build();
        }catch (Exception e) {
            logger.info("Error while fetching User list.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Error while fetching User list."));
        }
        return response;
    }
    
}
