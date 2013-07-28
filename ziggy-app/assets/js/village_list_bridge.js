function VillageListBridge() {
    var context = window.context;
    if (typeof context === "undefined" && typeof FakeVillageListContext !== "undefined") {
        context = new FakeVillageListContext();
    }

    return {
        getVillages: function () {
            return JSON.parse(context.get());
        }
    };
}

function FakeVillageListContext() {
    return {
        get: function () {
            return JSON.stringify([
                {
                    "entityId": "1",
                    "name": "Bherya",
                    "code": "0001"
                },
                {
                    "entityId": "2",
                    "name": "Munjanahalli",
                    "code": "0002"
                }
            ]);
        }
    };
}