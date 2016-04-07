var acts = angular.module('acts', ['smart-table']);


acts.controller('ActsController', function ActsController($scope) {
    $scope.status = 'booted ';

    $scope.testClick =function () {
        alert(1);
    };

    $scope.acts = localStorage.getItem('acts') || [];
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
