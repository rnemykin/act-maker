acts.controller('CreateActController',
    ['$scope', '$location', '$http', '$filter', 'routeService', function CreateActController($scope, $location, $http, $filter, routeService) {

    $scope.go = routeService.go;

    var user = JSON.parse(localStorage.getItem('user'));
    if(user && user.docSignDate) {
        user.docSignDate = new Date(user.docSignDate);
    }
    $scope.user = user;

    var act = {};
    var acts = JSON.parse(localStorage.getItem('acts'));
    if(acts) {
        if(localStorage.getItem('act.incrementNumber') != 0) {
            act.number = Math.max.apply(Math, acts.map(function(el) {
                return el.actNumber;
            })) + 1;
        }
    }
    $scope.act = act;

    $scope.create = function() {
        var now = new Date();
        var createDate = this.act.createDate || new Date();
        createDate.setHours(now.getHours());
        createDate.setMinutes(now.getMinutes());

        if(this.user.docSignDate) {
            this.user.docSignDate.setHours(now.getHours());
            this.user.docSignDate.setMinutes(now.getMinutes());
        }

        var act = {
            userName: this.user.name,
            createDate: createDate,
            actNumber: this.act.number,
            docNumber: this.user.docNumber,
            certSerial: this.user.certSerial,
            certNumber: this.user.certNumber,
            docSignDate: this.user.docSignDate,
            salaryRate: this.user.salaryRate,
            mainTask: this.act.mainTask,
            mainTaskHours: this.act.mainTaskHours,
            additionalTask: this.act.additionalTask,
            additionalTaskHours: this.act.additionalTaskHours
        };

        localStorage.setItem('user', JSON.stringify(this.user));
        $scope.isProcessing = true;
        $http.post('/acts', act, { responseType: 'arraybuffer' })
            .then(onCreateSuccess, onCreateFail)
            .then(function() { $scope.isProcessing = false; });

    };

    var onCreateSuccess = function(response) {
        var data = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'});
        var header = response.headers()['content-disposition'];
        var fileName = decodeURI(header.substr(header.indexOf('=') + 1));
        saveAs(data, fileName);

        var acts = JSON.parse(localStorage.getItem('acts')) || [];
        var act = response.config.data;
        act.id = new Date().getTime();
        act.fileName = fileName;
        act.clob = String.fromCharCode.apply(null, new Uint8Array(response.data));
        acts.push(act);
        localStorage.setItem('acts', JSON.stringify(acts));
        
        $scope.hasSuccess = true;
        setTimeout(function() {
            $scope.$apply(function() {
                $scope.hasSuccess = false;
            });
        }, 3000);
    };

    var onCreateFail = function(response) {
        $scope.errorMsg = response.status == 422
            ? decodeURI(response.headers()['act-error-message']).replace(/\+/g,' ')
            : 'Ooops';
        $scope.hasError = true;

        setTimeout(function() {
            $scope.$apply(function() {
                $scope.hasError = false;
                $scope.errorMsg = '';
            });

        }, 3000);
    };
}]);