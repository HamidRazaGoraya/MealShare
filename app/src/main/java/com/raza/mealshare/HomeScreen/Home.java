package com.raza.mealshare.HomeScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.MustSignIn;
import com.raza.mealshare.Database.AllProductsFills.TrackAllItems;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Fragments.Messages.MessagesFragment;
import com.raza.mealshare.HomeScreen.Fragments.Profile.ProfileFragments;
import com.raza.mealshare.HomeScreen.Fragments.Search.SearchFragment;
import com.raza.mealshare.HomeScreen.Fragments.Share.ShareHome;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private Fragment current;
    private BottomNavigationView navView;
    private FragmentManager fragmentManager;
    private MessagesFragment messagesFragment =new MessagesFragment();
    private ProfileFragments profiles=new ProfileFragments();
    private ShareHome shareHome=new ShareHome();
    private SearchFragment searchFragment=new SearchFragment();
    private FirebaseRef ref=new FirebaseRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navView = findViewById(R.id.nav_view);
        SetUpNavigation();
        getSettings();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLocation();
    }

    private void checkForLocation() {
        Log.i("location","001");
        if (!Utilities.DoesUserProfileAvailable(this)){
            Log.i("location","002");
            if (!Utilities.DoesLocationAvailable(this)){
                Log.i("location","003");
                Toast.makeText(this, "Location is required to find Items near u", Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(this, PickUpLocation.class));
            }
        }
        if (Utilities.DoesUserProfileAvailable(this)|| Utilities.DoesLocationAvailable(this)){
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                TrackAllItems.GetAllMyItems(this,FirebaseAuth.getInstance().getCurrentUser());
            }
            TrackAllItems.GetAllOtherItems(this);
        }
    }

    private void SetUpNavigation() {
        fragmentManager=getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag("profiles");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("shareHome");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("messages");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("searchFragment");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            fragmentManager.beginTransaction().add(R.id.contant_frame_main, profiles, "profiles").hide(profiles).commit();
            fragmentManager.beginTransaction().add(R.id.contant_frame_main, shareHome, "shareHome").hide(shareHome).commit();
            fragmentManager.beginTransaction().add(R.id.contant_frame_main, messagesFragment, "messages").hide(messagesFragment).commit();
        }
        fragmentManager.beginTransaction().add(R.id.contant_frame_main, searchFragment, "searchFragment").commit();
        current=searchFragment;
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    switch (item.getItemId()){
                        case R.id.navigation_search:
                            fragmentManager.beginTransaction().hide(current).show(searchFragment).commit();
                            current=searchFragment;
                            return true;
                        case R.id.navigation_messages:
                            fragmentManager.beginTransaction().hide(current).show(messagesFragment).commit();
                            current= messagesFragment;
                            return true;
                        case R.id.navigation_share:
                            fragmentManager.beginTransaction().hide(current).show(shareHome).commit();
                            current=shareHome;
                            return true;
                        case R.id.navigation_profile:
                            fragmentManager.beginTransaction().hide(current).show(profiles).commit();
                            current=profiles;
                            return true;
                    }
                }else {
                    if (item.getItemId()!=R.id.navigation_search){
                        MustSignIn mustSignIn=new MustSignIn(new MustSignIn.Buttons() {
                            @Override
                            public void SelectedSign() {
                                startActivity(new Intent(Home.this, MainActivity.class));
                                finish();
                            }
                        });
                        mustSignIn.show(getSupportFragmentManager(),"MustSignIn");
                    }
                }
                return false;
            }
        });
    }
    private void getSettings() {
        FirebaseFirestore.getInstance().collection(ref.ApplicationControl).document(ref.basic_controls).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (value.exists()){
                        SettingsModel settingsModel=value.toObject(SettingsModel.class);
                        if (settingsModel!=null){
                            Utilities.getSharedPreferences(Home.this).edit().putString(ref.Settings,new Gson().toJson(settingsModel)).apply();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            return;
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.i("tes",token);
                Map<String, Object> user = new HashMap<>();
                user.put(ref.fms, token);
                FirebaseFirestore.getInstance().collection(ref.users).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(user);
            }
        });
    }
}