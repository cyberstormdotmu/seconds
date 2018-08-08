package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.UserDto;

public class UserDtoAdapter {
	public UserDtoAdapter() {

	}

	public UserDto adapt(User user) {

		if (user == null) {
			return null;
		}
		return UserDto.aUserDto().firstName(user.getForename()).surname(user.getSurname())
				.emailAddress(user.getEmailAddress()).mobileNumber(user.getMobileNumber())
				.westcoastAccountNumber(user.getWestcoastAccountNumber())
				.buyerReferralCode(user.getBuyerReferralCode())
				.registrationToken(user.getRegistrationToken())
				.lapwingAccountNumber(user.getLapwingAccountNumber()).build();
	}

	public User adapt(UserDto user) {
		if (user == null) {
			return null;
		}
		return User.aUser().forename(user.getFirstName()).surname(user.getSurname())
				.username(user.getEmailAddress()).mobileNumber(user.getMobileNumber()).build();
	}

	public User adapt(UserDto userdto, User user) {
		if (user == null) {
			return null;
		}
		return User.aUser().username(userdto.getEmailAddress())
				.forename(userdto.getFirstName())
				.surname(userdto.getSurname())
				.hashedPassword(user.getHashedPassword())
				.mobileNumber(userdto.getMobileNumber())
				.lapwingAccountNumber(userdto.getLapwingAccountNumber())
				.westcoastAccountNumber(userdto.getWestcoastAccountNumber())
				.buyerReferralCode(userdto.getBuyerReferralCode())
				.vendor(userdto.getVendor())
				.registrationToken(user.getRegistrationToken())
				.build();
	}
}
