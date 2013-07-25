function ziggyViewController($scope) {
    $scope.formBridge = new FormBridge();

    $scope.openForm = function (formName, entityId) {
        $scope.formBridge.delegateToFormLaunchView(formName, entityId);
  };
}
