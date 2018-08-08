package com.ishoal.core.buyer;

import org.springframework.dao.DataIntegrityViolationException;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.AddressRepository;

public class ManageAddressService {

	private final AddressRepository addressRepository;
	private final AddressDatabaseExceptionHandler addressDatabaseExceptionHandler = new AddressDatabaseExceptionHandler();
	private final AddressAdapter addressAdapter = new AddressAdapter();

	public ManageAddressService(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public PayloadResult<Address> saveAddressofBuyer(User user, AddressRequest addressRequest) {
		PayloadResult<Address> payload = null;
		Address addedAddress = null;

		try {
			addedAddress = addressRepository.saveAddress(user, addressAdapter.buildRequest(addressRequest));
		} catch (DataIntegrityViolationException e) {
			payload = handleDatabaseException(addressRequest, e);
		}

		if (addedAddress != null) {
			payload = PayloadResult.success(addedAddress);
		}

		return payload != null ? payload : PayloadResult.error("Unexpected error");

	}

	private PayloadResult<Address> handleDatabaseException(AddressRequest request,
			DataIntegrityViolationException dbException) {

	              	return addressDatabaseExceptionHandler.handleDatabaseException(request, dbException);
	}

	public PayloadResult<Address> deleteAddressofBuyer(Long id) {
		PayloadResult<Address> payload = null;
		Result result;
		PayloadResult<Address> findResult = addressRepository.findAddressValidatingVersion(id);
		result = findResult;
		if (result.isSuccess()) {
			Address address = findResult.getPayload();
			AddressRequest addressRequest = AddressRequest.aAddNewAddressRequest().id(address.getId())
					.organisationName(address.getOrganisationName()).departmentName(address.getDepartmentName())
					.buildingName(address.getBuildingName()).streetAddress(address.getStreetAddress())
					.locality(address.getLocality()).postTown(address.getPostTown()).postcode(address.getPostcode())
					.build();
			try {
				result = addressRepository.deleteAddress(address, id);
			} catch (DataIntegrityViolationException e) {
				payload = handleDatabaseException(addressRequest, e);
			}
			if (payload == null) {
				payload = PayloadResult.success(address);
			}
			return payload;
		}
		return PayloadResult.error("Unexpected Error");
	}
}