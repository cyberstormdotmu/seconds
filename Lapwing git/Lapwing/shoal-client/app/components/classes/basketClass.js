/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.classes').
        factory('shoalApp_classes_Basket', function () {

            var basketException = function (message) {

                return {
                    message: message,
                    name: 'BasketException'
                };
            };

            // using douglas crockford's functional constructor pattern
            return function initBasketClass(spec, my) {
                var that,
                    modified = false,
                    itemCount = function () {
                        return Object.keys(my.items || []).length;
                    },
                    isEmpty = function () {
                        return that.itemCount === 0;
                    },
                    forEach = function (fn) {
                        var i,
                            keys = Object.keys(my.items),
                            key;

                        for (i = 0; i < keys.length; i += 1) {
                            key = keys[i];
                            fn(my.items[key]);
                        }
                    },
                    isModified = function () {
                        // only detects adds and removes - not clever enough to detect item updates yet
                        return modified;
                    },
                    copyArray = function (items) {
                        if (!Array.isArray(items)) {
                            throw new Error('items was not an array');
                        }
                        var i,
                            len = items.length,
                            key,
                            item,
                            getQuantity = function () {
                                return this.quantity;
                            },
                            setQuantity = function (quantity) {
                                this.quantity = quantity;
                                if (quantity === undefined) {
                                    return;
                                }
                                // whenever the client updates the quantity, this object will
                                // remember it has changed.
                                modified = true;

                                // provided by basket service
                                this.updatePrice(quantity);
                            };
                        // convert regular array into associative array
                        for (i = 0; i < len; i += 1) {
                            key = items[i].productCode;
                            item = items[i];
                            Object.defineProperty(item, 'bindQuantity', {
                                get: getQuantity,
                                set: setQuantity
                            });

                            my.items[key] = item;
                        }
                    },
                    removeItem = function (item) {
                        if (typeof item !== 'object') {
                            throw new Error('item was not an object');
                        }
                        if (my.items[item.productCode] !== undefined) {
                            delete my.items[item.productCode];
                            modified = true;
                        }
                    },
                    removeAll = function () {
                        my.items = {};
                        modified = true;
                    },
                    addItem = function (item) {
                        if (typeof item !== 'object') {
                            throw new Error('item was not an object');
                        }
                        if (my.items[item.productCode] === undefined) {
                            my.items[item.productCode] = item;
                            modified = true;
                        } else {
                            throw basketException('Tried to add the same item twice');
                        }
                    },
                    containsItem = function (item) {
                        if (typeof item !== 'object') {
                            throw new Error('item was not an object');
                        }
                        return my.items[item.productCode] !== undefined;
                    },
                    calculateSubTotal = function () {
                        var total = 0.00;
                        forEach(function (item) {
                            total += Number(item.quantity) * Number(item.unitPrice);
                        });
                        return total;
                    },
                    calculateVatTotal = function () {
                        var total = 0.00;
                        forEach(function (item) {
                            var vat;
                            if (item.vatRate) {
                                vat = Math.round(item.unitPrice * item.vatRate.rate) / 100;
                                total += Number(item.quantity) * vat;
                            }
                        });
                        return total;
                    },
                    calculateGrossTotal = function () {
                        return calculateSubTotal() + calculateVatTotal();
                    },
                    calculateDiscountTotal = function () {
                        var total = 0.00;
                        forEach(function (item) {
                            total += Number(item.quantity) * (Number(item.initialUnitPrice) - Number(item.unitPrice));
                        });
                        return total;
                    };

                my = my || {};
                my.items = {}; //associative array requires an object definition
                if (spec) {
                    copyArray(spec.items || []);
                }

                that = Object.create({});
                that.removeItem = removeItem;
                that.removeAll = removeAll;
                that.addItem = addItem;
                that.containsItem = containsItem;
                that.forEach = forEach;
                that.isModified = isModified;
                Object.defineProperty(that, "isEmpty", {
                    get: isEmpty
                });
                Object.defineProperty(that, "itemCount", {
                    get: itemCount
                });
                Object.defineProperty(that, "subTotal", {
                    get: calculateSubTotal
                });
                Object.defineProperty(that, "vatTotal", {
                    get: calculateVatTotal
                });
                Object.defineProperty(that, "grossTotal", {
                    get: calculateGrossTotal
                });
                Object.defineProperty(that, "discountTotal", {
                    get: calculateDiscountTotal
                });
                Object.defineProperty(that, "items", {
                    // actually returns live array, angular binding does not work if we return a copy
                    get: function () {
                        return my.items;
                    },
                    enumerable: true
                });

                return that;
            };
        });
}());
