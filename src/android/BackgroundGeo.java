package it.crispybacon.backgroundgeo;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BackgroundGeo extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("start")) {
            try {
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
        }
        return true;
    }

}