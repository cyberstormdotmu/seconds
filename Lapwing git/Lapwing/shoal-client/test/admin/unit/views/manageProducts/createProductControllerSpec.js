(function () {
    'use strict';
    describe('shoalAdmin.views.manageProducts module -> CreateProductController', function () {
        var CreateProductController,
            productForm,
            vm;

        beforeEach(function () {
            CreateProductController = quickmock({
                providerName: 'shoalAdmin.views.manageProducts.CreateProductController',
                providerAs: 'vm',
                moduleName: 'shoalAdmin.views.manageProducts',
                mockModules: ['shoalAdmin.ShoalAdminMocks']
            });
            productForm = CreateProductController.$mocks.productForm;
            vm = CreateProductController.$scope.vm;
        });

        it("should be loaded", function () {
            //spec body

            expect(CreateProductController.isLoaded()).toBe(true, " should be loaded");
        });

        describe("when the controller is loaded", function () {

            it("should store the product form", function () {
                expect(vm.productForm).toBeDefined();
                expect(vm.productForm).toEqual(productForm);
            });

            it("should create a start and end date picker", function () {
                expect(vm.startDatePicker).toBeDefined();
                expect(vm.endDatePicker).toBeDefined();
            });

            it("should have the date pickers closed to start with", function () {
                expect(vm.startDatePicker.opened).toBe(false);
                expect(vm.endDatePicker.opened).toBe(false);
            });

            describe("when the start date picker action is triggered", function () {
                beforeEach(function () {
                    vm.openStartDatePicker();
                });

                it("should open the start date calendar", function () {
                    expect(vm.startDatePicker.opened).toBe(true);
                    expect(vm.endDatePicker.opened).toBe(false);
                });

                describe("when the start date picker action is triggered again", function () {
                    beforeEach(function () {
                        vm.openStartDatePicker();
                    });

                    it("should close the start date calendar", function () {
                        expect(vm.startDatePicker.opened).toBe(false);
                        expect(vm.endDatePicker.opened).toBe(false);
                    });
                });
            });

            describe("when the end date picker action is triggered", function () {
                beforeEach(function () {
                    vm.openEndDatePicker();
                });

                it("should open the end date calendar", function () {
                    expect(vm.startDatePicker.opened).toBe(false);
                    expect(vm.endDatePicker.opened).toBe(true);
                });

                describe("when the end date picker action is triggered again", function () {
                    beforeEach(function () {
                        vm.openEndDatePicker();
                    });

                    it("should close the end date calendar", function () {
                        expect(vm.startDatePicker.opened).toBe(false);
                        expect(vm.endDatePicker.opened).toBe(false);
                    });
                });
            });
        });
    });
}());