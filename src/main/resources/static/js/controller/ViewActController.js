acts.controller('ViewActController', ['$scope', '$location', '$http', '$filter', '$routeParams', 'routeService', 
    function ViewActController($scope, $location, $http, $filter, $routeParams, routeService) {

        $scope.go = routeService.go;
    
        
        var acts = JSON.parse(localStorage.getItem('acts')) || [];
        var act = acts.filter(function (act) { return act.id == $routeParams.actId; })[0];
        act.createDate = new Date(act.createDate);
        act.docSignDate = new Date(act.docSignDate);
        $scope.act = act;
    
}]);