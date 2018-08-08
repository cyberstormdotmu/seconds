(function () {
    'use strict';
    var selectWrapper = require('../../utils/selectWrapper'),
        my = {};

    /**
     * Enable accessing a repeating element using an index, assuming it uses a convention of
     * input_name_<index>, can accept either a String representing the field name, or an object literal of the form
     * {
     *  name: 'field_name',
     *  action: function () {}
     * }
     */
    function accessRepeaterByIndex(index, properties) {
        var obj = {},
            that = this,
            keys = Object.keys(properties);

        keys.forEach(function (key) {
            var input = properties[key];

            if (typeof input === 'object') {
                Object.defineProperty(obj, key, {
                    value: function () {
                        return input.action(that.form.element(by.name(input.name + "_" + index)));
                    }
                });
            } else {
                Object.defineProperty(obj, key, {
                    get: function () {
                        return that.form.element(by.name(input + "_" + index));
                    },
                    set: function (value) {
                        that.form.element(by.name(input + "_" + index)).clear().sendKeys(value);
                    }
                });
            }
        });

        return obj;
    }

    function selectGet(name) {
        return function () {
            var select = selectWrapper(by.name(name));
            return select.getSelectedOptions();
        };
    }

    function selectSet(name) {
        return function (option) {
            var select = selectWrapper(by.name(name));
            select.selectByLabel(option);
        };
    }

    function padNumberAsTwoDigitString(number) {
        return number < 10 ? '0' + number : String(number);
    }

    function filterByEnabledButtons(btnEle) {
        return btnEle.isEnabled().then(function (isEnabled) {
            return isEnabled;
        });
    }

    function clickFirstButtonIfAvailable(allButtonEles) {
        if (allButtonEles.length > 0) {
            // click first available
            allButtonEles[0].click();
        }
    }

    my.manageProductPage = function () {
        return Object.create({}, {
            goto: {
                value: function () {
                    return browser.get('/admin/#/manageProducts/');
                }
            }
        });
    };

    my.createProductForm = function () {
        return Object.create({}, {
            form: {
                get: function () {
                    return element(by.id("create-product-form"));
                }
            },
            submit: {
                get: function () {
                    return this.form.element(by.xpath("//button[@type='submit']"));
                }
            },
            productName: {
                get: function () {
                    return this.form.element(by.name("product_name"));
                },
                set: function (value) {
                    this.form.element(by.name("product_name")).clear().sendKeys(value);
                }
            },
            productCode: {
                get: function () {
                    return this.form.element(by.name("product_code"));
                },
                set: function (value) {
                    this.form.element(by.name("product_code")).clear().sendKeys(value);
                }
            },
            productCategory: {
                get: selectGet('product_category'),
                set: selectSet('product_category')
            },
            vendorName: {
                get: selectGet('product_vendorName'),
                set: selectSet('product_vendorName')
            },
            vatRate: {
                get: selectGet('product_vatRate'),
                set: selectSet('product_vatRate')
            },
            description: {
                get: function () {
                    return this.form.element(by.name("product_description"));
                },
                set: function (value) {
                    this.form.element(by.name("product_description")).clear().sendKeys(value);
                }
            },
            specifications: {
                value: function (index) {
                    return accessRepeaterByIndex.bind(this, index, {
                        name: 'spec_name',
                        description: 'spec_description'
                    })();
                }
            },
            addSpecification: {
                value: function () {
                    return this.form.element(by.id("add-spec-button")).click();
                }
            },
            images: {
                value: function (index) {
                    return accessRepeaterByIndex.bind(this, index, {
                        url: 'image_url',
                        description: 'image_description'
                    })();
                }
            },
            addImage: {
                value: function () {
                    return this.form.element(by.id("add-image-button")).click();
                }
            },
            pickOfferStartDayInCurrentMonth: {
                value: function (day) {
                    this.form.element(by.id("start-date-picker-button")).click();
                    this.form.all(by.xpath("//div[@id='start-date-picker-wrapper']//table//span[text()='" + padNumberAsTwoDigitString(day) + "']"))
                        .filter(filterByEnabledButtons)
                        .then(clickFirstButtonIfAvailable);
                }
            },
            pickOfferEndDayInCurrentMonth: {
                value: function (day) {
                    this.form.element(by.id("end-date-picker-button")).click();
                    this.form.all(by.xpath("//div[@id='end-date-picker-wrapper']//table//span[text()='" + padNumberAsTwoDigitString(day) + "']"))
                        .filter(filterByEnabledButtons)
                        .then(clickFirstButtonIfAvailable);
                }
            },
            priceBands: {
                value: function (index) {
                    return accessRepeaterByIndex.bind(this, index, {
                        setUnboundedCheckBox: {
                            name: 'band_unbounded_max',
                            action: function (element) {
                                return element.click();
                            }
                        },
                        minVolume: 'band_start',
                        maxVolume: 'band_end',
                        buyerPrice: 'buyer_price',
                        vendorPrice: 'vendor_price',
                        shoalMargin: 'shoal_margin',
                        distributorMargin: 'distributor_margin'
                    })();
                }
            },
            addPriceBand: {
                value: function () {
                    return this.form.element(by.id("add-band-button")).click();
                }
            }
        });
    };

    module.exports = {
        manageProduct: my.manageProductPage(),
        createProductForm: my.createProductForm()
    };

}());
