(function () {
    'use strict';
    var registrationPage = function () {
        return Object.create({}, {
            goto: {
                value: function () {
                    return browser.get('/public/#/registration');
                }
            },
            form: {
                get: function () {
                    return element(by.id('registrationForm'));
                }
            },
            emailAddress: {
                get: function () {
                    return this.form.element(by.name("emailAddress"));
                },
                set: function (value) {
                    this.form.element(by.name("emailAddress")).clear().sendKeys(value);
                }
            },
            forename: {
                get: function () {
                    return this.form.element(by.name("forename"));
                },
                set: function (value) {
                    this.form.element(by.name("forename")).clear().sendKeys(value);
                }
            },
            surname: {
                get: function () {
                    return this.form.element(by.name("surname"));
                },
                set: function (value) {
                    this.form.element(by.name("surname")).clear().sendKeys(value);
                }
            },
            organisationName: {
                get: function () {
                    return this.form.element(by.name("organisation_name"));
                },
                set: function (value) {
                    this.form.element(by.name("organisation_name")).clear().sendKeys(value);
                }
            },
            organisationRegNumber: {
                get: function () {
                    return this.form.element(by.name("organisation_reg_number"));
                },
                set: function (value) {
                    this.form.element(by.name("organisation_reg_number")).clear().sendKeys(value);
                }
            },
            contactMobile: {
                get: function () {
                    return this.form.element(by.name("organisation_mobileNumber"));
                },
                set: function (value) {
                    this.form.element(by.name("organisation_mobileNumber")).clear().sendKeys(value);
                }
            },
            password: {
                get: function () {
                    return this.form.element(by.name("password"));
                },
                set: function (value) {
                    this.form.element(by.name("password")).clear().sendKeys(value);
                }
            },
            submit: {
                value: function () {
                    this.form.all(by.xpath("//button[@type='submit']")).get(0).click();
                }
            }
        });
    };

    module.exports = {
        registrationForm: registrationPage()
    };

}());
