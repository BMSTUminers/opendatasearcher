'use strict';

/* Controllers */

var opendataControllers = angular.module('opendataControllers', []);

opendataControllers.controller('SearchListCtrl', ['$scope', '$location', 'Search',
  function($scope, $location, Search) { 
    $scope.search = function(query) {
        $scope.results = Search.query({type: query}, function(results) {
          for (var i = results.length - 1; i >= 0; i--) {
            results[i].index = i;
          }
        });
        $scope.show = true;
        
    };

    $scope.initialize = function(element, attrs) {
            initialize(attrs.postRender, attrs.id, element[0].id);
        };

    var query = $location.search().q;

    if (query) {
      $scope.search(query);
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



opendataControllers.controller('SearchHitCtrl', ['$scope', '$location', 'Search',
  function($scope, $location, Search) {
        $scope.changeUrl = function(query) {
      $location.path('search');
      $location.search('q', query);
      
    };

  }]);

opendataControllers.controller('SomeHitCtrl', ['$scope',
  function($scope) {
        
  }]);
