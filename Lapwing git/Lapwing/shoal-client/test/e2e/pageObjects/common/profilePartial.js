(function () {
    'use strict';
    var profileForm = function () {
        var formEle = element(by.name("profileForm")),
            EC = protractor.ExpectedConditions;
        return Object.create({}, {
            isPresent: {
                get: function () {
                    return formEle.isPresent();
                }
            },
            contactTitle: {
                get: function () {
                    return formEle.element(by.name("contact_title"));
                },
                set: function (amount) {
                    this.contactTitle.clear().sendKeys(amount);
                }
            },
            contactFirstName: {
                get: function () {
                    return formEle.element(by.name("contact_firstName"));
                },
                set: function (amount) {
                    this.contactFirstName.clear().sendKeys(amount);
                }
            },
            contactSurname: {
                get: function () {
                    return formEle.element(by.name("contact_surname"));
                },
                set: function (amount) {
                    this.contactSurname.clear().sendKeys(amount);
                }
            },
            contactEmail: {
                get: function () {
                    return formEle.element(by.name("contact_emailAddress"));
                },
                set: function (amount) {
                    this.contactEmail.clear().sendKeys(amount);
                }
            },
            contactPhone: {
                get: function () {
                    return formEle.element(by.name("contact_phoneNumber"));
                },
                set: function (amount) {
                    this.contactPhone.clear().sendKeys(amount);
                }
            },
            deliveryDepartment: {
                get: function () {
                    return formEle.element(by.name("delivery_department"));
                },
                set: function (amount) {
                    this.deliveryDepartment.clear().sendKeys(amount);
                }
            },
            deliveryBuilding: {
                get: function () {
                    return formEle.element(by.name("delivery_building"));
                },
                set: function (amount) {
                    this.deliveryBuilding.clear().sendKeys(amount);
                }
            },
            deliveryStreet: {
                get: function () {
                    return formEle.element(by.name("delivery_street"));
                },
                set: function (amount) {
                    this.deliveryStreet.clear().sendKeys(amount);
                }
            },
            deliveryLocality: {
                get: function () {
                    return formEle.element(by.name("delivery_locality"));
                },
                set: function (amount) {
                    this.deliveryLocality.clear().sendKeys(amount);
                }
            },
            deliveryTown: {
                get: function () {
                    return formEle.element(by.name("delivery_town"));
                },
                set: function (amount) {
                    this.deliveryTown.clear().sendKeys(amount);
                }
            },
            deliveryPostcode: {
                get: function () {
                    return formEle.element(by.name("delivery_postcode"));
                },
                set: function (amount) {
                    this.deliveryPostcode.clear().sendKeys(amount);
                }
            },
            bankAccountName: {
                get: function () {
                    return formEle.element(by.name("bank_account_name"));
                },
                set: function (amount) {
                    this.bankAccountName.clear().sendKeys(amount);
                }
            },
            bankSortCode: {
                get: function () {
                    return formEle.element(by.name("bank_sort_code"));
                },
                set: function (amount) {
                    this.bankSortCode.clear().sendKeys(amount);
                }
            },
            bankAccountNumber: {
                get: function () {
                    return formEle.element(by.name("bank_account_number"));
                },
                set: function (amount) {
                    this.bankAccountNumber.clear().sendKeys(amount);
                }
            },
            bankName: {
                get: function () {
                    return formEle.element(by.name("bank_name"));
                },
                set: function (amount) {
                    this.bankName.clear().sendKeys(amount);
                }
            },
            bankBuilding: {
                get: function () {
                    return formEle.element(by.name("bank_building_name"));
                },
                set: function (amount) {
                    this.bankBuilding.clear().sendKeys(amount);
                }
            },
            bankStreet: {
                get: function () {
                    return formEle.element(by.name("bank_street"));
                },
                set: function (amount) {
                    this.bankStreet.clear().sendKeys(amount);
                }
            },
            bankLocality: {
                get: function () {
                    return formEle.element(by.name("bank_locality"));
                },
                set: function (amount) {
                    this.bankLocality.clear().sendKeys(amount);
                }
            },
            bankTown: {
                get: function () {
                    return formEle.element(by.name("bank_town"));
                },
                set: function (amount) {
                    this.bankTown.clear().sendKeys(amount);
                }
            },
            bankPostcode: {
                get: function () {
                    return formEle.element(by.name("bank_postcode"));
                },
                set: function (amount) {
                    this.bankPostcode.clear().sendKeys(amount);
                }
            },
            submit: {
                value: function () {
                    return formEle.element(by.xpath("//button[@type='submit']")).click();
                }
            },
            waitForProfileFormToDisappear: {
                value: function () {
                    return browser.wait(EC.not(EC.visibilityOf($('#profile-partial'))), 30000);
                }
            },
            waitForLoad: {
                value: function (afterLoad) {
                    browser.sleep(1000);
                    browser.refresh();
                    browser.wait(EC.visibilityOf($('#profile-partial')), 30000);
                    expect(element(by.id("profile-partial")).isDisplayed()).toBe(true, ' ProfileForm was not loaded');
                    if (afterLoad) {
                        return afterLoad();
                    }
                }
            },
            waitForProfileAccepted: {
                value: function () {
                    browser.wait(EC.visibilityOf($('#profile-accepted')), 30000);
                }
            },
            isProfileAccepted: {
                get: function () {
                    this.waitForProfileAccepted();
                    return element(by.id("profile-accepted")).isPresent();
                }
            }
        });
    };

    module.exports = {
        profileForm: profileForm()
    };

}());
