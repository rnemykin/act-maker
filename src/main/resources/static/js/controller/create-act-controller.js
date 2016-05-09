acts.controller('CreateACtController', function CreateACtController($scope, $location, $http) {
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
        $http.post('/acts', act, { responseType: 'arraybuffer' }).then(onCreateSuccess, onCreateFail);

    };

    var onCreateSuccess = function(response) {
        var data = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'});
        var header = response.headers()['content-disposition'];
        var fileName = header.substr(header.indexOf('=') + 1);
        saveAs(data, decodeURI(fileName));

        var acts = JSON.parse(localStorage.getItem('acts')) || [];
        var act = response.config.data;
        act.id = new Date().getTime(); 
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
                // $scope.hasInfo = false;
                $scope.hasError = false;
                $scope.errorMsg = '';
                // $scope.infoMsg = '';
            });

        }, 3000);
    };
});