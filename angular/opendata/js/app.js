'use strict';

/* App Module */

var opendataApp = angular.module('opendataApp', [
   'ngRoute',
  'opendataControllers',
  'opendataServices'
]);

opendataApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'partials/none.html',
        controller: 'SomeHitCtrl'
      }).
      when('/search/', {
        templateUrl: 'partials/results.html',
        controller: 'SearchListCtrl'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);