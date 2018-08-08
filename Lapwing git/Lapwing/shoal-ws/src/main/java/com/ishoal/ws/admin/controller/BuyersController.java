package com.ishoal.ws.admin.controller;

import static com.ishoal.core.buyer.FetchBuyerProfileRequest.aFetchBuyerProfileRequest;
import static com.ishoal.ws.buyer.dto.BuyerSummaryDto.aUserSummary;
import static com.ishoal.ws.buyer.dto.OrganisationDto.anOrganisationDto;
import static com.ishoal.ws.buyer.dto.RegistrationSummaryDto.aRegistration;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.buyer.ActivateBuyerService;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.credit.BuyerCreditService;
import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.BuyerProfiles;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.core.user.UserService;
import com.ishoal.core.vendor.BuyerVendorCreditService;
import com.ishoal.core.vendor.ManageVendorService;
import com.ishoal.ws.admin.dto.AddNewAdminDto;
import com.ishoal.ws.admin.dto.AdminUpdateVendorCreditDto;
import com.ishoal.ws.admin.dto.BuyerDetailsDto;
import com.ishoal.ws.admin.dto.BuyerListingDto;
import com.ishoal.ws.admin.dto.adapter.BuyerDetailsDtoAdapter;
import com.ishoal.ws.admin.dto.adapter.BuyerListingDtoAdapter;
import com.ishoal.ws.buyer.controller.ShoppingBasketController;
import com.ishoal.ws.buyer.dto.BuyerAllCreditsDto;
import com.ishoal.ws.buyer.dto.BuyerLapwingCreditsDto;
import com.ishoal.ws.buyer.dto.BuyerSummaryDto;
import com.ishoal.ws.buyer.dto.BuyerVendorCreditsDto;
import com.ishoal.ws.buyer.dto.OrderBalancesDto;
import com.ishoal.ws.buyer.dto.OrganisationDto;
import com.ishoal.ws.buyer.dto.RegistrationSummariesDto;
import com.ishoal.ws.buyer.dto.RegistrationSummaryDto;
import com.ishoal.ws.buyer.dto.adapter.BuyerVendorCreditsDtoAdapter;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@RestController
@RequestMapping("/ws/admin/buyers")
public class BuyersController {
	@Value("${shoal.webRootUrl}")
	private String webRootUrl;
	@Value("${shoal.mail.username}")
	private String adminEmail;
	@Value("${shoal.mail.name}")
	private String adminName;
	@Value("${shoal.mail.API_KEY}")
	private String apiKey;
	private static final Logger logger = LoggerFactory.getLogger(ShoppingBasketController.class);
	private final UserService userService;
	private final OrderSeekService orderService;
	private static final String API_KEY = "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";
	private final ManageBuyerProfileService manageBuyerProfileService;
	private final ActivateBuyerService activateBuyerService;
	private final BuyerCreditService buyerCreditService;
	private final BuyerVendorCreditService buyerVendorCreditService;
	private final BuyerWithdrawService buyerWithdrawService;
	private final ManageVendorService manageVendorService;
	private final BuyerListingDtoAdapter adapter = new BuyerListingDtoAdapter();
	private final BuyerVendorCreditsDtoAdapter buyerVendorCreditsDtoAdapter = new BuyerVendorCreditsDtoAdapter();
	private final BuyerDetailsDtoAdapter buyerDetailsAdapter = new BuyerDetailsDtoAdapter();

	public BuyersController(UserService userService, ManageBuyerProfileService manageBuyerProfileService,
			ActivateBuyerService activateBuyerService, OrderSeekService orderService,
			ManageVendorService manageVendorService, BuyerCreditService buyerCreditService,
			BuyerWithdrawService buyerWithdrawService, BuyerVendorCreditService buyerVendorCreditService) {

		this.userService = userService;
		this.manageBuyerProfileService = manageBuyerProfileService;
		this.activateBuyerService = activateBuyerService;
		this.orderService = orderService;
		this.buyerCreditService = buyerCreditService;
		this.buyerWithdrawService = buyerWithdrawService;
		this.buyerVendorCreditService = buyerVendorCreditService;
		this.manageVendorService = manageVendorService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "ALL")
	public List<BuyerListingDto> fetchBuyers() {
		logger.info("Admin request to find all Buyer");
		BuyerProfiles buyers = manageBuyerProfileService.findAllBuyers();
		return adapter.adapt(buyers);
	}

	@RequestMapping(method = RequestMethod.GET, value = "moneyOwedDetails")
	public List<BuyerVendorCreditsDto> fetchMoneyOwedBuyersDetails() {
		logger.info("Admin request to find all Buyer with Money Owed");

		BuyerVendorCredits buyerVendorCreditsDto = buyerVendorCreditService.findAll();

		return buyerVendorCreditsDtoAdapter.adapt(buyerVendorCreditsDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "moneyOwedDetail")
	public List<BuyerVendorCreditsDto> fetchMoneyOwedBuyersDetail(User user) {
		logger.info("Suppiler request to find all Buyer with Money Owed");

		BuyerVendorCredits buyerVendorCreditsDto = buyerVendorCreditService.findUserMoneyOwedDetail(user);

		return buyerVendorCreditsDtoAdapter.adapt(buyerVendorCreditsDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{id}/details")
	public ResponseEntity<BuyerDetailsDto> buyerDetails(@PathVariable("id") String id) {
		logger.info("createOfferReport for offer with reference [{}]", id);
		BuyerProfile buyerProfile = manageBuyerProfileService.fetchProfile(id);

		BuyerListingDto buyerListingDto = adapter.adapt(buyerProfile);
		OrderBalancesDto orderBalancesDto = getCreditBalances(buyerProfile.getUser());
		BuyerAllCreditsDto buyerAllCreditsDto = getAllCreditBalances(buyerProfile.getUser());

		return ResponseEntity.ok(buyerDetailsAdapter.adapt(buyerListingDto, orderBalancesDto, buyerAllCreditsDto));
	}

	@RequestMapping(method = RequestMethod.GET, value = "{id}/buyerDetails")
	public ResponseEntity<BuyerDetailsDto> buyerDetailsOfSuppiler(@PathVariable("id") String id, User user) {
		logger.info("createOfferReport for offer with reference [{}]", id);
		BuyerProfile buyerProfile = manageBuyerProfileService.fetchProfile(id);

		BuyerListingDto buyerListingDto = adapter.adapt(buyerProfile);
		OrderBalancesDto orderBalancesDto = getCreditBalances(buyerProfile.getUser());
		BuyerAllCreditsDto buyerAllCreditsDto = getAllCreditBalancesOfSuppiler(buyerProfile.getUser(), user);

		return ResponseEntity.ok(buyerDetailsAdapter.adapt(buyerListingDto, orderBalancesDto, buyerAllCreditsDto));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> fetchInactiveBuyers(@RequestParam(value = "role") String roleName) {

		logger.info("fetching all users with role = ", roleName);
		ResponseEntity<?> response;

		if ("INACTIVE".equals(roleName)) {
			Users users = userService.fetchAllUsersWithoutARole();
			response = ResponseEntity.ok().body(fetchBuyerProfileInfo(users));

		} else if ("CONFIRM".equals(roleName)) {
			Users users = userService.confirm("CONFIRM");
			response = ResponseEntity.ok().body(fetchBuyerProfileInfo(users));

		} else if ("REJECT".equals(roleName)) {
			logger.info("reject register user");
			Users users = userService.confirm("REJECT");
			response = ResponseEntity.ok().body(fetchBuyerProfileInfo(users));

		} else if ("CONTRACT_SIGNING_PENDING".equals(roleName)) {
			logger.info("fetching CONTRACT_SIGNING_PENDING register user");
			Users users = userService.confirm("CONTRACT_SIGNING_PENDING");
			response = ResponseEntity.ok().body(fetchBuyerProfileInfo(users));

		} else if ("PENDING_AUTHENTICATION".equals(roleName)) {
			logger.info("fetching user with PENDING_AUTHENTICATION");
			Users users = userService.confirm("PENDING_AUTHENTICATION");
			response = ResponseEntity.ok().body(fetchBuyerProfileInfo(users));

		} else {
			response = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateVendorCredit(@RequestBody AdminUpdateVendorCreditDto vendorCreditDto) {

		logger.info("admin {} is being upgraded to a vendor Credit", vendorCreditDto.getVendorName());
		ResponseEntity<?> response;

		BuyerProfile buyerProfile = manageBuyerProfileService.fetchProfile(vendorCreditDto.getId());
		Vendor vendor = manageVendorService.fetchVendor(vendorCreditDto.getVendorName());
		BuyerVendorCredit buyerVendorCredit = adapt(vendorCreditDto, buyerProfile, vendor);
		BuyerVendorCredit updateBuyerVendorCredit = buyerVendorCreditService.updateBuyerVendorCredit(buyerVendorCredit);
		if (updateBuyerVendorCredit == null) {
			response = ResponseEntity.badRequest()
					.body(ErrorInfo.badRequest("buyer " + buyerVendorCredit.getBuyer().getUser().getForename()
							+ " and vendor " + buyerVendorCredit.getVendor().getName() + "pair not found "));
		} else {
			response = ResponseEntity.ok().build();
		}
		return response;
	}

	@SuppressWarnings("null")
	@RequestMapping(method = RequestMethod.POST, value = "/addAdmin")
	public ResponseEntity<?> addNewAdmin(@RequestBody AddNewAdminDto addNewAdminDto) {

		logger.info("admin {} is being Add new Admin : ", addNewAdminDto.getFirstname());
		ResponseEntity<?> response;

		PayloadResult<User> result = userService.saveNewAdmin(adapt(addNewAdminDto));
		if (result == null) {
			response = ResponseEntity.badRequest()
					.body(ErrorInfo.badRequest("New Admin " + result.getPayload().getForename() + " is not Added"));
		} else {
			response = ResponseEntity.ok().body(result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{username}/activateBuyer")
	public ResponseEntity<?> activateBuyer(@PathVariable("username") String username) {

		logger.info("user {} is being upgraded to a BUYER", username);
		ResponseEntity<?> response;

		User user = userService.findByUsernameIgnoreCase(username);
		if (user == null) {
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("user " + username + " not found"));
		} else {
			activateBuyerService.activateBuyer(user);
			try {
				OkHttpClient client = new OkHttpClient();
				String loginLink = webRootUrl + "/public/#";
				String userEmail = user.getUsername();
				String userName = user.getForename() + " " + user.getSurname();
				client.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.251", 8080)));
				MediaType mediaType = MediaType.parse("application/json");
				com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType,
						"{\r\n" + "  \"personalizations\": [\r\n" + "    {\r\n" + "      \"to\": [\r\n"
								+ "        {\r\n" + "          \"email\": \"" + userEmail + "\",\r\n"
								+ "          \"name\": \"" + userName + "\"\r\n" + "        }\r\n" + "      ],\r\n"
								+ "      \"substitutions\":{  \r\n" + "          \"-loginLink-\":\"" + loginLink
								+ "\"         \r\n" + "          \"-userName-\":\"" + user.getForename()
								+ "\"         \r\n" + "         },\r\n" + "      \"subject\": \"Confirm Emai !\"\r\n"
								+ "    }\r\n" + "  ],\r\n" + "  \"from\": {\r\n" + "    \"email\": \"" + adminEmail
								+ "\",\r\n" + "    \"name\": \"" + adminName + "\"\r\n" + "  },\r\n"
								+ "  \"subject\": \"Your Silver Wing Account Was Approved !\",\r\n"
								+ "  \"template_id\":\"23884a8d-e644-41d2-b956-04feaf8400e8\"\r\n" + "\r\n" + "}");
				Request request = new Request.Builder().url("https://api.sendgrid.com/v3/mail/send").post(body)
						.addHeader("authorization", "Bearer " + apiKey).addHeader("content-type", "application/json")
						.build();

				Response emailResponse = client.newCall(request).execute();

				/*
				 * response.code() method gives response code : Ex. 202 :-
				 * Accepted / Success 401 :- Unauthorized 404 :- Not Found etc.
				 * 
				 * response.message() method gives message : Ex. "Accepted" if
				 * mail sent
				 * 
				 * response.isSuccessful() method gives boolean response : Ex.
				 * true if successful
				 */

				System.out.println(">>>>>>>>>>>>" + emailResponse.code());

			} catch (IOException e) {
				e.printStackTrace();
			}
			response = ResponseEntity.ok().build();
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{username}/confirmBuyer")
	public ResponseEntity<?> confirmBuyer(@PathVariable("username") String username) {

		logger.info("user {} is being upgraded to a BUYER", username);
		ResponseEntity<?> response;

		User user = userService.findByUsernameIgnoreCase(username);
		if (user == null) {
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("user " + username + " not found"));
		} else {
			activateBuyerService.confirmBuyer(user);
			PayloadResult<BuyerProfile> buyerprofile = manageBuyerProfileService.fetchProfile(buildFetchRequest(user));
			buyerVendorCreditService.addVendorCreditDetails(buyerprofile);
			response = ResponseEntity.ok().build();
		}
		return response;
	}

	private FetchBuyerProfileRequest buildFetchRequest(User user) {
		return FetchBuyerProfileRequest.aFetchBuyerProfileRequest().user(user).build();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{username}/resendEmail")
	public ResponseEntity<?> resendEmail(@PathVariable("username") String username) {
		logger.info("user {} is being upgraded to a BUYER", username);
		ResponseEntity<?> response;
		
		PayloadResult<String> result = userService.resendEmail(username);
		if(result.isSuccess())
		{
			response  = ResponseEntity.ok(result.getPayload());
		}
		else
		{
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{username}/deactivateBuyer")
	public ResponseEntity<?> deactivateBuyer(@PathVariable("username") String username) {

		logger.info("user {} is being upgraded to a BUYER", username);
		ResponseEntity<?> response;

		User user = userService.findByUsernameIgnoreCase(username);
		if (user == null) {
			response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("user " + username + " not found"));
		} else {
			activateBuyerService.deactivateBuyer(user);
			try {
				OkHttpClient client = new OkHttpClient();
				String userEmail = user.getUsername();
				String userName = user.getForename() + " " + user.getSurname();
				client.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.251", 8080)));
				MediaType mediaType = MediaType.parse("application/json");
				com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType,
						"{\r\n" + "  \"personalizations\": [\r\n" + "    {\r\n" + "      \"to\": [\r\n"
								+ "        {\r\n" + "          \"email\": \"" + userEmail + "\",\r\n"
								+ "          \"name\": \"" + userName + "\"\r\n" + "        }\r\n" + "      ],\r\n"
								+ "      \"substitutions\":{  \r\n" + "          \"-userName-\":\"" + user.getForename()
								+ "\"         \r\n" + "         },\r\n" + "      \"subject\": \"Reject Email !\"\r\n"
								+ "    }\r\n" + "  ],\r\n" + "  \"from\": {\r\n" + "    \"email\": \"" + adminEmail
								+ "\",\r\n" + "    \"name\": \"" + adminName + "\"\r\n" + "  },\r\n"
								+ "  \"subject\": \"Your Silver Wing Account Was Rejected !\",\r\n"
								+ "  \"template_id\":\"80628def-7b8f-4b9d-b4e4-6b73821ab910\"\r\n" + "\r\n" + "}");
				Request request = new Request.Builder().url("https://api.sendgrid.com/v3/mail/send").post(body)
						.addHeader("authorization", "Bearer " + apiKey).addHeader("content-type", "application/json")
						.build();

				Response emailResponse = client.newCall(request).execute();

				/*
				 * response.code() method gives response code : Ex. 202 :-
				 * Accepted / Success 401 :- Unauthorized 404 :- Not Found etc.
				 * 
				 * response.message() method gives message : Ex. "Accepted" if
				 * mail sent
				 * 
				 * response.isSuccessful() method gives boolean response : Ex.
				 * true if successful
				 */

				System.out.println(">>>>>>>>>>>>" + emailResponse.code());

			} catch (IOException e) {
				e.printStackTrace();
			}
			response = ResponseEntity.ok().build();
		}
		return response;
	}

	private User adapt(AddNewAdminDto addNewAdminDto) {
		Date date = new Date();
		return User.aUser().username(addNewAdminDto.getEmailAddress())
				.hashedPassword(SecurePassword.fromClearText(addNewAdminDto.getPassword()))
				.forename(addNewAdminDto.getFirstname()).surname(addNewAdminDto.getSurname())
				.mobileNumber(addNewAdminDto.getMobileNumber()).authoriseDate(date).registrationToken("CONFIRM")
				.build();
	}

	private BuyerVendorCredit adapt(AdminUpdateVendorCreditDto vendorCreditDto, BuyerProfile buyerProfile,
			Vendor vendor) {

		return BuyerVendorCredit.aBuyerVendorCredit().vendor(vendor).buyer(buyerProfile)
				.availableCredit(new BigDecimal(vendorCreditDto.getAvailableCredits()))
				.totalCredit(new BigDecimal(vendorCreditDto.getTotalCredits())).build();
	}

	private RegistrationSummariesDto fetchBuyerProfileInfo(Users users) {

		return IterableUtils.mapToCollection(users, user -> enrichWithProfileInfo(user),
				userList -> RegistrationSummariesDto.over(userList));

	}

	private RegistrationSummaryDto enrichWithProfileInfo(User user) {
		PayloadResult<BuyerProfile> payloadResult = manageBuyerProfileService
				.fetchProfile(aFetchBuyerProfileRequest().user(user).build());

		RegistrationSummaryDto.Builder registrationBuilder = aRegistration().buyer(adapt(user));

		if (payloadResult.isSuccess()) {
			BuyerProfile buyerProfile = payloadResult.getPayload();
			registrationBuilder.organisation(adapt(buyerProfile.getOrganisation()));
			registrationBuilder.registrationDate(buyerProfile.getCreatedDate());
		}
		return registrationBuilder.build();
	}

	private BuyerSummaryDto adapt(User user) {

		return aUserSummary().emailAddress(user.getEmailAddress()).firstName(user.getForename())
				.surname(user.getSurname()).mobileNumber(user.getMobileNumber()).appliedFor(user.getAppliedFor())
				.westcoastNumber(user.getWestcoastAccountNumber()).registrationToken(user.getRegistrationToken())
				.build();
	}

	private OrganisationDto adapt(Organisation organisation) {

		if (organisation == null) {
			return null;
		}

		return anOrganisationDto().name(organisation.getName()).registrationNumber(organisation.getRegistrationNumber())
				.build();
	}

	private OrderBalancesDto getCreditBalances(User user) {
		return OrderBalancesDto.someOrderBalances().moneyOwnedBalance(orderService.findCreditMoneyOwnedBalance(user))
				.latePaymentBalance(orderService.findCreditLatePaymentBalance(user))
				.accountPaybleBalance(orderService.findCreditAccountsPayableBalance(user)).build();
	}

	private BuyerAllCreditsDto getAllCreditBalances(User user) {

		BuyerCredits buyerCredits = buyerCreditService.fetchBuyerCredits(user);
		BigDecimal withdrawCredits = buyerWithdrawService.calculateTotalWithdrawCredits(user);

		BuyerLapwingCreditsDto buyerLapwingCreditsDto = BuyerLapwingCreditsDto.someCreditBalances()
				.pendingCreditBalance(buyerCredits.getPendingCreditBalance().gross())
				.availableCreditBalance(buyerCredits.getAvailableCreditBalance().gross())
				.redeemableCreditBalance(buyerCredits.getRedeemableCreditBalance().gross().subtract(withdrawCredits))
				.build();
		List<BuyerVendorCreditsDto> buyerVendorCreditsDto = buyerVendorCreditsDtoAdapter
				.adapt(buyerVendorCreditService.fetchBuyerVendorCredits(user));

		return BuyerAllCreditsDto.aBuyerAllCreditsDto().lapwingCredits(buyerLapwingCreditsDto)
				.vendorCredits(buyerVendorCreditsDto).build();
	}

	private BuyerAllCreditsDto getAllCreditBalancesOfSuppiler(User user, User userSuppiler) {

		BuyerCredits buyerCredits = buyerCreditService.fetchBuyerCredits(user);
		BigDecimal withdrawCredits = buyerWithdrawService.calculateTotalWithdrawCredits(user);

		BuyerLapwingCreditsDto buyerLapwingCreditsDto = BuyerLapwingCreditsDto.someCreditBalances()
				.pendingCreditBalance(buyerCredits.getPendingCreditBalance().gross())
				.availableCreditBalance(buyerCredits.getAvailableCreditBalance().gross())
				.redeemableCreditBalance(buyerCredits.getRedeemableCreditBalance().gross().subtract(withdrawCredits))
				.build();
		List<BuyerVendorCreditsDto> buyerVendorCreditsDto = buyerVendorCreditsDtoAdapter
				.adapt(buyerVendorCreditService.fetchBuyerVendorCreditsOfSuppiler(user, userSuppiler));

		return BuyerAllCreditsDto.aBuyerAllCreditsDto().lapwingCredits(buyerLapwingCreditsDto)
				.vendorCredits(buyerVendorCreditsDto).build();
	}
}
