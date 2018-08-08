/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.classes').
        factory('shoalCommon_classes_ProductOffer', function () {

            function addRepeatingFunctions(array, itemSpec) {
                if (!array.addItem) {
                    array.addItem = function () {
                        var newItem;
                        if (typeof itemSpec === 'function') {
                            // if the spec is a class then execute the constructor
                            newItem = itemSpec();
                        } else {
                            // if the spec is an object literal then copy it
                            newItem = angular.copy(itemSpec);
                        }
                        if (itemSpec.name === "priceBandClass") {
                            if (array.length > 0) {
                                newItem.shoalMargin = array[array.length - 1].shoalMargin;
                                newItem.distributorMargin = array[array.length - 1].distributorMargin;
                            }
                        }
                        if (newItem.onAddNewItem) {
                            console.log("add new item to index " + array.length);
                            newItem.onAddNewItem(newItem, array.length, array);
                        }
                        array.push(newItem);
                    };
                }
                if (!array.removeItem) {
                    array.removeItem = function (deletable) {
                        var i,
                            len;

                        console.log("remove item");
                        for (i = 0, len = array.length; i < len; i += 1) {

                            if (array[i] === deletable) {
                                array.splice(i, 1);
                            }
                        }
                    };
                }
            }

            function priceBandClass() {

                // private data
                var my = {
                        maxVolume: undefined,
                        oldMaxVolume: undefined,
                        minVolume: 0,
                        buyerPrice: undefined,
                        shoalMargin: 0,
                        distributorMargin: 0,
                        unbounded: false
                    },
                    self = {};

                // define api
                Object.defineProperties(self, {
                    unbounded: {
                        get: function () {
                            return my.unbounded;
                        },
                        set: function (value) {
                            if (value) {
                                my.oldMaxVolume = my.maxVolume;
                                my.maxVolume = null;
                            } else {
                                my.maxVolume = my.oldMaxVolume;
                            }
                            my.unbounded = value;
                        },
                        enumerable: true
                    },
                    minVolume: {
                        get: function () {
                            return my.minVolume;
                        },
                        set: function (value) {
                            my.minVolume = value;
                        },
                        enumerable: true
                    },
                    maxVolume: {
                        get: function () {
                            return my.maxVolume;
                        },
                        set: function (value) {
                            if (my.unbounded) {
                                console.log("unbounded is true - not setting max");
                                return;
                            }
                            console.log("unbounded is false - setting max");
                            my.maxVolume = value;
                        },
                        enumerable: true
                    },
                    buyerPrice: {
                        get: function () {
                            return my.buyerPrice;
                        },
                        set: function (value) {
                            my.buyerPrice = value;
                        },
                        enumerable: true
                    },
                    shoalMargin: {
                        get: function () {
                            return my.shoalMargin;
                        },
                        set: function (value) {
                            my.shoalMargin = value;
                        },
                        enumerable: true
                    },
                    distributorMargin: {
                        get: function () {
                            return my.distributorMargin;
                        },
                        set: function (value) {
                            my.distributorMargin = value;
                        },
                        enumerable: true
                    },
                    onAddNewItem: {
                        value: function (item, index, array) {
                            var currentPriceBand;

                            if (index > 0) {
                                currentPriceBand = array[index - 1];
                                item.minVolume = currentPriceBand.maxVolume + 1;
                            }
                        }
                    }
                });

                return self;
            }

            // using douglas crockford's functional constructor pattern
            return function initProductClass(spec, my) {
                var that,
                    priceBand,
                    bandPrice,
                    specConfig = {
                        name: '',
                        description: ''
                    },
                    imageConfig = {
                        url: '',
                        description: ''
                    };
                console.log("invoking productOfferClass functional constructor - allocating memory for functions");
                my = my || {};
                my.product = {
                    id: '',
                    code: '',
                    name: '',
                    description: '',
                    review: '',
                    suitability: '',
                    processNotification: false,
                    submitNotification: false,
                    stock: 0,
                    maximumPurchaseLimit: 0,
                    termsAndConditions: '',
                    specifications: [],
                    images: [],
                    categories: [],
                    vendorName: '',
                    vatRate: {
                        code: '',
                        rate: 0.00
                    },
                    reference: '',
                    offerStartDate: '',
                    offerEndDate: '',
                    priceBands: []
                };

                if (spec) {
                    my.product.id = spec.id;
                    my.product.code = spec.code;
                    my.product.name = spec.name;
                    my.product.description = spec.description;
                    my.product.processNotification = spec.processNotification;
                    my.product.submitNotification = spec.submitNotification;
                    my.product.review = spec.review;
                    my.product.suitability = spec.suitability;
                    my.product.termsAndConditions = spec.termsAndConditions;
                    my.product.stock = spec.stock;
                    my.product.maximumPurchaseLimit = spec.maximumPurchaseLimit;
                    my.product.specifications = [];
                    (spec.specifications || []).forEach(function (s) {
                        my.product.specifications.push(s);
                    });
                    my.product.images = [];
                    (spec.images || []).forEach(function (i) {
                        my.product.images.push(i);
                    });
                    my.product.categories = spec.categories;
                    my.product.vendorName = spec.vendorName;
                    my.product.vatRate = spec.vatRate;

                    my.product.offerEndDate = spec.offerEndDate;
                    my.product.currentVolume = spec.currentVolume;

                    my.product.priceBands = [];
                    (spec.priceBands || []).forEach(function (p) {
                        my.product.priceBands.push(p);
                    });
                    my.mainImage = my.product.images.length > 0 ? my.product.images[0] : '';
                }


                addRepeatingFunctions(my.product.specifications, specConfig);
                addRepeatingFunctions(my.product.images, imageConfig);
                addRepeatingFunctions(my.product.priceBands, priceBandClass);
                my.derivePriceBandIndex = function (volume) {
                    var priceBands = my.product.priceBands,
                        i,
                        len;

                    for (i = 0, len = priceBands.length; i < len; i += 1) {
                        if (volume >= priceBands[i].minVolume) {
                            if (!priceBands[i].maxVolume || volume <= priceBands[i].maxVolume) {
                                return i;
                            }
                        }
                    }
                    return priceBands.length - 1;
                };
                my.originalPrice = function () {
                    return my.product.priceBands[0].buyerPrice;
                };
                my.currentPrice = function () {
                    return my.product.priceBands[my.derivePriceBandIndex(my.product.currentVolume)].buyerPrice;
                };
                my.targetPrice = function () {
                    return my.product.priceBands[my.product.priceBands.length - 1].buyerPrice;
                };
                my.calculatePricing = function (quantity) {
                    var originalPrice = Number(my.originalPrice()),
                        volume = Number(my.product.currentVolume);

                    quantity = quantity || 0;
                    if (quantity < 0) {
                        throw new Error('quantity was not set');
                    }

                    if (quantity) {
                        volume += Number(quantity);
                    }

                    priceBand = my.product.priceBands[my.derivePriceBandIndex(volume)];
                    bandPrice = Number(priceBand.buyerPrice);

                    return {
                        unitPrice: bandPrice,
                        unitDiscount: originalPrice - bandPrice
                    };
                };
                my.getProduct = function () {
                    // strip all functions
                    return JSON.parse(JSON.stringify(my.product));
                };

                that = Object.create({}, {
                    id: {
                        get: function () {
                            return my.product.id;
                        },
                        set: function (value) {
                            my.product.id = value;
                        }
                    },
                    code: {
                        get: function () {
                            return my.product.code;
                        },
                        set: function (value) {
                            my.product.code = value;
                        }
                    },
                    name: {
                        get: function () {
                            return my.product.name;
                        },
                        set: function (value) {
                            my.product.name = value;
                        }
                    },
                    description: {
                        get: function () {
                            return my.product.description;
                        },
                        set: function (value) {
                            my.product.description = value;
                        }
                    },
                    review: {
                        get: function () {
                            return my.product.review;
                        },
                        set: function (value) {
                            my.product.review = value;
                        }
                    },
                    suitability: {
                        get: function () {
                            return my.product.suitability;
                        },
                        set: function (value) {
                            my.product.suitability = value;
                        }
                    },
                    processNotification: {
                        get: function () {
                            return my.product.processNotification;
                        },
                        set: function (value) {
                            my.product.processNotification = value;
                        }
                    },
                    submitNotification: {
                        get: function () {
                            return my.product.submitNotification;
                        },
                        set: function (value) {
                            my.product.submitNotification = value;
                        }
                    },
                    termsAndConditions: {
                        get: function () {
                            return my.product.termsAndConditions;
                        },
                        set: function (value) {
                            my.product.termsAndConditions = value;
                        }
                    },
                    stock: {
                        get: function () {
                            return my.product.stock;
                        },
                        set: function (value) {
                            my.product.stock = value;
                        }
                    },
                    maximumPurchaseLimit: {
                        get: function () {
                            return my.product.maximumPurchaseLimit;
                        },
                        set: function (value) {
                            my.product.maximumPurchaseLimit = value;
                        }
                    },
                    specifications: {
                        get: function () {
                            return my.product.specifications;
                        }
                    },
                    categories: {
                        get: function () {
                            return my.product.categories;
                        },
                        set: function (value) {
                            my.product.categories = value;
                        }
                    },
                    mainCategory: {
                        get: function () {
                            return my.product.categories[0];
                        },
                        set: function (value) {
                            my.product.categories = [ value ];
                        }
                    },
                    currentVolume: {
                        get: function () {
                            return my.product.currentVolume;
                        }
                    },
                    vendorName: {
                        get: function () {
                            return my.product.vendorName;
                        },
                        set: function (value) {
                            my.product.vendorName = value;
                        }
                    },
                    offerReference: {
                        get: function () {
                            return my.product.reference;
                        },
                        set: function (value) {
                            my.product.reference = value;
                        }
                    },
                    offerStartDate: {
                        get: function () {
                            return my.product.offerStartDate;
                        },
                        set: function (value) {
                            my.product.offerStartDate = value;
                        }
                    },
                    offerEndDate: {
                        get: function () {
                            return my.product.offerEndDate;
                        },
                        set: function (value) {
                            my.product.offerEndDate = value;
                        }
                    },
                    vatRate: {
                        get: function () {
                            return my.product.vatRate;
                        },
                        set: function (value) {
                            my.product.vatRate = value;
                        }
                    },
                    priceBands: {
                        get: function () {
                            return my.product.priceBands;
                        }
                    },
                    images: {
                        get: function () {
                            return my.product.images;
                        }
                    },
                    mainImage: {
                        get: function () {
                            return my.mainImage;
                        }
                    },
                    originalPrice: {
                        get: my.originalPrice
                    },
                    currentPrice: {
                        get: my.currentPrice
                    },
                    targetPrice: {
                        get: my.targetPrice
                    }
                });
                that.calculatePricing = my.calculatePricing;
                that.getProduct = my.getProduct;

                return that;
            };
        });
}());
