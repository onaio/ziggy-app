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
                    "name": "Bherya",
                    "code": "0001"
                },
                {
                    "name": "Munjanahalli",
                    "code": "0002"
                }
            ]);
        }
    };
}