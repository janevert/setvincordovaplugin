package com.daftrucks.setvincordovaplugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.daftrucks.dafbtcanreceiver.setvinapi.ISetVinInterface;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

public class SetVinPlugin extends CordovaPlugin {
    private static final String TAG = "GetVinPlugin";
    private static final String SET_VIN_ACTION = "com.daftrucks.dafbtcanreceiver.set_vin";
    private static final String SET_VIN_PACKAGE = "com.daftrucks.dafbtcanreceiver";
    private static final String SET_VIN_SERVICE = "com.daftrucks.dafbtcanreceiver.BtCanService";

    private ISetVinInterface mSetVinInterface;

    private final ServiceConnection mSetVinConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Connected to service.");
            mSetVinInterface = ISetVinInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSetVinInterface = null;
            Log.i(TAG, "Disconnected from service.");
        }
    };

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        Log.i(TAG, "Binding to service...");
        Intent i = new Intent(SET_VIN_ACTION);
        i.setClassName(SET_VIN_PACKAGE, SET_VIN_SERVICE);
        cordova.getActivity().bindService(i, mSetVinConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        cordova.getActivity().unbindService(mSetVinConnection);
        mSetVinInterface = null;
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        if ("setVin".equals(action)) {
            ISetVinInterface vinInterface = mSetVinInterface;
            if (vinInterface != null) {
                String newVin;
                try {
                    newVin = args.getString(0);
                } catch (JSONException e) {
                    Log.i(TAG, "Failed to retrieve VIN argument.");
                    PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                    return true;
                }
                try {
                    int status = vinInterface.setVin(newVin);
                    PluginResult result = new PluginResult(PluginResult.Status.OK, status);
                    result.setKeepCallback(false);
                    Log.i(TAG, "Sending plugin result status=" + status);
                    callbackContext.sendPluginResult(result);
                } catch (RemoteException e) {
                    callbackContext.error(e.getMessage());
                }
            } else {
                PluginResult result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
                result.setKeepCallback(false);
                callbackContext.sendPluginResult(result);
            }
            return true;
        }

        return false;
    }
}
