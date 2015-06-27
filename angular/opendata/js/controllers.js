'use strict';

/* Controllers */

var opendataControllers = angular.module('opendataControllers', []);

opendataControllers.controller('SearchListCtrl', ['$scope', 'Search',
  function($scope, Search) {
    
    $scope.search = function(query) {
        $scope.results = Search.get({type: query}, function(results) {
          for (var i = results.length - 1; i >= 0; i--) {
            results[i].index = i;
          };
        });
        $scope.show = true;

        
    }
  }]);


opendataControllers.controller('SearchHitCtrl', ['$scope', 'Search',
  function($scope, Search) {



  }]);
