package com.ishoal.core.buyer;

import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.security.SecurePassword;

import static com.ishoal.core.buyer.FetchBuyerProfileRequest.aFetchBuyerProfileRequest;
import static com.ishoal.core.buyer.RegisterBuyerRequest.aCreateBuyerProfileRequest;
import static com.ishoal.core.buyer.UpdateBuyerProfileRequest.anUpdateBuyerProfileRequest;
import static com.ishoal.core.domain.Address.anAddress;
import static com.ishoal.core.domain.BankAccount.aBankAccount;
import static com.ishoal.core.domain.Contact.aContact;
import static com.ishoal.core.domain.Organisation.anOrganisation;
import static com.ishoal.core.domain.User.aUser;

public class BuyerProfileServiceTestData {

    public static final String AN_EDITED_USER_PROFILE = "anincompleteuser@gmail.com";
    public static final String AN_UNEDITED_USER_PROFILE = "abrandnewuser@gmail.com";
    public static final String A_COMPLETED_USER_PROFILE = "acompleteduser@gmail.com";
    public static final String ORGANISATION_NAME = "HP Limited";
    public static final String CONTACT_EMAIL = "contact@thecompany.com";
    public static final String ADDRESS_DEPARTMENT = "some department";
    public static final String BANK_ACCOUNT_NUMBER = "88888888";

    private BuyerProfileServiceTestData() {

    }

    public static FetchBuyerProfileRequest buildAFetchBuyerProfileRequest() {

        return aFetchBuyerProfileRequest().user(buildAUser().build()).build();
    }

    public static UpdateBuyerProfileRequest.Builder buildUpdateBuyerProfileRequest() {

        return anUpdateBuyerProfileRequest()
            .user(buildAUser().build())
            .organisation(anOrganisation().name(ORGANISATION_NAME).registrationNumber("07496791").build())
            .contact(aContact().title("Mr").firstName("Roger").surname("Watkins")
                .emailAddress(CONTACT_EMAIL).phoneNumber("07493845094").build())
            .deliveryAddress(anAddress().departmentName(ADDRESS_DEPARTMENT).buildingName("Floor")
                .streetAddress("Filton Road Stoke Gifford, Pt. Ground").locality("").postTown("BRISTOL")
                .postcode("BS24 8QZ").build())
            .bankAccount(aBankAccount().accountName("MR R E WATKINS").sortCode("88-88-88")
                .accountNumber(BANK_ACCOUNT_NUMBER).bankName("Barclays").buildingName("1").streetAddress("Churchill place")
                .locality("Canary Wharf").postTown("LONDON").postcode("E145HP").build());
    }

    public static BuyerProfile.Builder buildCompleteProfile() {
        return buildIncompleteProfile().isCompleted(true);
    }

    public static BuyerProfile.Builder buildIncompleteProfile() {

        return BuyerProfile.aBuyerProfile()
            .user(buildAUser().build())
            .organisation(anOrganisation().name(ORGANISATION_NAME).registrationNumber("07496791").build())
            .contact(aContact().title("Mr").firstName("Roger").surname("Watkins")
                .emailAddress(CONTACT_EMAIL).phoneNumber("07493845094").build())
            .bankAccount(aBankAccount().accountName("MR R E WATKINS").sortCode("88-88-88")
                .accountNumber(BANK_ACCOUNT_NUMBER).bankName("Barclays").buildingName("1").streetAddress("Churchill place")
                .locality("Canary Wharf").postTown("LONDON").postcode("E145HP").build());
    }

    public static RegisterBuyerRequest.Builder buildRegisterBuyerRequest() {

        return aCreateBuyerProfileRequest().user(
            aUser().forename("Roger").surname("Watkins").username(AN_EDITED_USER_PROFILE).hashedPassword(
                SecurePassword.fromClearText("password"))
                .build())
            .organisation(anOrganisation().name(ORGANISATION_NAME)
                .registrationNumber("07496791").build()
            );
    }

    public static User.Builder buildAUser() {

        return aUser().username(AN_EDITED_USER_PROFILE)
            .id(UserId.from((1000L)))
            .forename("roger").surname("watkins")
            .hashedPassword(SecurePassword.fromClearText("password"));
    }

    public static Organisation buildAnOrganisation() {

        return anOrganisation().name(ORGANISATION_NAME)
            .registrationNumber("07496792").build();
    }
}