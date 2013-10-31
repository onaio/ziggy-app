(function() {

    var app = angular.module('clts.faqs', [
        'ngRoute',
        'ngTouch',
        'ajoslin.mobile-navigate',
    ]);

    app.factory('faqsModel', ['$rootScope', '$http',
        function($rootScope, $http) {

            var _that = this;
            var faqs = window.clts.storage.get('faqs');

            var update = function() {
                var url = window.clts.api.url('faqs');
                
                var promise = $http.get(url).
                    then(function(o) {

                        if (o.data.length > 0 && o.status == 200) {
                          window.clts.storage.set('faqs', o.data);
                          _that.faqs = o.data;
                        }
                    });
                return promise;
            };
            
            this.faqs = faqs;
            this.update = update;
            return this;
        }
    ]);

    app.controller('faqsController',
        ['$scope', 'faqsModel',

        function($scope, faqsModel) {

            $scope.faqs = faqsModel.faqs;
        }
    ]);

    app.config(function($routeProvider) {
        $routeProvider.
            when('/faqs', {
                controller: 'faqsController',
                templateUrl: 'templates/faqs.html'
            });
    });


})();
