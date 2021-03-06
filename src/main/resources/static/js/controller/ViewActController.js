acts.controller('ViewActController', ['$scope', '$location', '$http', '$filter', '$routeParams', 'routeService', 
    function ViewActController($scope, $location, $http, $filter, $routeParams, routeService) {

        $scope.go = routeService.go;
        
        var acts = JSON.parse(localStorage.getItem('acts')) || [];
        var act = acts.filter(function (act) { return act.id == $routeParams.actId; })[0];
        act.createDate = new Date(act.createDate);
        act.docSignDate = new Date(act.docSignDate);
        $scope.act = act;
        
        $scope.saveAct = function() {
            $scope.isProcessing = true;

            var now = new Date();
            var createDate = this.act.createDate || new Date();
            createDate.setHours(now.getHours());
            createDate.setMinutes(now.getMinutes());
            this.act.createDate = createDate;

            if (this.act.docSignDate) {
                this.act.docSignDate.setHours(now.getHours());
                this.act.docSignDate.setMinutes(now.getMinutes());
            }

            $http.post('/acts', $scope.act, {responseType: 'arraybuffer'})
                .then(onCreateSuccess, onCreateFail)
                .finally(function () {
                    $scope.isProcessing = false;
                })
                .then(function () {
                    if($scope.hasError) {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                $scope.hasSuccess = false;
                                $scope.hasError = false;
                                $scope.errorMsg = '';
                            });
                        }, 3000);
                    } else {
                        setTimeout(function() {
                            $scope.$apply(function() { $scope.go('/'); });
                        }, 1000);
                    }
                });
        };

        var onCreateSuccess = function (response) {
            var header = response.headers()['content-disposition'];
            $scope.act.fileName = decodeURI(header.substr(header.indexOf('=') + 1));
            $scope.act.clob = String.fromCharCode.apply(null, new Uint8Array(response.data));

            var acts = JSON.parse(localStorage.getItem('acts')) || [];
            for(var i = 0; i < acts.length; i++) {
                if(acts[i].id == $scope.act.id) {
                    acts[i] = $scope.act;
                    break;
                }
            }

            localStorage.setItem('acts', JSON.stringify(acts));
            $scope.hasSuccess = true;
        };

        var onCreateFail = function (response) {
            $scope.errorMsg = response.status == 422
                ? decodeURI(response.headers()['act-error-message']).replace(/\+/g, ' ')
                : 'Ooops';
            $scope.hasError = true;
        };

}]);