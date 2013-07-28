function NavigationBridge() {
    var navigationContext = window.navigationContext;
    if (typeof navigationContext === "undefined" && typeof FakeNavigationContext !== "undefined") {
        navigationContext = new FakeNavigationContext();
    }

    return {
        delegateToVillageListView: function () {
            return navigationContext.startVillageListActivity();
        }
    };
}

function FakeNavigationContext() {
    return {
        startVillageListActivity: function () {
            window.location.href = "village_list.html"
        }
    }
}
