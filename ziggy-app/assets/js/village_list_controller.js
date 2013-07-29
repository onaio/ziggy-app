function villageListController($scope) {
    $scope.bridge = new VillageListBridge();

    $scope.getVillages = function () {
        return $scope.bridge.getVillages();
    };

    $scope.villages = $scope.getVillages();

    pageView.onReload(function () {
        $scope.$apply(function () {
            $scope.villages = $scope.getVillages();
        });
    });

}
