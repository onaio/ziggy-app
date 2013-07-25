function FormBridge() {
    var formContext = window.formContext;
    if (typeof formContext === "undefined" && typeof FakeFormContext !== "undefined") {
        formContext = new FakeFormContext();
    }

    return {
        delegateToFormLaunchView: function (formName, entityId) {
            return formContext.startFormActivity(formName, entityId);
        }
    };
}

function FakeFormContext() {
    return {
        startFormActivity: function (formName, entityId) {
            alert("Launching form: " + formName + ", for entityId: '" + entityId + "'");
        }
    }
}
