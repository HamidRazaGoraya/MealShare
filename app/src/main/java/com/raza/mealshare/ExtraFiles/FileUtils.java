package com.raza.mealshare.ExtraFiles;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;

public class FileUtils {

    @NotNull
    public static JSONObject GetFileDetailsImage(@NotNull File file){
        JSONObject jsonObject=new JSONObject();

         try {
             jsonObject.put("data_path_on_device",file.getAbsolutePath());
             jsonObject.put("data_modified_time", file.lastModified());
             jsonObject.put("data_file_size",file.length());
             SizeFromImage sizeFromImage=new SizeFromImage(file.getAbsolutePath());
             jsonObject.put("image_height",sizeFromImage.height());
             jsonObject.put("image_width",sizeFromImage.width());
             Log.i("data",jsonObject.toString());
         }catch (Exception e){
             e.printStackTrace();
         }
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                Log.i("data2",directory.toString());
                if (directory.getName().equals("JPEG") || directory.getName().equals("Exif IFD0") )
                {
                    for (Tag tag : directory.getTags()) {
                        switch (tag.getTagType()){
                            case 274:
                                try {
                                    jsonObject.put("image_orientation",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 271:
                                try {
                                    jsonObject.put("user_phone_make",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 272:
                                try {
                                    jsonObject.put("user_phone_model",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 37385:
                                try {
                                    jsonObject.put("image_flash_used",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;

                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    @NotNull
    public static JSONObject GetFileDetailsVideo(@NotNull File file){
        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("data_path_on_device",file.getAbsolutePath());
            jsonObject.put("data_modified_time", file.lastModified());
            jsonObject.put("data_file_size",file.length());
            /*
            SizeFromImage sizeFromImage=new SizeFromImage(file.getAbsolutePath());
            jsonObject.put("image_height",sizeFromImage.height());
            jsonObject.put("image_width",sizeFromImage.width());
            Log.i("data",jsonObject.toString());*/
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                Log.i("data2",directory.toString());
                if (directory.getName().equals("MP4") || directory.getName().equals("MP4 Sound") || directory.getName().equals("MP4 Video") || directory.getName().equals("File Type") || directory.getName().equals("File"))
                {
                    for (Tag tag : directory.getTags()) {
                        switch (tag.getTagType()){
                            case 3:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_detected_mime_type",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 4:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_expected_file_name_extension",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    if (directory.getName().equals("File")){
                                        jsonObject.put("data_file_name",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 205:
                                try {
                                    if (directory.getName().equals("MP4 Video"))
                                    jsonObject.put("data_height",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 204:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_width",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 210:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_compression_type",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 301:
                                try {
                                    if (directory.getName().equals("MP4 Sound")){
                                        jsonObject.put("data_sound_format",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 512:
                                try {
                                    if (directory.getName().equals("MP4")){
                                        jsonObject.put("data_rotation",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 259:
                                try {
                                    if (directory.getName().equals("MP4")){
                                        jsonObject.put("data_duration",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 101:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_creation_time",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 209:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_depth",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 214:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_frame_rate",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_detected_file_type_long_name",tag.getDescription());
                                    }else if (directory.getName().equals("File")){
                                        jsonObject.put("data_file_size",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;

                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    @NotNull
    public static JSONObject GetFileDetailsAudio(@NotNull File file){
        JSONObject jsonObject=new JSONObject();
/*
        try {
            jsonObject.put("data_path_on_device",file.getAbsolutePath());
            jsonObject.put("data_modified_time", file.lastModified());
            jsonObject.put("data_file_size",file.length());

            SizeFromImage sizeFromImage=new SizeFromImage(file.getAbsolutePath());
            jsonObject.put("image_height",sizeFromImage.height());
            jsonObject.put("image_width",sizeFromImage.width());
            Log.i("data",jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }*/
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getAbsolutePath());
            int METADATA_KEY_DURATION=Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int METADATA_KEY_BITRATE=Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            jsonObject.put("METADATA_KEY_DURATION",METADATA_KEY_DURATION);
            jsonObject.put("METADATA_KEY_BITRATE",METADATA_KEY_BITRATE);
            jsonObject.put("data_path_on_device",file.getAbsolutePath());
            jsonObject.put("data_modified_time", file.lastModified());
            jsonObject.put("data_file_size",file.length());
            /*for (Directory directory : metadata.getDirectories()) {
                Log.i("data2",directory.toString());
                if (directory.getName().equals("MP4") || directory.getName().equals("MP4 Sound") || directory.getName().equals("MP4 Video") || directory.getName().equals("File Type") || directory.getName().equals("File"))
                {
                    for (Tag tag : directory.getTags()) {
                        switch (tag.getTagType()){
                            case 3:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_detected_mime_type",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 4:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_expected_file_name_extension",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    if (directory.getName().equals("File")){
                                        jsonObject.put("data_file_name",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 205:
                                try {
                                    if (directory.getName().equals("MP4 Video"))
                                        jsonObject.put("data_height",tag.getDescription());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 204:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_width",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 210:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_compression_type",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 301:
                                try {
                                    if (directory.getName().equals("MP4 Sound")){
                                        jsonObject.put("data_sound_format",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 512:
                                try {
                                    if (directory.getName().equals("MP4")){
                                        jsonObject.put("data_rotation",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 259:
                                try {
                                    if (directory.getName().equals("MP4")){
                                        jsonObject.put("data_duration",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 101:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_creation_time",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 209:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_depth",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 214:
                                try {
                                    if (directory.getName().equals("MP4 Video")){
                                        jsonObject.put("data_frame_rate",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    if (directory.getName().equals("File Type")){
                                        jsonObject.put("data_detected_file_type_long_name",tag.getDescription());
                                    }else if (directory.getName().equals("File")){
                                        jsonObject.put("data_file_size",tag.getDescription());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;

                        }
                    }
                }

            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    private static void print(@NotNull Metadata metadata)
    {
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                Log.d("tag",tag.getDescription());
                Log.d("tag",tag.getDirectoryName());
                Log.d("tag",tag.getTagName());

                Log.d("tag",String.valueOf(tag.getTagType()));
            }

        }
    }

}