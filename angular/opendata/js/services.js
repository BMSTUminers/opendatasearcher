'use strict';

/* Services */

var opendataServices = angular.module('opendataServices', ['ngResource']);

opendataServices.factory('Search', ['$resource',
  function($resource){
    return $resource('data/:type.json', {}, {
      query: {method:'GET', params: {type: 'table'}}
    });
  }]);
