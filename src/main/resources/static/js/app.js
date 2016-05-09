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

acts.factory('routeService', ['$location', function($location) {
    return {
        go: function(path) {
            $location.path(path);
        }
    }
}]);

acts.controller('ActsController', ['$scope', 'routeService', function ActsController($scope, routeService) {

    $scope.go = routeService.go;

    $scope.acts = JSON.parse(localStorage.getItem('acts')) || [];

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

    $scope.removeAct = function(id) {
        var acts = JSON.parse(localStorage.getItem('acts'));
        acts = acts.filter(function (act) { return act.id != id; });
        localStorage.setItem('acts', JSON.stringify(acts));

        angular.element(document.querySelector('#act_' + id)).remove();
    }

}]);
