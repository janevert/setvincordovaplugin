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
        checkEverythingReceived();
    };
    private final MessageListener<Integer> mIntListener = (messageType, i) -> {
        checkEverythingReceived();
    };

    private TruckMessageConnection mConnection;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        // abusing TAG as a lock
        synchronized (TAG) {
            long startTime = System.currentTimeMillis();
            mConnection = TruckMessageConnection.getInstance();
            Log.i(TAG, "Initializing Truck Message Layer... " + mConnection.getVersionInformation().getSdkVersion());
            mConnection.initialize(cordova.getContext());
            mConnection.registerListener(MessageTypes.ServerConnectionStatus, mIntListener);
            mConnection.registerListener(MessageTypes.Vin, mStringListener);
            mConnection.registerListener(MessageTypes.FuelType, mIntListener);
            mConnection.registerListener(MessageTypes.DafSolutionType, mIntListener);
            try {
                // wait for the connection to the DAF CAN receiver
                // should not take too long as the DAF CAN receiver is expected to be running already
                TAG.wait(1000);
            } catch (InterruptedException e) {
                Log.w(TAG, "Failed to wait for server connection: " + e.getMessage(), e);
            }
            long initTime = System.currentTimeMillis() - startTime;
            Log.i(TAG, "initialization " + initTime + "ms");
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mConnection.close();
        mConnection = null;
    }

    private void checkEverythingReceived() {
        synchronized (TAG) {
            Integer serverConnectionStatus = MessageTypes.ServerConnectionStatus.getLatestValue();
            Integer solutionType = MessageTypes.DafSolutionType.getLatestValue();
            Integer deviceType = MessageTypes.DafDeviceType.getLatestValue();
            Integer fuelType = MessageTypes.FuelType.getLatestValue();
            String vin = MessageTypes.Vin.getLatestValue();
            if (serverConnectionStatus != null && serverConnectionStatus == MessageTypes.CONNECTION_CONNECTED
                    && solutionType != null
                    && deviceType != null
                    && fuelType != null
                    && vin != null) {
                TAG.notifyAll();
            }
        }
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        MessageType<?> mType = MessageTypes.ParkingBrake;
        try {
            if ("getVin".equals(action)) {
                Log.i(TAG, "Get VIN");
                mType = MessageTypes.Vin;
                String value = mConnection.getLatestValue(MessageTypes.Vin);
                if (value == null) {
                    // something doesn't like null
                    value = "";
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getFuelType".equals(action)) {
                Log.i(TAG, "Get fuel type");
                mType = MessageTypes.FuelType;
                Integer value = mConnection.getLatestValue(MessageTypes.FuelType);
                if (value == null) {
                    // not available
                    value = 0;
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getSolutionType".equals(action)) {
                Log.i(TAG, "Get solution type");
                mType = MessageTypes.DafSolutionType;
                // value for solution type cannot be null
                Integer value = mConnection.getLatestValue(MessageTypes.DafSolutionType);
                PluginResult result = new PluginResult(PluginResult.Status.OK, value);
                callbackContext.sendPluginResult(result);
            } else if ("getServerConnected".equals(action)) {
                Log.i(TAG, "Get server connected");
                mType = MessageTypes.ServerConnectionStatus;
                // value for server connection status cannot be null
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
