package com.raza.mealshare.Database.AllProductsFills;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.MessagesFiles.AllMessagesDeo;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.Models.MessageModel;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TrackAllItems {
    public static void GetAllMyItems(Activity context, @NotNull FirebaseUser firebaseUser){
        FirebaseRef ref=new FirebaseRef();
        FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (value!=null){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AllDataBaseConstant allDataBaseConstant=AllDataBaseConstant.getInstance(context);
                                AllMyItemsDeo allMyItemsDeo =allDataBaseConstant.allMyItemsDeo();
                                allMyItemsDeo.deleteAll();
                                for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                                    PostedItems itemDetails=documentSnapshot.toObject(PostedItems.class).withId(documentSnapshot.getId());
                                    MyProduct myProduct =new MyProduct(itemDetails, Utilities.GetLatLog(context));
                                    Log.i("basekey", myProduct.baseKey);
                                    allMyItemsDeo.insert(myProduct);
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        GetAllMessages(context,firebaseUser.getUid());
    }
    public static void GetAllOtherItems(Activity context){
        FirebaseRef ref=new FirebaseRef();
        Log.i("Found","01");
        FirebaseFirestore.getInstance().collectionGroup(ref.AllFoodShared).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (value!=null){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Found","true");
                                AllDataBaseConstant allDataBaseConstant=AllDataBaseConstant.getInstance(context);
                                AllOtherItemsDeo allOtherItemsDeo =allDataBaseConstant.allOtherItemsDeo();
                                allOtherItemsDeo.deleteAll();
                                for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                                    PostedItems itemDetails=documentSnapshot.toObject(PostedItems.class).withId(documentSnapshot.getId());
                                    OtherProduct myProduct =new OtherProduct(itemDetails, Utilities.GetLatLog(context));
                                    allOtherItemsDeo.insert(myProduct);
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public static void GetAllMessages(Activity context,String userId){
        FirebaseRef ref=new FirebaseRef();
        Log.i("Found","messages");
        FirebaseFirestore.getInstance().collectionGroup(ref.messages).whereEqualTo(ref.receiver,userId).orderBy(ref.time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {
                           for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                               MessageModel messageModel=documentSnapshot.toObject(MessageModel.class).withId(documentSnapshot.getId());

                               AllMessagesDeo allMessagesDeo=  AllDataBaseConstant.getInstance(context).allMessagesDeo();

                               if (allMessagesDeo.loadShopByBaseKey(documentSnapshot.getId()).size()>0){
                                   allMessagesDeo.update(new Messages(messageModel));
                               }else {
                                   allMessagesDeo.insert(new Messages(messageModel));
                               }
                           }
                       }
                   });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        FirebaseFirestore.getInstance().collectionGroup(ref.messages).whereEqualTo(ref.sender,userId).orderBy(ref.time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                                MessageModel messageModel=documentSnapshot.toObject(MessageModel.class).withId(documentSnapshot.getId());
                                AllMessagesDeo allMessagesDeo=  AllDataBaseConstant.getInstance(context).allMessagesDeo();

                                if (allMessagesDeo.loadShopByBaseKey(documentSnapshot.getId()).size()>0){
                                    allMessagesDeo.update(new Messages(messageModel));
                                }else {
                                    allMessagesDeo.insert(new Messages(messageModel));
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
