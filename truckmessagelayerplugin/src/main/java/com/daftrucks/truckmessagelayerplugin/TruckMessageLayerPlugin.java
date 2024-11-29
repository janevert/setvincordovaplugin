package com.daftrucks.truckmessagelayerplugin;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import daftrucks.truckmessageapi.MessageListener;
import daftrucks.truckmessageapi.MessageType;
import daftrucks.truckmessageapi.MessageTypes;
import daftrucks.truckmessageapi.TruckMessageConnection;

public class TruckMessageLayerPlugin extends CordovaPlugin {
    private static final String TAG = "TruckMessageLayerPlugin";

    private final MessageListener<String> mStringListener = (messageType, s) -> {
        // nothing to do
    };
    private final MessageListener<Integer> mIntListener = (messageType, i) -> {
        if (messageType == MessageTypes.ServerConnectionStatus) {
            synchronized (TAG) {
                Integer serverConnectionStatus = MessageTypes.ServerConnectionStatus.getLatestValue();
                if (serverConnectionStatus != null && serverConnectionStatus == MessageTypes.CONNECTION_CONNECTED) {
                    TAG.notifyAll();
                }
            }
        }
        // else nothing to do
    };

    private TruckMessageConnection mConnection;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        // abusing TAG as a lock
        synchronized (TAG) {
            mConnection = TruckMessageConnection.getInstance();
            Log.i(TAG, "Initializing Truck Message Layer... " + mConnection.getVersionInformation().getSdkVersion());
            mConnection.initialize(cordova.getContext());
            mConnection.registerListener(MessageTypes.ServerConnectionStatus, mIntListener);
            mConnection.registerListener(MessageTypes.Vin, mStringListener);
            mConnection.registerListener(MessageTypes.FuelType, mIntListener);
            mConnection.registerListener(MessageTypes.DafSolutionType, mIntListener);
            try {
                // wait for the connection to the DAF CAN receiver
                TAG.wait(1000);
            } catch (InterruptedException e) {
                Log.w(TAG, "Failed to wait for server connection: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mConnection.close();
        mConnection = null;
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        MessageType<?> mType = MessageTypes.ParkingBrake;
        try {
            if ("getVin".equals(action)) {
                Log.i(TAG, "Get VIN");
                mType = MessageTypes.Vin;
                String value = mConnection.getLatestValue(MessageTypes.Vin);
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getFuelType".equals(action)) {
                Log.i(TAG, "Get fuel type");
                mType = MessageTypes.FuelType;
                Integer value = mConnection.getLatestValue(MessageTypes.FuelType);
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getSolutionType".equals(action)) {
                Log.i(TAG, "Get solution type");
                mType = MessageTypes.DafSolutionType;
                Integer value = mConnection.getLatestValue(MessageTypes.DafSolutionType);
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getServerConnected".equals(action)) {
                Log.i(TAG, "Get server connected");
                mType = MessageTypes.ServerConnectionStatus;
                Integer value = mConnection.getLatestValue(MessageTypes.ServerConnectionStatus);
                PluginResult result = new PluginResult(PluginResult.Status.OK, value == MessageTypes.CONNECTION_CONNECTED);
                callbackContext.sendPluginResult(result);
            } else {
                Log.w(TAG, "Unrecognized action " + action);
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.w(TAG, "Failed to get " + mType.getName(), e);
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Failed to get " + mType.getName() + " because: " + e.getMessage());
            callbackContext.sendPluginResult(result);
            return false;
        }
    }
}
