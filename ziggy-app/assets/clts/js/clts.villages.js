(function() {

    var app = angular.module('clts.villages', [
        'ngRoute',
        'ngTouch',
        'ajoslin.mobile-navigate',
    ]);

    app.factory('villagesModel', [
        '$http',
        
        function($http) {

            var _that = this;
            var villages = window.clts.storage.get('villages') || [];

            var createVillage = function(name) {
                
                var village = {name: name};
                _that.villages.push(village);
                window.clts.storage.set('villages', villages);
                return village;
            };

            this.villages = villages;
            this.createVillage = createVillage;
            return this;
        }
    ]);

    app.controller('selectVillageController',[
        '$scope',
        'villagesModel',
        
        function($scope, villagesModel) {

            $scope.villages = villagesModel.villages;


            $scope.openForm = function(name, village)
            {
                var meta = {
                    fieldOverrides: "{\"name\":\""+ village.name + "\",\"code\":\"1234\"}"
                };

                if (typeof(window.formContext) === 'undefined') {
                    alert('Not running with-in ZIGGY!');
                    return;
                }

                console.log('*** opening form: ' + name + ' with fieldOverrides: ' + JSON.stringify(meta));

                window.formContext.startFormActivity(name, undefined, JSON.stringify(meta));
            };
        }
    ]);

    app.controller('createVillageController', [
        '$scope',
        'villagesModel',
        
        function($scope, villagesModel) {

            $scope.create = function(name) {
                var village = villagesModel.createVillage(name);
                $scope.name = '';
            };
        }
    ]);


    app.config(function($routeProvider) {
        $routeProvider.
            when('/village/select', {
                controller: 'selectVillageController',
                templateUrl: 'templates/village_select.html'
            });
    });


})();
