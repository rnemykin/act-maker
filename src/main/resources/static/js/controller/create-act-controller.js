acts.controller('CreateACtController', function CreateACtController($scope, $location) {
    $scope.user = localStorage.getItem('user');
    $scope.onlyNumbers = /^\d+$/;
});