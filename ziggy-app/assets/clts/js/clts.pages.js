(function() {

    var app = angular.module('clts.pages', [
        'ngRoute',
        'ngTouch',
        'ajoslin.mobile-navigate',
    ]);

    app.factory('pagesModel', ['$rootScope', '$http',
        function($rootScope, $http) {

            var _that = this;
            
            var pages = window.clts.storage.get('pages');
            if (pages === false) {
                pages = {};
            }

            var update = function(category) {

                var url = window.clts.api.url('pages', category);
                
                
                var promise = $http.get(url).
                    then(function(o) {
                        if (o.data.length > 0 && o.status == 200) {
                            _that.pages[category] = o.data;
                            window.clts.storage.set('pages', _that.pages);
                        }
                    });
                return promise;
            };


            var get = function(category, id) {



                for (var index in _that.pages[category]) {
                    page = _that.pages[category][index];

                    if (page.id == id) return page;
                }

                return {
                    title: 'Not found?'
                };
            };

            this.pages = pages;
            this.update = update;
            this.get = get;
            return this;
        }
    ]);

    app.controller('pagesController', [
        '$scope',
        '$routeParams',
        '$navigate',

        'pagesModel',
        
        function($scope, $routeParams, $navigate, pagesModel) {

            

            var category = $routeParams.category;
            var id = $routeParams.id;
                
            $scope.category = category;
            $scope.pages = pagesModel.pages;
            $scope.$navigate = $navigate;


            if (id) {
                $scope.page = pagesModel.get(category, id);
            }
        }
    ]);

    app.config(function($routeProvider) {
        $routeProvider.
            when('/pages/:category/', {
                controller: 'pagesController',
                templateUrl: 'templates/pages.html'
            }).
            when('/pages/:category/:id/', {
                controller: 'pagesController',
                templateUrl: 'templates/pages_detail.html'
            });
    });


})();
