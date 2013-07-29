var pageView = (function () {
    var callbackForReload = function () {
    };

    return {
        reload: function () {
            callbackForReload();
        },
        onReload: function (callBack) {
            callbackForReload = callBack;
        }
    }
})();
