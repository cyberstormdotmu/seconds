/*global SHOAL, angular */
(function () {
    // all entries in CAPITALS are immutable and cannot be changed anywhere
    // you will need to copy these items in tests to be able to modify them
    // a useful feature of this setup is that you can lock down certain objects in your tests and mutify just the
    // objects that you expect to change, enabling tracking of side effects as Javascript will give you an error.
    'use strict';
    var productCode1 = "HPELITE840",
        productCode2 = "HPSPECTRE",
        productCode3 = "HPPAVILION15",
        bandA1 = {"minVolume": 0, "maxVolume": 40, "buyerPrice": 1030.00},
        bandA2 = {"minVolume": 41, "maxVolume": 100, "buyerPrice": 930.00},
        bandA3 = {"minVolume": 101, "maxVolume": 200, "buyerPrice": 830.00},
        bandA4 = {"minVolume": 201, "maxVolume": null, "buyerPrice": 730.00},
        bandB1 = {"minVolume": 0, "maxVolume": 40, "buyerPrice": 2030.00},
        bandB2 = {"minVolume": 41, "maxVolume": 100, "buyerPrice": 1930.00},
        bandB3 = {"minVolume": 101, "maxVolume": 200, "buyerPrice": 830.00},
        bandB4 = {"minVolume": 201, "maxVolume": null, "buyerPrice": 1730.00},
        specs1 = [{name: "Processor", description: "Intel Core i5 processor"}, {
            "name": "Memory",
            "description": "4GB DDR3L-16600 SDRAM (1 x 4GB)"
        }],
        categories1 = ["Products", "Laptops", "Power User"],
        image1 = {
            "order": 1,
            "url": "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889408_390x286.jpg",
            "description": "HP laptop"
        },
        image2 = {
            "order": 1,
            "url": "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/P5P13EA-ABU_1_1560x1144.jpg",
            "description": "HP Spectre x360 laptop"
        },
        image3 = {
            "order": 1,
            "url": "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c04241981_390x286.jpg",
            "description": "HP Pavilion 15-p264na laptop"
        },
        standardVat = {
            "code": "STANDARD",
            "rate": 20.00
        },
        product1 = {
            "code": productCode1,
            "name": "HP EliteBook 840 G2 Laptop",
            "offerEndDate": "2016-03-17T23:59:59.000Z",
            "description": "The HP EliteBook 840 thin and light notebook allows...",
            "specifications": specs1,
            "categories": categories1,
            "currentVolume": 36,
            "vatRate": standardVat,
            "priceBands": [bandA1, bandA2, bandA3, bandA4],
            "images": [image1, image2]
        },
        product2 = {
            "code": productCode2,
            "name": "HP Spectre 13-4109na x360 Convertible Laptop",
            "offerEndDate": "2016-04-15T22:59:59.000Z",
            "currentVolume": 0,
            "vatRate": standardVat,
            "specifications": specs1,
            "categories": categories1,
            "priceBands": [bandB1, bandB2, bandB3, bandB4],
            "images": [image2]
        },
        product3 = {
            "code": productCode3,
            "name": "HP Pavilion 15-p264na Laptop",
            "offerEndDate": "2016-03-17T23:59:59.000Z",
            "description": "The HP Pavilion Notebook provides all of the benefits of a desktop in a sleek...",
            "specifications": specs1,
            "categories": categories1,
            "currentVolume": 99,
            "vatRate": standardVat,
            "priceBands": [bandA1, bandA2, bandA3, bandA4],
            "images": [image3]
        },
        basketItem1 = {
            productCode: productCode1,
            quantity: 1,
            unitPrice: 1030.00,
            currentVolume: 36
        },
        basketItem2 = {
            productCode: productCode2,
            quantity: 2,
            unitPrice: 2010.00,
            currentVolume: 0
        },
        basketItem3 = {
            productCode: productCode3,
            quantity: 5,
            unitPrice: 930.00,
            currentVolume: 99
        },
        dummyBasket = {
            items: {
                "HPELITE840": basketItem1
            },
            forEach: function () {
                return;
            },
            containsItem: function () {
                return true;
            }
        },
        address1 = {
            'organisationName': 'Codera Ltd',
            'departmentName': 'Development',
            'buildingName': 'No 1 Leeds',
            'streetAddress': '26 Whitehall Road',
            'locality': '',
            'postTown': 'Leeds',
            'postcode': 'LS12 1BE'
        },
        address2 = {
            'organisationName': 'Shoal Ltd',
            'departmentName': '',
            'buildingName': 'The Old Hall',
            'streetAddress': 'Back Lane',
            'locality': 'Bramham',
            'postTown': 'Wetherby',
            'postcode': 'LS23 6QR'
        },
        dummyOrder = {
            lines: [
                {
                    productCode: basketItem1.productCode,
                    quantity: basketItem1.quantity,
                    unitPrice: basketItem1.unitPrice
                },
                {
                    productCode: basketItem2.productCode,
                    quantity: basketItem2.quantity,
                    unitPrice: basketItem2.unitPrice
                }
            ],
            creditToBeApplied: 120,
            invoiceAddress: address1,
            deliveryAddress: address2
        };

    SHOAL.StaticData = {
        PRODUCT_1: Object.freeze(product1),
        PRODUCT_2: Object.freeze(product2),
        PRODUCT_3: Object.freeze(product3),
        EXISTING_BASKET_ITEM_1: Object.freeze(basketItem1),
        EXISTING_BASKET_ITEM_2: Object.freeze(basketItem2),
        NEW_BASKET_ITEM_3: Object.freeze(basketItem3),
        DUMMY_BASKET: dummyBasket,
        ORDER: Object.freeze(dummyOrder),
        PRICE_BAND_1: Object.freeze(bandA1),
        PRICE_BAND_2: Object.freeze(bandA2),
        PRICE_BAND_3: Object.freeze(bandA3),
        PRICE_BAND_4: Object.freeze(bandA4),
        IMAGE_1: Object.freeze(image1),
        IMAGE_2: Object.freeze(image2),
        IMAGE_3: Object.freeze(image3),
        buildDefaultCategory: function () {
            return {
                name: 'Power User',
                parents: ['Laptops', 'Products']
            };
        },
        buildDefaultProductListing: function () {
            return [
                angular.copy(product1),
                angular.copy(product2)
            ];
        },
        buildDefaultProduct: function () {
            return product1;
        },
        buildAllProducts: function () {
            var products = {};
            products[productCode1] = angular.copy(product1);
            products[productCode2] = angular.copy(product2);
            products[productCode3] = angular.copy(product3);
            return products;
        },
        buildDefaultBasketItems: function () {
            return {
                items: [
                    angular.copy(basketItem1),
                    angular.copy(basketItem2)
                ]
            };
        },
        buildBuyerProfile: function () {
            return {
                organisation: {
                    name: 'HP Inc',
                    registrationNumber: '07496791'
                },
                contact: {
                    title: 'Mr',
                    firstName: 'Roger',
                    surname: 'Watkins',
                    phoneNumber: 'rogerwatkins@hotmail.co.uk'
                },
                deliveryAddress: {
                    departmentName: 'IT',
                    buildingName: 'Floor',
                    streetAddress: 'Filton Road Stoke Gifford, Pt. Ground',
                    locality: '',
                    postTown: 'BRISTOL',
                    postcode: 'BS24 8QZ'
                },
                bankAccount: {
                    accountName: 'MR R E WATKINS',
                    sortCode: '88-88-88',
                    accountNumber: '88888888',
                    bankName: 'Barclays',
                    buildingName: '1',
                    streetAddress: 'Churchill place',
                    locality: 'Canary Wharf',
                    postTown: 'LONDON   ',
                    postcode: 'E14 5HP'
                }
            };
        },
        buildASpringFormattedServerException: function () {
            return {
                "error": "Internal Server Error",
                "exception": "org.springframework.dao.InvalidDataAccessResourceUsageException",
                "message": "some error message"
            };
        }
    };
}());
