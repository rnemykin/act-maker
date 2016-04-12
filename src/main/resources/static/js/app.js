var acts = angular.module('acts', ['ngRoute', 'smart-table']);

acts.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'part/index',
        controller: 'ActsController'
    }).when('/create', {
        templateUrl:  'part/create-act',
        controller: 'CreateACtController'
    }).otherwise({
        redirectTo: '/'
    });
}]);


acts.controller('ActsController', function ActsController($scope, $location) {

    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.acts = localStorage.getItem('acts') || [];

    //todo test
    $scope.acts.push({
        id: 1,
        actNumber: 1,
        createDate: new Date()
    });
    $scope.acts.push({
        id: 2,
        actNumber: 2,
        createDate: new Date()
    });

});
