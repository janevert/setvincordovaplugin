var exec = require('cordova/exec');

var PLUGIN_NAME = 'SetVinPlugin';

exports.getVin = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getVin", []);
};
exports.getFuelType = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getFuelType", []);
};
exports.getSolutionType = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getSolutionType", []);
};
