describe('clts.faqs', function() {

    var $_httpBackend_;
    
    var controller;
    var myScope, myModel;

    beforeEach(function() {
        window.localStorage.clear();
        window.clts.api.baseURL = '/';
        
        module('clts.faqs');
    });

    beforeEach(inject(function($rootScope, $controller, $httpBackend, $injector) {

        myModel = $injector.get('faqsModel');

        myScope = $rootScope.$new();
        controller = $controller('faqsController',  {
            $scope: myScope,
        });

        $_httpBackend_ = $httpBackend;

    }));

    it('should fetch a list of faqs', function() {

        $_httpBackend_.expectGET('/faqs/').
            respond(200, [1, 2, 3, 4]);
        myModel.update();
        $_httpBackend_.flush();
        expect(myModel.faqs.length).toEqual(4);
    });


});
