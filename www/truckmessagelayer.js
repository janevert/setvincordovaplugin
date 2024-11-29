var exec = require('cordova/exec');

var PLUGIN_NAME = 'TruckMessageLayerPlugin';

// returns string
exports.getVin = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getVin", []);
};

// returns int
exports.getFuelType = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getFuelType", []);
};

// returns int
exports.getSolutionType = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getSolutionType", []);
};

// returns boolean
exports.getServerConnected = function (success, error) {
  exec(success, error, PLUGIN_NAME, "getServerConnected", []);
};
