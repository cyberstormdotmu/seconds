package com.ishoal.core.buyer;

import static com.ishoal.email.EmailTemplates.anAccountActivationMessage;
import static com.ishoal.email.EmailTemplates.anAccountDeactivationMessage;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import com.ishoal.core.domain.Role;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.email.EmailService;
import com.ishoal.email.TemplateProperties;
import com.ishoal.email.TemplatePropertiesFactory;

public class ActivateBuyerService {
    private final EmailService emailService;
    private final TemplatePropertiesFactory templatePropertiesFactory;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(ActivateBuyerService.class);
    public ActivateBuyerService(EmailService emailService, TemplatePropertiesFactory templatePropertiesFactory,
        UserRepository userRepository) {

        this.emailService = emailService;
        this.templatePropertiesFactory = templatePropertiesFactory;
        this.userRepository = userRepository;
    }
    
     private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
     public static String randomAlphaNumeric(int count) {
     StringBuilder builder = new StringBuilder();
     while (count-- != 0) {
     int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
     builder.append(ALPHA_NUMERIC_STRING.charAt(character));
     }
     return builder.toString();
     }

    
    @Transactional
    public void activateBuyer(User user) {
    	Date date = new Date();
        User currentUser = userRepository.findByUsernameIgnoreCase(user.getUsername());
        User confirm = User.aUser().registrationToken("CONTRACT_SIGNING_PENDING").forename(currentUser.getForename())
        		.mobileNumber(currentUser.getMobileNumber()).surname(currentUser.getSurname())
        		.authoriseDate(date).buyerReferralCode(randomAlphaNumeric(9))
        		.hashedPassword(currentUser.getHashedPassword())
        		.username(currentUser.getUsername())
        		.appliedFor(currentUser.getAppliedFor())
        		.westcoastAccountNumber(currentUser.getWestcoastAccountNumber())
        		.lapwingAccountNumber(currentUser.getLapwingAccountNumber())
        		.roles(currentUser.getRoles()).id(currentUser.getId()).build();
        logger.info("Current User Registration Token:",currentUser.getRegistrationToken());
        if (confirm == null) {
            throw new IllegalArgumentException("user does not exist");
        }
        if(confirm.getAppliedFor().equalsIgnoreCase("BUYER"))
        {
        	makeBuyer(confirm);
        }
        else {
        	makeSupplier(confirm);
		}
    }
    
    @Transactional
    public void confirmBuyer(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user does not exist");
        }
        userRepository.saveConfirmedUser(user);
    }
    
    @Transactional
    public void deactivateBuyer(User user) {
    	Date date = new Date();
        User currentUser = userRepository.findByUsernameIgnoreCase(user.getUsername());
        User reject = User.aUser().registrationToken("REJECT").forename(currentUser.getForename())
        		.mobileNumber(currentUser.getMobileNumber()).surname(currentUser.getSurname())
        		.authoriseDate(date)
        		.hashedPassword(currentUser.getHashedPassword())
        		.username(currentUser.getUsername())
        		.id(currentUser.getId()).build();
        if (reject == null) {
            throw new IllegalArgumentException("user does not exist");
        }
        deactiveBuyer(reject);
    }

    private void makeBuyer(User confirm) {

    	confirm.addRole(Role.BUYER);
        
        //Add User Table registration_token CONFIRM
        userRepository.save(confirm);
    }
    @Transactional
    private void makeSupplier(User confirm) {

    	confirm.addRole(Role.SUPPLIER);
        
        //Add User Table registration_token CONFIRM
        userRepository.save(confirm);
    }

    private void sendBuyerActivationEmail(User user) {

        TemplateProperties props = templatePropertiesFactory.createProperties();
        props.addProperty(TemplateProperties.USER_FIRST_NAME, user.getForename());
        props.addProperty(TemplateProperties.USER_SURNAME, user.getSurname());
        props.addProperty(TemplateProperties.EMAIL, user.getEmailAddress());
        props.addProperty(TemplateProperties.MOBILENUMBER, user.getMobileNumber());
        props.addProperty(TemplateProperties.USER_AS, user.getAppliedFor());
        emailService.sendMessage(anAccountActivationMessage(props).build());
    }
    
    private void deactiveBuyer(User reject) {
        userRepository.save(reject);
    }

    private void sendBuyerDeactivationEmail(User user) {

        TemplateProperties props = templatePropertiesFactory.createProperties();
        props.addProperty(TemplateProperties.USER_FIRST_NAME, user.getForename());
        props.addProperty(TemplateProperties.USER_SURNAME, user.getSurname());
        props.addProperty(TemplateProperties.EMAIL, user.getEmailAddress());
        props.addProperty(TemplateProperties.MOBILENUMBER, user.getMobileNumber());
        emailService.sendMessage(anAccountDeactivationMessage(props).build());
    }
}
