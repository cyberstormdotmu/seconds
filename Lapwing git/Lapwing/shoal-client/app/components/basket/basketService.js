/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.basket').
        factory('shoalApp_basket_BasketService', function ($rootScope, $resource, ENV, shoalApp_classes_Basket, shoalCommon_products_ProductService) {
            var basketWebServiceUrl = ENV.webServiceUrl + "/basket",
                basketConstructor = shoalApp_classes_Basket,
                activeBasket,
                productService = shoalCommon_products_ProductService,
                vatRateSpec = {
                    code: '',
                    rate: 0.00
                },
                basketItemSpec = {
                    productCode: '',
                    unitPrice: 0.00,
                    quantity: 0,
                    currentVolume: 0,
                    stock: 0,
                    vatRate: vatRateSpec
                },

                notifyBasketChange = function () {
                    console.log('triggering basketUpdated notification');
                    $rootScope.$broadcast('basketUpdated', activeBasket);
                },
                enableBasketItemPriceUpdates = function (product, item) {
                    item.updatePrice = function () {
                        var pricing = product.calculatePricing(item.bindQuantity);
                        item.unitPrice = pricing.unitPrice;
                    };
                },
                buildBasketItem = function (product, order) {
                    var item = angular.copy(basketItemSpec);
                    item.productCode = product.code;
                    item.unitPrice = order.unitPrice;
                    item.quantity = order.quantity;
                    item.currentVolume = product.currentVolume;
                    item.stock = product.stock;
                    item.vatRate = angular.merge(vatRateSpec, product.vatRate);

                    enableBasketItemPriceUpdates(product, item);
                    return item;
                },
                enrichBasketItem = function (product, item) {
                    item.productName = product.name;
                    item.initialUnitPrice = product.originalPrice;
                    item.stock = product.stock;
                    item.vatRate = angular.merge(vatRateSpec, product.vatRate);
                    item.image = product.images[0];
                    enableBasketItemPriceUpdates(product, item);
                },
                Basket = $resource(basketWebServiceUrl, null,
                    {
                        'get': {
                            method: 'GET',
                            isArray: false
                        },
                        'update': {method: 'PUT'}
                    }),
                updateServer = function (responseHandler) {
                    var items = [],
                        payload;

                    activeBasket.forEach(function (item) {
                        items.push({
                            "productCode": item.productCode,
                            "quantity": item.quantity,
                            "unitPrice": item.unitPrice,
                            "currentVolume": item.currentVolume,
                            "stock": item.stock
                        });
                    });
                    payload = {
                        items: items
                    };
                    console.log('update items ' + JSON.stringify(payload));
                    Basket.update(payload, responseHandler);
                },
                parseBasket = function (response) {
                    var items = response.items,
                        enrichCount = 0;
                    if (items) {
                        console.log('fetched basket with + ' + items.length + ' items from <-------------------- server');
                        //console.log('parseBasket -> creating basketItems ' + JSON.stringify(items));
                        activeBasket = basketConstructor({
                            items: items
                        });
                        if (items.length === 0) {
                            notifyBasketChange();
                        }
                        activeBasket.forEach(function (item) {
                            productService.findByCode(item.productCode).then(function (product) {
                                //console.log("enriching product " + product.code);
                                enrichBasketItem(product, item);
                                enrichCount += 1;
                                if (enrichCount === items.length) {
                                    console.log('all items have been enriched');
                                    notifyBasketChange();
                                }
                            });
                        });
                    }
                },
                synchroniseBasket = function () {
                    console.log('basket is being synchronised...');
                    // if any changes in basket then write to server first, before fetching whole lot back again
                    // with updated prices / volumes
                    if (activeBasket && activeBasket.isModified()) {
                        updateServer(function (response) {
                            console.log('server update response is ' + JSON.stringify(response));
                            parseBasket(response);
                        });
                    } else {
                        console.log('no client basket changes detected');

                        Basket.get(function (response) {
                            parseBasket(response);
                        });
                    }
                },
                getBasket = function () {
                    console.log("Active Baskert Information-------");
                    console.log(JSON.stringify(activeBasket));
                    return activeBasket;
                },
                addItemToBasket = function (product, order) {
                    console.log('adding item ' + product.name + ' to basket');
                    activeBasket.addItem(buildBasketItem(product, order));
                    synchroniseBasket();
                },
                removeItemFromBasket = function (item) {
                    console.log('removing item ' + item.productName + ' from basket');
                    activeBasket.removeItem(item);
                    synchroniseBasket();
                },
                removeAllFromBasket = function () {
                    console.log('removing all items from basket');
                    activeBasket.removeAll();
                    synchroniseBasket();
                };


            return {
                getBasket: getBasket,
                addItemToBasket: addItemToBasket,
                removeItemFromBasket: removeItemFromBasket,
                removeAllFromBasket: removeAllFromBasket,
                synchroniseBasket: synchroniseBasket
            };
        });
}());
