(function () {
    'use strict';

    var updateProfileWithDefaults = function (profileForm) {
        profileForm.contactTitle = "Mr";
        profileForm.contactFirstName = "Roger";
        profileForm.contactSurname = "Watkins";
        profileForm.contactEmail = "rogerwatkins@gmail.com";
        profileForm.contactPhone = "01239481410";
        profileForm.contactMobile = "12345678911";

        profileForm.deliveryDepartment = "HP Inc.";
        profileForm.deliveryBuilding = "Pt. Ground Floor";
        profileForm.deliveryStreet = "Filton Road";
        profileForm.deliveryLocality = "Stoke Gifford";
        profileForm.deliveryTown = "Bristol";
        profileForm.deliveryPostcode = "BS34 8QZ";

        profileForm.bankAccountName = "MR R E WATKINS";
        profileForm.bankSortCode = "88-88-88";
        profileForm.bankAccountNumber = "88888888";
        profileForm.bankName = "Barclays";
        profileForm.bankBuilding = "1";
        profileForm.bankStreet = "Churchill Street";
        profileForm.bankLocality = "Canary Wharf";
        profileForm.bankTown = "London";
        profileForm.bankPostcode = "E14 5PQ";

        return profileForm.submit();
    };

    module.exports = {
        updateProfileWithDefaults: updateProfileWithDefaults
    };
}());
