package com.raza.mealshare.Services;

import android.net.Uri;

import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.Serializable;

public class UploadFilesModel implements Serializable  {
    private  String Data;
    public UploadFilesModel(@NotNull Uri uri, String DocumentID) {
        JSONObject  jsonObject=new JSONObject();
        try {
            FirebaseRef ref = new FirebaseRef();
            jsonObject.put(ref.uri,uri.toString());
            jsonObject.put(ref.DoumentId,DocumentID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Data=jsonObject.toString();
    }
    public String getData() {
        return Data;
    }
}
