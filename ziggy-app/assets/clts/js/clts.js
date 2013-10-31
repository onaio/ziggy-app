window.clts = {

    api: {
        baseURL: 'http://qa.clts.praekeltfoundation.org/api/v1/',
        //baseURL: 'http://localhost:8080/api/v1/',
        url: function() {
            // a tiny helper method that constructs a URL from the passed in 
            // arguments.
            var args = Array.prototype.slice.call(arguments);
            return window.clts.api.baseURL + args.join('/') + '/';
        }
    },
    
    storage: {
        // a crude wrapper around dom storage.
        prefix: 'clts.v1.',
        set: function(key, value) {
            console.log('~~~ set ' + window.clts.storage.prefix + key);
            var d = JSON.stringify(value);
            window.localStorage.setItem(window.clts.storage.prefix + key, d);
        },

        get: function(key) {
            console.log('~~~ get ' + window.clts.storage.prefix + key);
            var d = window.localStorage.getItem(window.clts.storage.prefix + key) || false;
            if (d === false) return false;
            return JSON.parse(d);
        },
    }
};


if (!Function.prototype.bind) {
  Function.prototype.bind = function (oThis) {
    if (typeof this !== "function") {
      // closest thing possible to the ECMAScript 5 internal IsCallable function
      throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
    }

    var aArgs = Array.prototype.slice.call(arguments, 1),
        fToBind = this,
        fNOP = function () {},
        fBound = function () {
          return fToBind.apply(this instanceof fNOP && oThis ? this : oThis, aArgs.concat(Array.prototype.slice.call(arguments)));
        };

    fNOP.prototype = this.prototype;
    fBound.prototype = new fNOP();

    return fBound;
  };
}