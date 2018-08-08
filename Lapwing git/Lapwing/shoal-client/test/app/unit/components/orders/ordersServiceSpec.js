(function () {
    'use strict';

    describe('shoalApp.orders -> shoalApp_orders_OrderService', function () {

        var subject,
            order,
            $httpBackend,
            webServiceUrl = "https://domain.com/ws",
            createOrderUrl = webServiceUrl + "/orders/";

        beforeEach(function () {

            subject = quickmock({
                providerName: 'shoalApp_orders_OrderService',
                moduleName: 'shoalApp.orders',
                mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
            });

            $httpBackend = subject.$mocks.$httpBackend;
            order = SHOAL.StaticData.ORDER;
        });

        it('should be loaded', function () {
            expect(subject.isLoaded()).toBe(true, 'should be loaded');
        });

        describe('when an order is submitted', function () {
            var payload;

            function whenServerReturnsStatus(status) {
                $httpBackend.whenPOST(createOrderUrl).respond(status, {});
                var success;
                subject.submitOrder(order).then(function () {
                    success = true;
                }, function () {
                    success = false;
                });
                $httpBackend.flush();
                return success;
            }

            afterEach(function () {
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
                $httpBackend.resetExpectations();
            });

            beforeEach(function () {
                payload = {
                    lines: [
                        {
                            productCode: 'HPELITE840',
                            quantity: 1,
                            unitPrice: 1030.00
                        },
                        {
                            productCode: 'HPSPECTRE',
                            quantity: 2,
                            unitPrice: 2010.00
                        }
                    ],
                    creditToBeApplied: 120,
                    invoiceAddress: {
                        'organisationName': 'Codera Ltd',
                        'departmentName': 'Development',
                        'buildingName': 'No 1 Leeds',
                        'streetAddress': '26 Whitehall Road',
                        'locality': '',
                        'postTown': 'Leeds',
                        'postcode': 'LS12 1BE'
                    },
                    deliveryAddress: {
                        'organisationName': 'Shoal Ltd',
                        'departmentName': '',
                        'buildingName': 'The Old Hall',
                        'streetAddress': 'Back Lane',
                        'locality': 'Bramham',
                        'postTown': 'Wetherby',
                        'postcode': 'LS23 6QR'
                    }
                };
            });

            it('should send the order data to the webservice', function () {
                $httpBackend.expectPOST(createOrderUrl, payload).respond(201, {});

                subject.submitOrder(order);

                $httpBackend.flush();
            });

            it('should resolve the promise when order is submitted', function () {
                var success = whenServerReturnsStatus(201);
                expect(success).toBe(true, ' submitOrder promise was not resolved after a successful server request');
            });

            it('should reject the promise when order fails', function () {
                var success = whenServerReturnsStatus(500);
                expect(success).toBe(false, ' submitOrder promise was not rejected after a server error');
            });

            it('should reject the promise when the server does not indicate a new order was created', function () {
                var success = whenServerReturnsStatus(200);
                expect(success).toBe(false, ' submitOrder promise was not rejected after a server ok');
            });
        });
    });
}());