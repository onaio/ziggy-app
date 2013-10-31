describe('clts.champion', function() {

    var $_httpBackend_;
    
    var controller;
    var myScope;

    beforeEach(function() {
        window.localStorage.clear();
        window.clts.api.baseURL = '/';
        
        module('clts.champion');
    });

    beforeEach(inject(function($rootScope, $controller, $httpBackend) {

        myScope = $rootScope.$new();
        controller = $controller('activateController',  {
            $scope: myScope,
        });

        $_httpBackend_ = $httpBackend;

    }));

    it('the champion should be inactivate by default', function() {
        expect(myScope.champion.activated).toEqual(false);
    });

    it('should activate the champion', function() {
        
        $_httpBackend_.expectPOST('/champions/1234567890/activate/').
            respond(200, {champion: {activated: true}});

        $_httpBackend_.expectGET('templates/champion_welcome.html').
            respond(200, '');
        
        myScope.activate('1234567890');
        $_httpBackend_.flush();
        expect(myScope.champion.activated).toEqual(true);

    });


});
