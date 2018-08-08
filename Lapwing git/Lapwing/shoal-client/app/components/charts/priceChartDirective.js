/*globals angular, Chartist, console */
(function () {
    'use strict';
    angular.module('shoalApp.charts')
        .directive('shoPriceChart', function () {
            return {
                restrict: 'E',
                templateUrl: '../app/components/charts/priceChartView.html',
                scope: {},
                bindToController: {
                    priceBands: '=',
                    currentVolume: '=',
                    orderQuantity: '=',
                    productStock: '='
                },
                controllerAs: 'vm',
                controller: function ($scope) {
                    var vm = this,
                        uniqClass = 'ct-chart' + String.fromCharCode(65 + Math.floor(Math.random() * 26)) + Date.now(),
                        derivedPriceBands = [],
                        chart,
                        priceCurveSeries = { name: 'priceCurve', data: [] },
                        currentVolumeSeries = { name: 'currentVolume', data: [] },
                        volumeIncludingOrderSeries = { name: 'volumeIncludingOrder', achievedTargetPrice: false, data: [] },
                        volumeExcludingOrderSeries = { name: 'volumeExcludingOrder', achievedTargetPrice: false, data: [] },
                        chartData = {
                            series: [
                                priceCurveSeries,
                                currentVolumeSeries,
                                volumeIncludingOrderSeries,
                                volumeExcludingOrderSeries
                            ]
                        },
                        createChartOptions = function () {
                            var i,
                                len,
                                priceBand,
                                volumeRanges = [],
                                priceRanges = [];

                            for (i = 0, len = vm.priceBands.length; i < len; i += 1) {
                                priceBand = vm.priceBands[i];
                                volumeRanges[i] = priceBand.minVolume;
                            }
                            for (i = 0, len = vm.priceBands.length; i < len; i += 1) {
                                priceBand = vm.priceBands[i];
                                priceRanges[i] = priceBand.buyerPrice;
                            }
                            return {
                                chartPadding: {
                                    top: 40,
                                    right: 30,
                                    bottom: 20,
                                    left: 30
                                },
                                showArea: true,
                                showLine: false,
                                showPoint: false,
                                fullWidth: true,
                                axisX: {
                                    type: Chartist.FixedScaleAxis,
                                    ticks: volumeRanges,
                                    onlyInteger: true,
                                    showLabel: true,
                                    showGrid: false
                                },
                                axisY: {
                                    type: Chartist.FixedScaleAxis,
                                    ticks: priceRanges,
                                    onlyInteger: true,
                                    showLabel: true,
                                    showGrid: false,
                                    labelInterpolationFnc: function (value) {
                                        return '£' + value;
                                    }
                                },
                                lineSmooth: Chartist.Interpolation.step(),
                                plugins: [
                                    Chartist.plugins.ctAxisTitle({
                                        axisX: {
                                            axisTitle: 'VOLUME',
                                            axisClass: 'ct-axis-title',
                                            offset: {
                                                x: 0,
                                                y: 50
                                            },
                                            textAnchor: 'middle'
                                        },
                                        axisY: {
                                            axisTitle: 'PRICE YOU PAY PER UNIT',
                                            axisClass: 'ct-axis-title',
                                            offset: {
                                                x: 0,
                                                y: 0
                                            },
                                            textAnchor: 'middle',
                                            flipTitle: false
                                        }
                                    })
                                ]
                            };
                        },
                        calculateDerivedPriceBands = function () {
                            var i, len, priceBand, maxVolume;
                            derivedPriceBands.length = 0;
                            for (i = 0, len = vm.priceBands.length; i < len; i += 1) {
                                priceBand = vm.priceBands[i];
                                maxVolume = priceBand.maxVolume || vm.productStock + vm.currentVolume;
                                derivedPriceBands.push({ minVolume: priceBand.minVolume, maxVolume: maxVolume, buyerPrice: priceBand.buyerPrice });
                            }
                            derivedPriceBands.push({ minVolume: vm.productStock + vm.currentVolume + 1, maxVolume: vm.productStock + vm.currentVolume + 1, buyerPrice: vm.priceBands[vm.priceBands.length - 1].buyerPrice * 0.95 });
                        },
                        updatePriceCurveSeries = function () {
                            var i, len, priceBand;
                            priceCurveSeries.data.length = 0;
                            for (i = 0, len = derivedPriceBands.length; i < len; i += 1) {
                                priceBand = derivedPriceBands[i];
                                priceCurveSeries.data.push({x: priceBand.minVolume, y: priceBand.buyerPrice});
                            }
                            priceBand = derivedPriceBands[derivedPriceBands.length - 1];
                            priceCurveSeries.data.push({x: priceBand.maxVolume, y: priceBand.buyerPrice});
                        },
                        updateCurrentVolumeSeries = function () {
                            var i, len, priceBand;
                            currentVolumeSeries.data.length = 0;
                            for (i = 0, len = derivedPriceBands.length; i < len; i += 1) {
                                priceBand = derivedPriceBands[i];
                                if (vm.currentVolume >= priceBand.minVolume) {
                                    currentVolumeSeries.data.push({x: priceBand.minVolume, y: priceBand.buyerPrice});
                                    if (priceBand.maxVolume && vm.currentVolume <= priceBand.maxVolume) {
                                        currentVolumeSeries.data.push({x: vm.currentVolume, y: priceBand.buyerPrice});
                                    }
                                }
                            }
                            priceBand = derivedPriceBands[derivedPriceBands.length - 1];
                            if (vm.currentVolume > priceBand.maxVolume) {
                                priceCurveSeries.data.push({x: priceBand.maxVolume, y: priceBand.buyerPrice});
                            }
                        },
                        updateOrderQuantitySeries = function () {
                            var i, len, priceBand, volumeIncludingOrder, orderQuantity,
                                haveStartingPoint = false;

                            orderQuantity = vm.orderQuantity || 0;
                            volumeIncludingOrder = vm.currentVolume + orderQuantity;
                            volumeIncludingOrderSeries.data.length = 0;
                            volumeExcludingOrderSeries.data.length = 0;
                            if (derivedPriceBands.length > 0) {
                                for (i = 0, len = derivedPriceBands.length; i < len; i += 1) {
                                    priceBand = derivedPriceBands[i];
                                    priceBand.minVolume = priceBand.minVolume === undefined ? 0 : priceBand.minVolume;
                                    priceBand.maxVolume = priceBand.maxVolume === undefined ? 0 : priceBand.maxVolume;
                                    if (vm.currentVolume >= priceBand.minVolume && vm.currentVolume <= priceBand.maxVolume) {
                                        volumeIncludingOrderSeries.data.push({x: vm.currentVolume, y: priceBand.buyerPrice});
                                        volumeExcludingOrderSeries.data.push({x: vm.currentVolume, y: priceBand.buyerPrice});
                                        haveStartingPoint = true;
                                    }
                                    if (haveStartingPoint && volumeIncludingOrder >= priceBand.minVolume) {
                                        volumeIncludingOrderSeries.data.push({x: priceBand.minVolume, y: priceBand.buyerPrice});
                                        if (volumeIncludingOrder <= priceBand.maxVolume) {
                                            volumeIncludingOrderSeries.data.push({x: volumeIncludingOrder, y: priceBand.buyerPrice});
                                            volumeExcludingOrderSeries.data.push({x: vm.currentVolume, y: priceBand.buyerPrice});
                                        }
                                    }
                                }
                                priceBand = derivedPriceBands[derivedPriceBands.length - 1];
                                if (volumeIncludingOrder > priceBand.maxVolume) {
                                    volumeIncludingOrderSeries.data.push({x: priceBand.maxVolume, y: priceBand.buyerPrice});
                                    volumeExcludingOrderSeries.data.push({x: vm.currentVolume, y: priceBand.buyerPrice});
                                }
                                volumeIncludingOrderSeries.achievedTargetPrice = volumeIncludingOrder >= priceBand.minVolume;
                            }
                        },
                        drawPriceYouPay = function (data) {
                            if (data.type === 'area' && data.series.name === 'volumeIncludingOrder') {
                                var lineXPosition,
                                    textXOffset,
                                    textAnchor,
                                    textLine1,
                                    textLine2;

                                lineXPosition = data.path.pathElements[data.path.pathElements.length - 1].x;
                                if (lineXPosition > data.chartRect.x1 + (data.chartRect.x2 - data.chartRect.x1) * 2 / 3) {
                                    textXOffset = -10;
                                    textAnchor = 'end';
                                } else {
                                    textXOffset = 10;
                                    textAnchor = 'start';
                                }

                                data.group.elem('line', {
                                    x1: lineXPosition,
                                    x2: lineXPosition,
                                    y1: data.chartRect.y1,
                                    y2: data.chartRect.y2 - 40
                                }, 'ct-line');

                                if (data.series.achievedTargetPrice) {
                                    textLine1 = 'TARGET PRICE ACHIEVED';
                                    textLine2 = 'YOU PAY £' + data.series.data[data.series.data.length - 1].y;
                                } else {
                                    textLine1 = 'PRICE YOU PAY';
                                    textLine2 = '£' + data.series.data[data.series.data.length - 1].y;
                                }

                                data.group.elem('text', {
                                    x: lineXPosition + textXOffset,
                                    y: data.chartRect.y2 - 30,
                                    'text-anchor': textAnchor
                                }, 'ct-label').text(textLine1);
                                data.group.elem('text', {
                                    x: lineXPosition + textXOffset,
                                    y: data.chartRect.y2 - 15,
                                    'text-anchor': textAnchor
                                }, 'ct-label').text(textLine2);
                            }
                        },
                        drawUnitsSoldPay = function (data) {
                            if (data.type === 'area' && data.series.name === 'volumeExcludingOrder' && data.series.data[data.series.data.length - 1].x > 0) {
                                var lineXPosition,
                                    textXOffset,
                                    textAnchor,
                                    textLine1,
                                    textLine2;

                                lineXPosition = data.path.pathElements[data.path.pathElements.length - 1].x;
                                textXOffset = -10;
                                textAnchor = 'end';

                                data.group.elem('line', {
                                    x1: lineXPosition,
                                    x2: lineXPosition,
                                    y1: data.chartRect.y1,
                                    y2: data.chartRect.y2 - 40
                                }, 'ct-line');

                                textLine1 = data.series.data[data.series.data.length - 1].x + ' UNITS';
                                textLine2 = 'ALREADY SOLD';

                                data.group.elem('text', {
                                    x: lineXPosition + textXOffset,
                                    y: data.chartRect.y2 - 30,
                                    'text-anchor': textAnchor
                                }, 'ct-green-label').text(textLine1);
                                data.group.elem('text', {
                                    x: lineXPosition + textXOffset,
                                    y: data.chartRect.y2 - 15,
                                    'text-anchor': textAnchor
                                }, 'ct-green-label').text(textLine2);
                            }
                        },
                        createChart = function () {
                            updatePriceCurveSeries();
                            updateCurrentVolumeSeries();
                            updateOrderQuantitySeries();
                            ////chart = Chartist.Line('.ct-chart', chartData, createChartOptions()).on('draw', drawPriceYouPay);
                            chart = Chartist.Line('.' + uniqClass, chartData, createChartOptions()).on('draw', drawPriceYouPay).on('draw', drawUnitsSoldPay);
                            ////chart = Chartist.Line('.' + uniqClass, chartData, createChartOptions()).on('draw', drawUnitsSoldPay);
                        },
                        updateChart = function () {
                            updateOrderQuantitySeries();
                            ////Commented due to function not found.
                            chart.update(chartData);
                        };

                    vm.uniqClass = uniqClass;

                    $scope.$watch(function () {
                        return vm.priceBands;
                    }, function () {
                        if (vm.priceBands) {
                            setTimeout(function () {
                                calculateDerivedPriceBands();
                                createChart();
                            });
                        }
                    });
                    $scope.$watch(function () {
                        return vm.orderQuantity;
                    }, function () {
                        updateChart();
                    });
                }
            };
        });
}());