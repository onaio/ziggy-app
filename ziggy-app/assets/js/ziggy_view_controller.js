function ziggyViewController($scope) {
    $scope.formBridge = new FormBridge();
    $scope.navigationBridge = new NavigationBridge();

    $scope.openForm = function (formName, entityId) {
        $scope.formBridge.delegateToFormLaunchView(formName, entityId);
    };

    $scope.openVillageList = function () {
            $scope.navigationBridge.delegateToVillageListView();
    };
}
