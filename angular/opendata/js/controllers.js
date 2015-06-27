'use strict';

/* Controllers */

var opendataControllers = angular.module('opendataControllers', []);

opendataControllers.controller('SearchListCtrl', ['$scope', 'Search',
  function($scope, Search) {
    
    $scope.search = function(query) {
        $scope.result = Search.get({type: query}, function(result) {
          if (result.type === 'KML') {
            initialize(result.kmlRef);
          }
        });
        $scope.show = true;

        
    }
  }]);


opendataControllers.controller('SearchHitCtrl', ['$scope', 'Search',
  function($scope, Search) {



  }]);
