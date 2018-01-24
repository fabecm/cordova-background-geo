package it.crispybacon.backgroundgeo;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import it.campagnolo.pluginlocationmycampy.MyCampyLocationManager;

public class BackgroundGeo extends CordovaPlugin {

   public static int REQUEST_FINE_LOCATION = 102;

   MyCampyLocationManager myCampyLocationManager;
    CallbackContext callbackContext;

   String [] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
   boolean started = false;


   public boolean execute(String action, JSONArray args,final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        myCampyLocationManager = MyCampyLocationManager.getInstance(this.cordova.getActivity().getApplicationContext());

       if (action.equals("start")) {
            if(hasPermisssion())
                startListener();
            else {
                started = false;
                PermissionHelper.requestPermissions(this, 0, permissions);
            }
        }
        else if (action.equals("stop")){
           started = false;
            myCampyLocationManager.stopListenerLocationChange();
        }
        else if (action.equals("getPoints")){
            ArrayList<String> points =  myCampyLocationManager.getPoints();
            JSONArray pointsObject = new JSONArray();
            for(String json : points){
                try {
                    pointsObject.put(new JSONObject(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            PluginResult result = new PluginResult(PluginResult.Status.OK, (JSONArray) pointsObject);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        }
        return true;
    }

   public void startListener(){
        Log.i("PLUGIN_BACKGROUND_GEO", "startListenerLocationChange");
       started = true;
        myCampyLocationManager.startListenerLocationChange(new MyCampyLocationManager.MyCampyLocationListener() {
            @Override
            public void onLocationChange(String json) {
                PluginResult result = null;
                try {
                    result = new PluginResult(PluginResult.Status.OK, (JSONObject) new JSONObject(json));
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

           }
        }, 1000, 15);
    }

   public boolean hasPermisssion() {
        for(String p : permissions)
        {
            if(!PermissionHelper.hasPermission(this, p))
            {
                return false;
            }
        }
        return true;
    }

   public void requestPermissions(int requestCode)
    {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }

   public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException
    {
        PluginResult result;
        //This is important if we're using Cordova without using Cordova, but we have the geolocation plugin installed
        if(callbackContext != null) {
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_DENIED) {
                    result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                    callbackContext.sendPluginResult(result);
                    return;
                }

           }
            //result = new PluginResult(PluginResult.Status.OK);
            //callbackContext.sendPluginResult(result);
            startListener();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("BackgroundGeo","onDestroy");
        if(started && myCampyLocationManager!=null){
            myCampyLocationManager.stopListenerLocationChange();
        }
    }
}