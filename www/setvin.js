var exec = require('cordova/exec');

var PLUGIN_NAME = 'SetVinPlugin';

exports.getVin = function (newVin, success, error) {
  exec(success, error, PLUGIN_NAME, "setVin", [newVin]);
};
