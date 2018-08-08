package com.ishoal.core.domain;

public class AddressTestData {

    public static Address.Builder anAddress() {
        return Address.anAddress()
                .organisationName("Shoal")
                .departmentName("Development")
                .buildingName("The Old Hall")
                .locality("Bramham")
                .postTown("Wetherby")
                .postcode("LS23 6QR");
    }
}
