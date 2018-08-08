(function () {
    'use strict';
    describe('shoalApp.classes module -> shoalApp_classes_Order', function () {
        var orderClass, items, basketClass, basket, subject;

        beforeEach(function () {
            orderClass = quickmock({
                providerName: 'shoalApp_classes_Order',
                moduleName: 'shoalApp.classes',
                mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
            });
            subject = orderClass();
            basketClass = orderClass.$mocks.shoalApp_classes_Basket;
            items = SHOAL.StaticData.buildDefaultBasketItems();
            basket = basketClass(items);
        });

        it('should be initialised', function () {
            expect(subject).toBeDefined();
        });

        describe('when the basket is set', function () {
            var orderLine = [
                    {
                        productCode: "HPELITE840",
                        quantity: 1,
                        unitPrice: 1030.00
                    },
                    {
                        productCode: "HPSPECTRE",
                        quantity: 2,
                        unitPrice: 2010.00
                    }
                ];

            beforeEach(function () {
                subject.basket = basket;
            });

            it('should have the expected order lines', function () {
                console.log(JSON.stringify(subject.basket));
                console.log(JSON.stringify(subject.lines));
                expect(JSON.stringify(subject.lines)).toEqual(JSON.stringify(orderLine), 'order did not have the expected order lines');
            });
        });
    });
}());