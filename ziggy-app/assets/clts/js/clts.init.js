(function() {

    var app = angular.module('clts', [
        'ngRoute',
        'ngTouch',
        'ajoslin.mobile-navigate',

        'clts.champion',
        'clts.faqs',
        'clts.pages',
        'clts.dataCollect',
    ]);

    app.run([
        '$rootScope',
        '$navigate',
        'championModel',
        'faqsModel',
        'pagesModel',

        function($rootScope, $navigate, championModel, faqsModel, pagesModel) {

            if (championModel.champion.activated !== true) {
                $navigate.go('/champion/activate', 'none');
            }

            // TODO: grab faqs and pages updates.
            faqsModel.update();
            pagesModel.update('training');
            
        }
    ]);

    app.directive('toolbarMenu', ['$navigate',
        function ($navigate) {
        return {
            restrict: 'A',
            templateUrl: 'templates/toolbar.html',
            link: function ($scope, $el, attrs) {
                $scope.back = function(p) {
                    $navigate.go(p, undefined, true);
                };
            }
        };
    }]);

    app.controller('menuController',
        ['$scope', '$navigate',
        'championModel', 'faqsModel', 'pagesModel',
        function($scope, $navigate, championModel, faqsModel, pagesModel) {

            $scope.$navigate = $navigate;

            $scope.reset = function() {

                if (confirm("Are you sure you want to RESET the app?")) {

                    window.localStorage.clear();
                    window.location = '#/';
                }
            };

            $scope.update = function() {

                console.log('-- updating...');
                // update villages.
                faqsModel.update();
                pagesModel.update('training');

                console.log('-- done');
            };
        }
    ]);

    app.config(function($routeProvider) {

        $routeProvider.
            when('/', {
                controller: 'menuController',
                templateUrl: 'templates/menu.html'
            });
    });
})();
