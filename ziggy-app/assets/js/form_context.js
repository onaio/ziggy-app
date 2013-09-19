function FormBridge() {
    var formContext = window.formContext;
    if (typeof formContext === "undefined" && typeof FakeFormContext !== "undefined") {
        formContext = new FakeFormContext();
    }

    return {
        delegateToFormLaunchView: function (formName, entityId, metadata) {
            return formContext.startFormActivity(formName, entityId, metadata);
        }
    };
}

function FakeFormContext() {
    return {
        startFormActivity: function (formName, entityId, metadata) {
            alert("Launching form: " + formName + ", for entityId: '" + entityId + ", with metadata: '" + metadata + "'");
        }
    }
}
