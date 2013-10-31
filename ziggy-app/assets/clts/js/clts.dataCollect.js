(function() {

    var app = angular.module('clts.dataCollect', []);

    app.config(function($routeProvider) {
        $routeProvider.
            when('/collect', {
                templateUrl: 'data_collect.html'
            });
    });


})();
