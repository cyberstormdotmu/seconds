package com.ishoal.app.integrationtest.data;

import static com.ishoal.ws.buyer.dto.BankAccountDto.aBankAccount;
import static com.ishoal.ws.buyer.dto.BuyerProfileDto.aBuyerProfileDto;
import static com.ishoal.ws.buyer.dto.BuyerRegistrationDto.aBuyerRegistrationDto;
import static com.ishoal.ws.buyer.dto.ContactDto.aContact;
import static com.ishoal.ws.buyer.dto.OrganisationDto.anOrganisationDto;
import static com.ishoal.ws.buyer.dto.UserDto.aNewUserDto;

import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.ishoal.ws.buyer.dto.BuyerRegistrationDto;
import com.ishoal.ws.buyer.dto.UserDto;

public class BuyerProfileTestData {

    public static final String NEW_USER_NAME = "rogerwatkins@hotmail.co.uk";
    public static final String NEW_USER_PASSWORD = "password";

    public static final String ORGANISATION_NAME = "HP Limited";
    public static final String ORGANISATION_REG = "07496791";

    public static final String CONTACT_TITLE = "Mr";
    public static final String CONTACT_FIRST_NAME = "Roger";
    public static final String CONTACT_SURNAME = "Watkins";
    public static final String CONTACT_EMAIL_ADDRESS = "adifferentemail@gmail.com";
    public static final String CONTACT_PHONE_NUMBER = "07493845094";

    public static final String DELIVERY_ADDRESS_DEPARTMENT = "IT";
    public static final String DELIVERY_ADDRESS_BUILDING = "Floor";
    public static final String DELIVERY_ADDRESS_STREET = "Filton Road Stoke Gifford, Pt. Ground";
    public static final String DELIVERY_ADDRESS_LOCALITY = "somewhere in bristol";
    public static final String DELIVERY_ADDRESS_POSTTOWN = "BRISTOL";
    public static final String DELIVERY_ADDRESS_POSTCODE = "BS24 8QZ";

    public static final String BANK_ACCOUNT_NAME = "MR R E WATKINS";
    public static final String BANK_ACCOUNT_SORTCODE = "88-88-88";
    public static final String BANK_ACCOUNT_NUMBER = "88888888";
    public static final String BANK_NAME = "Barclays";
    public static final String BANK_ACCOUNT_BUILDING_NAME = "1";
    public static final String BANK_ACCOUNT_STREET_NAME = "Churchill place";
    public static final String BANK_ACCOUNT_LOCALITY = "Canary Wharf";
    public static final String BANK_ACCOUNT_POSTTOWN = "LONDON";
    public static final String BANK_ACCOUNT_POSTCODE = "E145HP";

    private BuyerProfileTestData() {}

    public static BuyerRegistrationDto.Builder buildABuyerRegistrationDto() {

        return aBuyerRegistrationDto().buyer(buildAUserDto().build())
            .organisation(anOrganisationDto().name(ORGANISATION_NAME)
                .registrationNumber(ORGANISATION_REG).build());
    }

    public static BuyerProfileDto.Builder buildABuyerProfile() {

        return aBuyerProfileDto()
            .organisation(anOrganisationDto().name(ORGANISATION_NAME).registrationNumber(ORGANISATION_REG).build())
            .contact(aContact().title(CONTACT_TITLE).firstName(CONTACT_FIRST_NAME).surname(CONTACT_SURNAME)
                .emailAddress(CONTACT_EMAIL_ADDRESS).password(NEW_USER_PASSWORD).phoneNumber(CONTACT_PHONE_NUMBER).build())
            .bankAccount(aBankAccount().accountName(BANK_ACCOUNT_NAME).sortCode(BANK_ACCOUNT_SORTCODE)
                .accountNumber(BANK_ACCOUNT_NUMBER).bankName(BANK_NAME).buildingName(BANK_ACCOUNT_BUILDING_NAME)
                .streetAddress(BANK_ACCOUNT_STREET_NAME).locality(BANK_ACCOUNT_LOCALITY).postTown(BANK_ACCOUNT_POSTTOWN)
                .postcode(BANK_ACCOUNT_POSTCODE).build());
    }

    private static UserDto.Builder buildAUserDto() {

        return aNewUserDto().firstName("Roger")
            .surname("Watkins")
            .emailAddress(NEW_USER_NAME)
            .password(NEW_USER_PASSWORD);
    }
}
