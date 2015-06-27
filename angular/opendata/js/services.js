'use strict';

/* Services */

var opendataServices = angular.module('opendataServices', ['ngResource']);

opendataServices.factory('Search', ['$resource',
  function($resource){
    return $resource('search/query?query=:type', {}, {
        //query: {method:'GET', isArray:true}
    });
  }]);
