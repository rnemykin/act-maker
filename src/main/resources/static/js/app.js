var acts = angular.module('acts', ['ngRoute', 'smart-table']);

acts.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'part/index',
        controller: 'IndexController'
    }).when('/create', {
        templateUrl:  'part/create-act',
        controller: 'CreateActController'
    }).when('/view/:actId', {
        templateUrl:  'part/view-act',
        controller: 'ViewActController'
    }).otherwise({
        redirectTo: '/'
    });
}]);

acts.factory('routeService', ['$location', function($location) {
    return {
        go: function(path) {
            $location.path(path);
        }
    }
}]);
