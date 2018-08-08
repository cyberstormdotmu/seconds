package com.ishoal.ws.buyer.controller;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.core.matchers.GenericMatcher.lamdaMatch;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.buyer.AddressRequest;
import com.ishoal.core.buyer.ManageAddressService;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.ws.common.dto.AddressDto;

@RunWith(MockitoJUnitRunner.class)
public class AddressControllerTest {

	private static final String USER_NAME = "rogerwatkins@gmail.com";

	@Mock
	private ManageAddressService manageAddressService;

	private AddressController addressController;
	private ArgumentCaptor<User> userRequest = ArgumentCaptor.forClass(User.class);
	private ArgumentCaptor<AddressRequest> addressRequest = ArgumentCaptor.forClass(AddressRequest.class);
	private User user;

	@Before
	public void before() {

		Address payload = anAddress().build();
		addressController = new AddressController(manageAddressService);
		when(manageAddressService.saveAddressofBuyer(userRequest.capture(), addressRequest.capture()))
				.thenReturn(PayloadResult.success(payload));

		when(manageAddressService.saveAddressofBuyer(userRequest.capture(), deptNameEquals("Sales")))
				.thenReturn(PayloadResult.error(ErrorType.CONFLICT, "an error occured"));

		user = buildAUser().build();
	}

	@Test
	public void shouldGiveAnOKResponse_WhenAddNewAddressSuccessful() {

		ResponseEntity<?> response = addressController.addNewAddress(buildAUser().build(), buildAddressDTO().build());
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

	@Test
	public void shouldInvokeBuyerAddressService_whenAddNewAddressCalled() {

		addressController.addNewAddress(buildAUser().build(), buildAddressDTO().build());
		verify(manageAddressService).saveAddressofBuyer(any(User.class), any(AddressRequest.class));
	}

	@Test
	public void shouldRaiseConflictError_WhenServiceReportsDepartmentExists() {

		ResponseEntity<?> response = addressController.addNewAddress(buildAUser().build(),
				buildDuplicateAddressDTO().build());
		assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
	}

	private Address.Builder anAddress() {
		return Address.anAddress().buildingName("building").departmentName("department").streetAddress("street")
				.locality("local").postTown("pt").postcode("LS23 6QR");
	}

	private static AddressDto.Builder buildAddressDTO() {
		return AddressDto.anAddressDto().buildingName("building").departmentName("department").streetAddress("street")
				.locality("local").postTown("pt").postcode("LS23 6QR");
	}

	private static AddressDto.Builder buildDuplicateAddressDTO() {
		return AddressDto.anAddressDto().buildingName("building").departmentName("Sales").streetAddress("street")
				.locality("local").postTown("pt").postcode("LS23 6QR");
	}

	private static AddressRequest.Builder buildAnDuplicateAddressRequest() {
		return AddressRequest.aAddNewAddressRequest().buildingName("building").departmentName("Sales")
				.streetAddress("street").locality("local").postTown("pt").postcode("LS23 6QR");
	}

	public static User.Builder buildADuplicateUser() {

		return aUser().username("oliver.squires@ishoal.com").id(UserId.from((1L))).forename("Oliver").surname("Squires")
				.hashedPassword(SecurePassword.fromClearText("password"));
	}

	public AddressRequest deptNameEquals(String deptName) {

		return (AddressRequest) argThat(lamdaMatch(obj -> deptName.equals(((AddressRequest) obj).getDepartmentName())));
	}

}
