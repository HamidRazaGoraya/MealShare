package com.raza.mealshare.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.UploadImage;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class MyUploadService extends MyBaseTaskService {

    private static final String TAG = "MyUploadService";
    private final FirebaseRef ref=new FirebaseRef();
    /** Intent Actions **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String Add_New_File="Add_New_File";
    private StorageReference mStorageRef;
    private ArrayList<UploadFilesModel>  filesForUpload;
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > 26) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        NotificationChannel chan = new NotificationChannel("example.permanence", "Background Service",NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.YELLOW);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(chan);
        startForeground(2, new NotificationCompat.Builder(this, "example.permanence").setOngoing(true).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@NotNull Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            filesForUpload = (ArrayList<UploadFilesModel>) intent.getExtras().getSerializable(EXTRA_FILE_URI);
            assert filesForUpload != null;
            uploadFromUri();
        }else if (Add_New_File.equals(intent.getAction())){
            ArrayList<UploadFilesModel> newUpload=(ArrayList<UploadFilesModel>) intent.getExtras().getSerializable(EXTRA_FILE_URI);
            assert newUpload != null;
            filesForUpload.addAll(newUpload);
        }
        return START_REDELIVER_INTENT;
    }
    private void uploadFromUri() {
        if (filesForUpload.size()>0){
         uploadFile(0);
        }
    }

    private void  uploadFile(final  int index){
        uploader(index);
    }
    private void uploader(final  int index){
        Log.i("uploading",String.valueOf(index));
        UploadFilesModel uploadFilesModel=filesForUpload.get(index);
        UploadFilesOrignal selectedFiles = new UploadFilesOrignal(uploadFilesModel.getData());
        String thumbnail= Utilities.GetName();
        selectedFiles.setThumbnailUrl(thumbnail);
        StorageReference photoRef=FileStorageRef(selectedFiles,thumbnail);


        final UploadFilesOrignal finalSelectedFiles = selectedFiles;
        taskStarted();
        showProgressNotification(getString(R.string.progress_uploading)+" " + (index + 1) +"/"+ filesForUpload.size(), 0, 0);
        assert photoRef != null;
        photoRef.putFile(selectedFiles.getUri()).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                showProgressNotification(getString(R.string.progress_uploading)+" " + (index + 1) +"/"+ filesForUpload.size(),
                        snapshot.getBytesTransferred(),
                        snapshot.getTotalByteCount());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Map<String, Object> ImageUploaded = new HashMap<>();
                Map<String,Object> firebase_metadata=new HashMap<>();
                firebase_metadata.put(ref.data_thumbnail_storage_path, photoRef.getParent().getPath()+"/thumbnails/"+finalSelectedFiles.getThumbnailUrl()+"_200x300.jpg");
                firebase_metadata.put(ref.data_fullimage_storage_path, photoRef.getPath());
                ImageUploaded.put(ref.All_Images,FieldValue.arrayUnion(firebase_metadata));
                FirebaseFirestore.getInstance().collection(ref.users).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(ref.AllFoodShared).document(finalSelectedFiles.getDocumentPath()).update(ImageUploaded);
                if (index== filesForUpload.size()-1){
                    broadcastUploadFinished(finalSelectedFiles.getUri());
                    showUploadFinishedNotification("uploaded", finalSelectedFiles);
                    taskCompleted();
                    stopSelf();
                }else {
                    uploadFile(index+1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                broadcastUploadFinished(finalSelectedFiles.getUri());
                showUploadFinishedNotification(null, finalSelectedFiles);
                taskCompleted();
            }
        });
    }

    private void broadcastUploadFinished(@Nullable Uri fileUri) {
        Intent broadcast = new Intent(UPLOAD_ERROR)
                .putExtra(EXTRA_DOWNLOAD_URL, (Uri) null)
                .putExtra(EXTRA_FILE_URI, fileUri);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    private void showUploadFinishedNotification(@Nullable String downloadUrl, @Nullable UploadFilesOrignal fileUri) {

        dismissProgressNotification();
        assert fileUri != null;
        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri.getUri())
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success_image) : getString(R.string.upload_failure);
        showFinishedNotification("", caption, intent, success);

    }
    @NotNull
    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);
        return filter;
    }
    @NotNull
    private StorageReference FileStorageRef(@NotNull UploadFilesOrignal selectedFiles, String thumbnail){
        StorageReference photoRef;
        photoRef=mStorageRef.child(ref.AllFood).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref.user_submitted_data);
        UploadImage.UploadThumbnailImage(selectedFiles.getUri(), photoRef.child("thumbnails").child(thumbnail + "_200x300.jpg"));
        return photoRef.child(thumbnail + ".jpg");
    }

}