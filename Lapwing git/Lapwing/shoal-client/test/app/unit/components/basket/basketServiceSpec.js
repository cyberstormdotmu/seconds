/*global angular, quickmock,  describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, SHOAL */
(function () {
    'use strict';
    describe('shoalApp.basket module -> shoalApp_basket_BasketService', function () {
        var subject,
            productService,
            $httpBackend,
            basketUrl,
            $rootScope;

        beforeEach(function () {
            var ENV;
            subject = quickmock({
                providerName: 'shoalApp_basket_BasketService',
                moduleName: 'shoalApp.basket',
                mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
            });
            $httpBackend = subject.$mocks.$httpBackend;
            ENV = subject.$mocks.ENV;
            basketUrl = ENV.webServiceUrl + "/basket";
            productService = subject.$mocks.shoalCommon_products_ProductService;
            $rootScope = subject.$rootScope;
        });

        it('should be loaded', function () {
            //spec body

            expect(subject.isLoaded()).toBe(true, 'should be loaded');
        });

        it('should initially have an undefined basket', function () {
            expect(subject.getBasket()).toBeUndefined();
        });

        describe('when synchronising the basket', function () {

            var synchroniseBasket = function () {
                subject.synchroniseBasket();
                $httpBackend.flush();
            };

            beforeEach(function () {
                $httpBackend.whenGET(basketUrl).respond(200, SHOAL.StaticData.buildDefaultBasketItems());
            });

            afterEach(function () {
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.resetExpectations();
            });

            it('should call the server to get the basic basket items', function () {
                $httpBackend.expectGET(basketUrl);

                synchroniseBasket();
            });

            describe('if the server basket is empty', function () {

                beforeEach(function () {
                    $httpBackend.expectGET(basketUrl).respond(200, {
                        items: []
                    });

                    synchroniseBasket();
                });

                it('should empty the current basket', function () {
                    expect(subject.getBasket().itemCount).toBe(0);
                });

                it('should notify the outside world that the basket has changed', function () {
                    expect($rootScope.$broadcast).toHaveBeenCalledWith('basketUpdated',
                        jasmine.objectContaining(subject.getBasket()));
                });
            });

            describe('after the basket has been synchronised', function () {
                var product1,
                    priceBand;

                beforeEach(function () {
                    product1 = SHOAL.StaticData.PRODUCT_1;
                    priceBand = SHOAL.StaticData.PRICE_BAND_1;

                    synchroniseBasket();
                    $rootScope.$digest();
                });

                describe('when the basket is populated', function () {

                    it('should be loaded into the active basket', function () {

                        expect(subject.getBasket().itemCount).toBe(2);
                    });

                    it('should fetch the product information for each basket item', function () {
                        expect(productService.findByCode.calls.count()).toBe(2);
                    });

                    it('should enrich the basket item with the product info', function () {

                        expect(subject.getBasket().items[product1.code]).toEqual(jasmine.objectContaining({
                            productName: product1.name,
                            initialUnitPrice: priceBand.buyerPrice,
                            image: SHOAL.StaticData.IMAGE_1
                        }));
                    });

                    it('should be possible to calculate the band price of a basket item', function () {
                        var basketItem = subject.getBasket().items[product1.code];
                        basketItem.updatePrice();
                        expect(basketItem.unitPrice).toEqual(1030.00);
                    });
                });

                describe('when a new item was added to the basket', function () {
                    var basketItem,
                        product3,
                        updatedCurrentVolume = 101,
                        buildClientSideBasketItems,
                        buildServerSideBasketItems;


                    beforeEach(function () {
                        buildClientSideBasketItems = function () {
                            return {
                                items: [
                                    SHOAL.StaticData.EXISTING_BASKET_ITEM_1,
                                    SHOAL.StaticData.EXISTING_BASKET_ITEM_2,
                                    SHOAL.StaticData.NEW_BASKET_ITEM_3
                                ]
                            };
                        };
                        buildServerSideBasketItems = function () {
                            var serverUpdatedBasketItem = angular.copy(SHOAL.StaticData.NEW_BASKET_ITEM_3);
                            serverUpdatedBasketItem.currentVolume = updatedCurrentVolume;

                            return {
                                items: [
                                    SHOAL.StaticData.EXISTING_BASKET_ITEM_1,
                                    SHOAL.StaticData.EXISTING_BASKET_ITEM_2,
                                    serverUpdatedBasketItem
                                ]
                            };
                        };
                        $httpBackend.whenPUT(basketUrl).respond(200, buildServerSideBasketItems());

                        basketItem = SHOAL.StaticData.NEW_BASKET_ITEM_3;
                        product3 = SHOAL.StaticData.PRODUCT_3;
                        subject.addItemToBasket(SHOAL.StaticData.PRODUCT_3, {
                            quantity: basketItem.quantity,
                            unitPrice: basketItem.unitPrice
                        });
                    });

                    describe('before the server is updated', function () {

                        it('should update the basket item to the managed basket', function () {
                            expect(subject.getBasket().containsItem(basketItem)).toBeTruthy(' item was not added to the managed basket');
                        });

                        it('should enrich the basket with product information already held', function () {

                            var basketItems = subject.getBasket().items;

                            expect(basketItems[SHOAL.StaticData.PRODUCT_3.code]).toEqual(jasmine.objectContaining({
                                currentVolume: SHOAL.StaticData.PRODUCT_3.currentVolume
                            }));
                        });

                        it('should synchronise the basket with the server', function () {
                            $httpBackend.expectPUT(basketUrl).respond(200);
                        });

                        it('should send the basket details to the server', function () {

                            $httpBackend.expectPUT(basketUrl, buildClientSideBasketItems()).respond(200);
                        });
                    });

                    describe('after the server basket is updated', function () {

                        beforeEach(function () {
                            $httpBackend.flush();
                        });

                        it('should use the server response to update the basket', function () {
                            var basketItems = subject.getBasket().items;

                            expect(basketItems[SHOAL.StaticData.PRODUCT_3.code]).toEqual(jasmine.objectContaining({
                                productName: product3.name,
                                currentVolume: updatedCurrentVolume
                            }));
                        });

                        it('should notify the outside world that the basket has changed', function () {
                            expect($rootScope.$broadcast).toHaveBeenCalledWith('basketUpdated',
                                jasmine.objectContaining(subject.getBasket()));
                        });
                    });
                });

                describe('when the basket is synchronised', function () {
                    beforeEach(function () {
                        subject.synchroniseBasket();
                        $rootScope.$digest();
                        $httpBackend.flush();
                    });

                    describe('when the basket is modified', function () {

                        afterEach(function () {
                            subject.synchroniseBasket();
                            $rootScope.$digest();
                        });

                        describe('and the change is valid', function () {
                            beforeEach(function () {
                                var basketItem = subject.getBasket().items[product1.code];
                                basketItem.bindQuantity = 10;
                            });

                            it('should send the modified basket to the server', function () {
                                $httpBackend.expectPUT(basketUrl).respond(200);
                            });
                        });

                        describe('and the change is invalid', function () {
                            beforeEach(function () {
                                var basketItem = subject.getBasket().items[product1.code];
                                // invalid value from form
                                basketItem.bindQuantity = undefined;
                            });

                            it('should NOT send the modified basket to the server', function () {
                                $httpBackend.verifyNoOutstandingExpectation();
                                $httpBackend.verifyNoOutstandingRequest();
                            });
                        });

                    });
                });
            });

        });
    });
}());