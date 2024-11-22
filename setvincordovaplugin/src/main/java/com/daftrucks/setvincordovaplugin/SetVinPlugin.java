package com.daftrucks.setvincordovaplugin;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import daftrucks.truckmessageapi.MessageListener;
import daftrucks.truckmessageapi.MessageType;
import daftrucks.truckmessageapi.MessageTypes;
import daftrucks.truckmessageapi.TruckMessageConnection;

public class SetVinPlugin extends CordovaPlugin {
    private static final String TAG = "SetVinPlugin";

    private final MessageListener<String> mStringListener = (messageType, s) -> {
        // nothing to do
    };
    private final MessageListener<Integer> mIntListener = (messageType, i) -> {
        // nothing to do
    };

    private TruckMessageConnection mConnection;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        Log.i(TAG, "Initializing Truck Message Layer...");
        mConnection = TruckMessageConnection.getInstance();
        mConnection.initialize(cordova.getContext());
        mConnection.registerListener(MessageTypes.Vin, mStringListener);
        mConnection.registerListener(MessageTypes.FuelType, mIntListener);
        mConnection.registerListener(MessageTypes.DafSolutionType, mIntListener);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mConnection.close();
        mConnection = null;
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        if ("getVin".equals(action)) {
            Log.i(TAG, "Get VIN");
            String value = mConnection.getLatestValue(MessageTypes.Vin);
            PluginResult result = new PluginResult(PluginResult.Status.OK, value);
            callbackContext.sendPluginResult(result);
        } else if ("getFuelType".equals(action)) {
            Log.i(TAG, "Get fuel type");
            Integer value = mConnection.getLatestValue(MessageTypes.FuelType);
            PluginResult result = new PluginResult(PluginResult.Status.OK, value);
            callbackContext.sendPluginResult(result);
        } else if ("getSolutionType".equals(action)) {
            Log.i(TAG, "Get solution type");
            Integer value = mConnection.getLatestValue(MessageTypes.DafSolutionType);
            PluginResult result = new PluginResult(PluginResult.Status.OK, value);
            callbackContext.sendPluginResult(result);
        } else {
            Log.w(TAG, "Unrecognized action " + action);
            return false;
        }

        return true;
    }
}
