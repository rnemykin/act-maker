acts.controller('CreateACtController', function CreateACtController($scope, $location) {
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

    $scope.create = function($scope) {
        var act = {
            userName: this.user.name,
            date: this.act.date,
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

    };
});