acts.controller('CreateACtController', function CreateACtController($scope, $location) {
    $scope.userInfo = localStorage.getItem('userInfo');
    $scope.docInfo = localStorage.getItem('docInfo');

});