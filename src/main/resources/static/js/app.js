var acts = angular.module('acts', ['ngRoute', 'smart-table']);

acts.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'part/index',
        controller: 'ActsController'
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

acts.controller('ActsController', ['$scope', '$window', 'routeService', function ActsController($scope, $window, routeService) {

    $scope.go = routeService.go;

    $scope.acts = JSON.parse(localStorage.getItem('acts')) || [];

    $scope.removeAct = function(id) {
        if(!$window.confirm('Удалить акт?')) {
            return;
        }

        var acts = JSON.parse(localStorage.getItem('acts'));
        acts = acts.filter(function (act) { return act.id != id; });
        localStorage.setItem('acts', JSON.stringify(acts));

        angular.element(document.querySelector('#act_' + id)).remove();
    };
    
    $scope.downloadAct = function (actId) {
        var acts = JSON.parse(localStorage.getItem('acts'));
        var act = acts.filter(function (act) { return act.id == actId; })[0];
        if(!act) {
            alert('Act with id ' + actId + ' does not exists!');
            return;
        }

        var buf = new ArrayBuffer(act.clob.length * 2);
        var bufView = new Uint16Array(buf);
        for (var i=0, strLen=act.clob.length; i<strLen; i++) {
            bufView[i] = act.clob.charCodeAt(i);
        }
        var data = new Blob([buf], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'});
        saveAs(data, act.fileName);
    };

}]);
