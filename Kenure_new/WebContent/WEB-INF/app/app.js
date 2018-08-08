/**
 * 
 *  @author tatvaSoft
 * 
 */

// defining Angular App module 
var kenureApp = angular.module('myApp',['ngCookies']);

/*//Angular TimeZone generic method to use at multiple pages
kenureApp.factro*/

//Angular Generic method for pagination in all pages

kenureApp.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;
          
          element.bind('change', function(){
             scope.$apply(function(){
                modelSetter(scope, element[0].files[0]);
             });
          });
       }
    };
 }]);

kenureApp.directive('fileReader', function() {
	return {
		scope: {
			fileReader:"="
		},
		link: function(scope, element) {
			$(element).on('change', function(changeEvent) {
				var files = changeEvent.target.files;
				if (files.length) {
					var r = new FileReader();
					r.onload = function(e) {
						var contents = e.target.result;
						scope.$apply(function () {
							scope.fileReader = contents;
						});
					};
					r.readAsText(files[0]);
					// split content based on new line
					var allTextLines = r.result.split(/\r\n|\n/);
					var headers = allTextLines[0].split(',');
					var lines = [];

					for ( var i = 0; i < allTextLines.length; i++) {
						// split content based on comma
						var data = allTextLines[i].split(',');
						if (data.length == headers.length) {
							var tarr = [];
							for ( var j = 0; j < headers.length; j++) {
								tarr.push(data[j]);
							}
							lines.push(tarr);
						}
					}
					$scope.data = lines;
				}
			});
		}
	};
});

kenureApp.directive('loading',['$http' ,function ($http){
	return {
		restrict: 'A',
		link: function (scope, elm, attrs)
		{
			scope.isLoading = function () {
				return $http.pendingRequests.length > 0;
			};

			scope.$watch(scope.isLoading, function (v){
				if(v){
					elm.show();
				}else{
					elm.hide();
				}
			});
		}
	};
}]);

/*kenureApp.factory("Pagination",function(){
	var results;
	var limit;
	var begin;
	return{
		changedValue: function(recordSize, list){
			limit = recordSize;
			begin = 0;
			// Change Max Limit
			var keys = Object.keys(list);
			var length = keys.length;

			// Max list calculations
			var totalPage = Math.ceil(length/limit);
			results = [limit,begin,totalPage];
			return results;
		},
		getNumber:  function(number){
			return new Array(number);
		},

		// Detecting page change event
		fetchRecord : function(number,recordSize){
			var currentRecordSize = recordSize; // current record size
			begin = parseInt((number-1)*currentRecordSize);
			limit = parseInt(begin) + parseInt(currentRecordSize);
			results = [limit,begin];
			return results;
		}	
	};
	
});*/

kenureApp.factory("RouteFileLine",function(){
	var routeFileLine = {
			"col9":'',
			"col10":'',
			"col11":'',
			"col12":'',
			"col13":'',
			"col14":'',
			"col15":'',
			"col16":'',
			"col17":'',
			"col18":''
	};
	var wholeRouteFileLine = {
			"col0":'',
			"col1":'',
			"col2":'',
			"col3":'',
			"col4":'',
			"col5":'',
			"col6":'',
			"col7":'',
			"col8":'',
			"col9":'',
			"col10":'',
			"col11":'',
			"col12":'',
			"col13":'',
			"col14":'',
			"col15":'',
			"col16":'',
			"col17":'',
			"col18":''
	};
	return {
        getFileLine: function () {
            return routeFileLine;
        },
        setFileLine: function(value) {
        	routeFileLine = value;
        },
        getWholeFileLine: function () {
            return wholeRouteFileLine;
        },
        setWholeFileLine: function(value) {
        	wholeRouteFileLine = value;
        }
	};
});

var url = localStorage.path;