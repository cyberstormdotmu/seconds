package com.ishoal.core.orders;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.AddressDatabaseExceptionHandler;
import com.ishoal.core.domain.Users;
import com.ishoal.core.repository.UserRepository;

public class RegisterUserConfirmationService {
	private final AddressDatabaseExceptionHandler addressDatabaseExceptionHandler = new AddressDatabaseExceptionHandler();
	private final UserRepository UserRepository;
	public RegisterUserConfirmationService( UserRepository UserRepository) {
		this.UserRepository = UserRepository;
	}
	
	@Transactional
	public PayloadResult<Users> confirm(String confirm) {
		PayloadResult<Users> payload = null;
		Users Users = null;
		
			Users = UserRepository.findUsersByConfirmationStatus(confirm);
		if (Users != null) {
			payload = PayloadResult.success(Users);
		}
		return payload != null ? payload : PayloadResult.error("Unexpected error");

		}
	
	}
