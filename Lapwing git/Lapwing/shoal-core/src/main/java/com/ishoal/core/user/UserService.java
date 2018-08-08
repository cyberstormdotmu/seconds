package com.ishoal.core.user;

import static com.ishoal.email.EmailTemplates.anAccountActivationMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.Role;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.email.EmailService;
import com.ishoal.email.TemplateProperties;
import com.ishoal.email.TemplatePropertiesFactory;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class UserService {

	@Value("${shoal.webRootUrl}")
	private String webRootUrl;
	//@Value("${shoal.mail.API_KEY}")
	private String apiKey = "1234";
	private final EmailService emailService;
	private final UserRepository userRepository;
	private final TemplatePropertiesFactory templatePropertiesFactory;

	public UserService(UserRepository userRepository, EmailService emailService,
			TemplatePropertiesFactory templatePropertiesFactory) {

		this.userRepository = userRepository;
		this.emailService = emailService;
		this.templatePropertiesFactory = templatePropertiesFactory;
	}

	public PayloadResult<String> resendEmail(String username) {
		Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			client.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.251", 8080))); 
			User user = findByUsernameIgnoreCase(username);
			String confirmLink = webRootUrl + "/public/#/registration/confirm/" + user.getRegistrationToken();

			MediaType mediaType = MediaType.parse("application/json");
			com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType,
					"{\r\n" + "  \"personalizations\": [\r\n" + "    "
							+ "{\r\n" + "      "
									+ "\"to\": [\r\n" + "        "
												+ "{\r\n"
														+ "\"email\": \"" + user.getEmailAddress() + "\",\r\n" 
														+ "\"name\": \""+ user.getForename() + "\"\r\n" 
												+" }     "
											+ "],"+ "\r\n" 
									+ "\"substitutions\":{  \r\n" 
															+ " \"-confirmEmailLink-\":\"" + confirmLink
							+ "\"         \r\n" + "         },\r\n"
									+ "\"subject\": \"Confirm Email !\"\r\n"
							+ "    }\r\n" 
							+ "  ],\r\n" 
							+ "  \"from\": {\r\n"
							+ "    \"email\": \"help@silverwing.co\",\r\n" 
							+ "    \"name\": \"Ashton Squires\"\r\n"
							+ "  },\r\n" 
							+ "  \"subject\": \"Confirm Email !\",\r\n"
							+ "  \"template_id\":\"a3a80f36-d001-4694-b10a-1d8e3faceb04\"\r\n" 							
							+ "}");
			Request request = new Request.Builder().url("https://api.sendgrid.com/v3/mail/send").post(body)
					.addHeader("authorization", "Bearer " + apiKey).addHeader("content-type", "application/json")
					.build();

			response = client.newCall(request).execute();
			System.out.println(">>>>>>>>>>>>" + response.code());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(response.code() == 202)
		{
			return PayloadResult.success("Mail resent successfully.");
		}
		else
		{
			return PayloadResult.error((response.message()));
		}
	}

	@Transactional(readOnly = true)
	public User findByUsernameIgnoreCase(String username) {

		return userRepository.findByUsernameIgnoreCase(username);
	}

	@Transactional
	public Users confirm(String confirm) {

		if (confirm.compareToIgnoreCase("PENDING_AUTHENTICATION") == 0) {
			return userRepository.findUsersWithPendingAuthentication();
		}

		return userRepository.findUsersByConfirmationStatus(confirm);
	}

	public Users fetchAllUsersWithoutARole() {

		return userRepository.findAllUsersWithoutARole();
	}

	@Transactional
	public List<User> getUserListByCriteria(String forename, String surname, String username) {

		return userRepository.getUserListByCriteria(forename, surname, username);
	}

	@Transactional
	public PayloadResult<User> saveNewAdmin(User user) {
		PayloadResult<User> payload = null;
		User newAdminUser = null;
		newAdminUser = userRepository.saveNewAdmin(user);

		if (newAdminUser != null) {
			Date date = new Date();
			User newAdmin = User.aUser().username(newAdminUser.getUsername()).id(newAdminUser.getId())
					.hashedPassword(newAdminUser.getHashedPassword()).forename(newAdminUser.getForename())
					.surname(newAdminUser.getSurname()).mobileNumber(newAdminUser.getMobileNumber())
					.registrationToken("CONFIRM").authoriseDate(date).roles(newAdminUser.getRoles()).build();
			if (newAdmin == null) {
				throw new IllegalArgumentException("user does not exist");
			}
			makeBuyer(newAdmin);
			sendBuyerActivationEmail(newAdmin);
			payload = PayloadResult.success(newAdminUser);
		}

		return payload != null ? payload : PayloadResult.error("Unexpected error");
	}

	private void sendBuyerActivationEmail(User user) {

		TemplateProperties props = templatePropertiesFactory.createProperties();
		props.addProperty(TemplateProperties.USER_FIRST_NAME, user.getForename());
		props.addProperty(TemplateProperties.USER_SURNAME, user.getSurname());
		props.addProperty(TemplateProperties.EMAIL, user.getEmailAddress());
		props.addProperty(TemplateProperties.MOBILENUMBER, user.getMobileNumber());
		emailService.sendMessage(anAccountActivationMessage(props).build());
	}

	private void makeBuyer(User confirm) {
		confirm.addRole(Role.ADMIN);
		userRepository.saveAdminRole(confirm);
	}
}
