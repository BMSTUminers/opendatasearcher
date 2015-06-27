'use strict';

/* Controllers */

var opendataControllers = angular.module('opendataControllers', []);

opendataControllers.controller('SearchListCtrl', ['$scope', 'Search',
  function($scope, Search) {
    
    $scope.search = function(query) {
        $scope.results = Search.query({type: query}, function(results) {
          for (var i = results.length - 1; i >= 0; i--) {
            results[i].index = i;
          }
        });
        $scope.show = true;
        $scope.initialize = function(element, attrs) {
            initialize(attrs.postRender, attrs.id, element[0].id);
        }
    }
  }]);

opendataControllers.directive('postRender', [ '$timeout', function($timeout) {
    var def = {
        restrict : 'A',
        terminal : true,
        transclude : true,
        link : function(scope, element, attrs) {
            $timeout(scope.initialize(element, attrs), 1);  //Calling a scoped method
        }
    };
    return def;
}]);



opendataControllers.controller('SearchHitCtrl', ['$scope', 'Search',
  function($scope, Search) {

  }]);
