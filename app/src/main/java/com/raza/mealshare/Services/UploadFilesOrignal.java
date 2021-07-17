package com.raza.mealshare.Services;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.raza.mealshare.ExtraFiles.FirebaseRef;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

public class UploadFilesOrignal implements Serializable  {
    private  Uri uri;
    private String thumbnailUrl;
    private String DocumentPath;
    public UploadFilesOrignal(@NotNull String data) {
        FirebaseRef ref=new FirebaseRef();
        try {
            JSONObject bundle=new JSONObject(data);
            Log.i("Image_Path",bundle.getString(ref.uri));
            uri=Uri.parse(bundle.getString(ref.uri));
            DocumentPath=bundle.getString(ref.DoumentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDocumentPath() {
        return DocumentPath;
    }

    public void setDocumentPath(String documentPath) {
        DocumentPath = documentPath;
    }

    public Uri getUri() {
        return uri;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


}
