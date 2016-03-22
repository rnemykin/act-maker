var acts = angular.module('acts', []);

acts.controller('ActsController', function ActsController($scope) {
    $scope.status = 'booted ';

    $scope.testClick =function () {
        alert(1);
    }
});
