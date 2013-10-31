(function() {

    var app = angular.module('clts.champion', [
        'ngRoute',
        'ngTouch',
        'ajoslin.mobile-navigate',

        'clts.villages'
    ]);



    app.factory('championModel', [
        '$rootScope',
        '$http',
        
        function($rootScope, $http) {

            this.champion = window.clts.storage.get('champion') || {activated: false};
            var activate = function(msisdn) {

                var that = this;

                var promise =$http.post(window.clts.api.url('champions', msisdn, 'activate')).
                    success(function(data, status) {

                        if (status == 200) {

                            if (typeof(data.champion) !== 'undefined') {
                                that.champion = data.champion;
                                that.champion.activated = true;
                                window.clts.storage.set('champion', that.champion);
                            }

                            if (typeof(data.villages) !== 'undefined') {
                                window.clts.storage.set('villages', data.villages);
                            }

                        } else {
                            that.champion.activated = false;
                            that.champion.activationErrorCode = status;
                        }
                    }).
                    error(function(data, status) {
                        that.champion.activationErrorCode = status;
                    });

                return promise;
            };

            this.activate = activate.bind(this);
            return this;
        }
    ]);

    app.controller('activateController', [
        '$scope',
        '$navigate',
        'championModel',

        function($scope, $navigate, championModel) {

            $scope.champion = championModel.champion;

            $scope.activate = function(msisdn) {
                championModel.activate(msisdn).
                    then(function() {

                        $scope.champion = championModel.champion;


                        if (championModel.champion.activated) {
                            $navigate.go('/champion/welcome');
                        }
                    });
            };
        }
    ]);

    app.controller('welcomeController', [
        '$scope',
        '$navigate',
        'championModel',
        'villagesModel',

        function($scope, $navigate, championModel, villagesModel) {
            $scope.$navigate = $navigate;
            
            $scope.champion = championModel.champion;
            $scope.villages = villagesModel.villages;
            

        }
    ]);

    app.config(function($routeProvider) {

        $routeProvider.
            when('/champion/activate', {
                controller: 'activateController',
                templateUrl: 'templates/champion_activate.html'
            }).
            when('/champion/welcome', {
                controller: 'welcomeController',
                templateUrl: 'templates/champion_welcome.html'
            });

    });

})();
