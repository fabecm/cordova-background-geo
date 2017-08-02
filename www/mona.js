module.exports = {
    start: function () {
        cordova.exec(console.log, console.error, 'BackgroundGeo', 'start', []);
    },
    end: function () {
        cordova.exec(console.log, console.error, 'BackgroundGeo', 'stop', []);
    }
}