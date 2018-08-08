(function () {
    'use strict';
    describe('shoalApp.classes module -> shoalApp_classes_Basket', function () {
        var basketClass,
            subject,
            item1 = {
                productCode: 'HPELITE840',
                quantity: 1,
                unitPrice: 930.00,
                initialUnitPrice: 1030.00,
                vatRate : {
                    code: 'STANDARD',
                    rate: 20.00
                }

            },
            item2 = {
                productCode: 'HPSPECTRE',
                quantity: 2,
                unitPrice: 2010.00,
                initialUnitPrice: 2030.00,
                vatRate : {
                    code: 'REDUCED',
                    rate: 5.00
                }
            };

        beforeEach(function () {
            basketClass = quickmock({
                providerName: 'shoalApp_classes_Basket',
                moduleName: 'shoalApp.classes',
                mockModules: []
            });
            subject = basketClass();
        });

        it('should be initialised', function () {
            //spec body

            expect(subject).toBeDefined();
        });

        it('should be empty at first', function () {
            expect(subject.isEmpty).toBe(true);
        });

        it('should not be empty when items in it', function () {
            subject = basketClass(SHOAL.StaticData.buildDefaultBasketItems());

            expect(subject.isEmpty).toBe(false, 'basket should not be empty when it has items in it');
        });

        it('should add basket items', function () {

            subject.addItem(item1);

            expect(subject.itemCount).toBe(1);

            subject.addItem(item2);

            expect(subject.itemCount).toBe(2);
        });

        it('should remove basket items', function () {

            subject.addItem(item1);
            subject.addItem(item2);

            subject.removeItem(item1);

            expect(subject.itemCount).toBe(1);

            subject.removeItem(item2);

            expect(subject.itemCount).toBe(0);
        });

        it('should fail to add the same basket item more than once', function () {
            try {
                subject.addItem(item1);
                subject.addItem(item1);
                expect(subject.itemCount).toBe(1, 'the basket should not be able to hold more than one discrete item of the same type');
            } catch (ignore) {
                // if this happens then the test passes
            }
        });

        it('should calculate subtotal of all items', function () {
            subject = basketClass();

            expect(subject.subTotal).toBe(0.00, ' should be zero subtotal when no items in basket');

            subject.addItem(item1);

            expect(subject.subTotal).toBe(930.00, 'incorrect subtotal for one item');

            subject.addItem(item2);

            expect(subject.subTotal).toBe(4950.00, 'incorrect subtotal for two items');

            subject.removeItem(item1);

            expect(subject.subTotal).toBe(4020.00, 'incorrect subtotal for remaining item');

            subject.removeItem(item2);

            expect(subject.subTotal).toBe(0.00, ' should be zero subtotal when no items in basket');
        });

        it('should calculate vat of all items', function () {
            subject = basketClass();

            expect(subject.vatTotal).toBe(0.00, ' should be zero vatTotal when no items in basket');

            subject.addItem(item1);

            expect(subject.vatTotal).toBe(186.00, 'incorrect vatTotal for one item');

            subject.addItem(item2);

            expect(subject.vatTotal).toBe(387.00, 'incorrect vatTotal for two items');

            subject.removeItem(item1);

            expect(subject.vatTotal).toBe(201.00, 'incorrect vatTotal for remaining item');

            subject.removeItem(item2);

            expect(subject.vatTotal).toBe(0.00, ' should be zero vatTotal when no items in basket');
        });

        it('should calculate discount of all items', function () {
            subject = basketClass();

            expect(subject.discountTotal).toBe(0.00, ' should be zero discount when no items in basket');

            subject.addItem(item1);

            expect(subject.discountTotal).toBe(100.00, ' incorrect discount for one item');

            subject.addItem(item2);

            expect(subject.discountTotal).toBe(140.00, ' incorrect discount for two items');

            subject.removeItem(item1);

            expect(subject.discountTotal).toBe(40.00, ' incorrect discount for remaining item');

            subject.removeItem(item2);

            expect(subject.discountTotal).toBe(0.00, ' should be zero discount when no items in basket');
        });

        it('should calculate gross total of all items', function () {
            subject = basketClass();

            expect(subject.grossTotal).toBe(0.00, ' should be zero discount when no items in basket');

            subject.addItem(item1);

            expect(subject.grossTotal).toBe(1116.00, ' incorrect discount for one item');

            subject.addItem(item2);

            expect(subject.grossTotal).toBe(5337.00, ' incorrect discount for two items');

            subject.removeItem(item1);

            expect(subject.grossTotal).toBe(4221.00, ' incorrect discount for remaining item');

            subject.removeItem(item2);

            expect(subject.grossTotal).toBe(0.00, ' should be zero discount when no items in basket');
        });

        it('should provide basket items in a suitable array for display use', function () {
            var items = [item1, item2];

            subject = basketClass({
                items: items
            });

            expect(subject.items[item1.productCode].productCode).toBe(item1.productCode, ' array item was not in expected place');
            expect(subject.items[item2.productCode].productCode).toBe(item2.productCode, ' array item was not in expected place');
        });

        it('should report a contained item', function () {
            expect(subject.containsItem(item1)).toBe(false, 'incorrectly reporting containsItem');
            expect(subject.containsItem(item2)).toBe(false, 'incorrectly reporting containsItem');

            subject.addItem(item1);

            expect(subject.containsItem(item1)).toBe(true, 'incorrectly reporting containsItem');
            expect(subject.containsItem(item2)).toBe(false, 'incorrectly reporting containsItem');

            subject.addItem(item2);
            expect(subject.containsItem(item1)).toBe(true, 'incorrectly reporting containsItem');
            expect(subject.containsItem(item2)).toBe(true, 'incorrectly reporting containsItem');
        });
    });
}());
