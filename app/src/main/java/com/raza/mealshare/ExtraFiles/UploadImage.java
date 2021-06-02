package com.raza.mealshare.ExtraFiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;

import androidx.annotation.NonNull;

public class UploadImage {
    public static void UploadProfile(Uri uri,String uid,Uploaded uploaded){
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference finalPhotoRef = mStorageRef.child("users").child(uid).child("profile_pic").child(String.valueOf(System.currentTimeMillis()+".jpeg"));
        finalPhotoRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                uploaded.Status(task.isSuccessful(),finalPhotoRef.getPath());
            }
        });
    }
    public static void UploadProfileBitMap(Bitmap bitmap,String uid, Uploaded uploaded){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference finalPhotoRef = mStorageRef.child("users").child(uid).child("profile_pic").child(String.valueOf(System.currentTimeMillis()+".jpeg"));
        finalPhotoRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                uploaded.Status(task.isSuccessful(),finalPhotoRef.getPath());
            }
        });
    }
    public interface Uploaded{
        void Status(boolean bol,String path);
    }
    public static Bitmap getThumbnail(String picturePath){
        return ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MINI_KIND);
    }
    public static Bitmap getThumbnailImage(@NotNull File picturePath){
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath.getAbsolutePath()), 200, 300);
    }
    public static void UploadThumbnail(@NotNull File file, @NotNull StorageReference storageReference){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getThumbnail(file.getAbsolutePath()).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storageReference.putBytes(data);
    }
    public static void UploadThumbnailImage(@NotNull Uri file, @NotNull StorageReference storageReference){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getThumbnailImage(new File(file.getPath())).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storageReference.putBytes(data);
    }
    public interface Upload{
        void Image(Bitmap bitmap);
    }
    public static void UploadThumbnail(){

    }

}
