module.exports = {
    start: function (success, error) {
        cordova.exec(success, error, 'BackgroundGeo', 'start', []);
    },
    stop: function (success, error) {
        cordova.exec(success, error, 'BackgroundGeo', 'stop', []);
    },
    getPoints: function (success, error) {
        cordova.exec(success, error, 'BackgroundGeo', 'getPoints', []);
    }
};
