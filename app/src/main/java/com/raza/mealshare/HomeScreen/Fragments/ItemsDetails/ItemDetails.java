package com.raza.mealshare.HomeScreen.Fragments.ItemsDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.MustSignIn;
import com.raza.mealshare.CustomDialogs.ShowIntrest;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.AllFavoritItemsDeo;
import com.raza.mealshare.Database.AllProductsFills.MyFavoruitProduct;
import com.raza.mealshare.Database.AllProductsFills.OtherProduct;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Tools;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.ActivityItemDetailsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDetails extends AppCompatActivity {
    private ActivityItemDetailsBinding binding;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private OtherProduct otherProduct;
    private FirebaseRef ref=new FirebaseRef();
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog=new LoadingDialog(this,R.style.DialogLoadingTheme);
        otherProduct=new Gson().fromJson(getIntent().getStringExtra("otherProduct"),OtherProduct.class);

        initComponent();
    }



    private void initComponent() {
        binding.backButtonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.itamName.setText(otherProduct.getItemName());
        binding.ShortDescription.setText(otherProduct.getItemDescription());
        binding.longDescription.setText(otherProduct.ItemDescriptionLong);
        if (otherProduct.getUserInfo()!=null){
            if (otherProduct.getUserInfo().getUser_profile_pic()!=null){
                if (TextUtils.isEmpty(otherProduct.getUserInfo().getUser_profile_pic())){
                    Glide.with(this).load(Utilities.StorageReference(otherProduct.getUserInfo().getUser_profile_pic())).into(binding.posterImage);
                }
                binding.posterName.setText(otherProduct.getUserInfo().getUser_name());
            }
        }
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    new MustSignIn(new MustSignIn.Buttons() {
                        @Override
                        public void SelectedSign() {
                            startActivity(new Intent(ItemDetails.this, MainActivity.class));
                            finishAffinity();
                        }
                    }).show(getSupportFragmentManager(),"SignIn");
                    return;
                }
                if (otherProduct.getUserInfo().getUser_uid().equals(FirebaseAuth.getInstance().getCurrentUser())){
                    new CustomToast(ItemDetails.this,"Can't send message to your self");
                    return;
                }
                if (otherProduct.getAllInterested()!=null){
                    for (int i=0;i<otherProduct.getAllInterested().size();i++){
                        if (otherProduct.getAllInterested().get(i).getUser_uid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            new CustomToast(ItemDetails.this,"Already sent message for this product check messages");
                            return;
                        }
                    }
                }
                new ShowIntrest(new ShowIntrest.Buttons() {
                    @Override
                    public void SendMessage() {
                        ProfileInfo profileInfo= Utilities.UserProfile(ItemDetails.this);
                        if (profileInfo.getUser_location()==null){
                            return;
                        }
                        HashMap<String, Object> userInfo=new HashMap<>();
                        userInfo.put(ref.user_name,profileInfo.getUser_name());
                        userInfo.put(ref.user_profile_pic,profileInfo.getUser_profile_pic());
                        userInfo.put(ref.user_uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
                        userInfo.put(ref.user_location,profileInfo.getUser_location());
                        userInfo.put(ref.data_submission_time, FieldValue.serverTimestamp());
                        HashMap<String, Object> allInterested = new HashMap<>();
                        allInterested.put(ref.interested_users,userInfo);
                        allInterested.put(ref.user_uid,otherProduct.getUserInfo().getUser_uid());
                        allInterested.put(ref.product_id,otherProduct.baseKey);
                        loadingDialog.show();
                        FirebaseFirestore.getInstance().collection(ref.interested_users).add(allInterested).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                try {
                                    loadingDialog.dismiss();
                                    if (task.isSuccessful()){
                                        new CustomToast(ItemDetails.this,"Sent");
                                    }else {
                                        new CustomToast(ItemDetails.this,task.getException().getMessage());
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).show(getSupportFragmentManager(),"Interest");
            }
        });
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<All_Images>());

        adapterImageSlider.setItems(otherProduct.getAllImages());
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AllFavoritItemsDeo allFavoritItemsDeo=AllDataBaseConstant.getInstance(ItemDetails.this).allFavoritItemsDeo();
                        if (allFavoritItemsDeo.CheckOnce(otherProduct.baseKey).size()>0){
                            allFavoritItemsDeo.deleteOne(otherProduct.baseKey);
                        }else {
                            allFavoritItemsDeo.insert(new Gson().fromJson(new Gson().toJson(otherProduct), MyFavoruitProduct.class));
                        }
                    }
                });
            }
        });
        AllDataBaseConstant.getInstance(this).allFavoritItemsDeo().CheckIfThisFavor(otherProduct.baseKey).observeForever(new Observer<List<MyFavoruitProduct>>() {
            @Override
            public void onChanged(List<MyFavoruitProduct> myFavoruitProducts) {
                if (myFavoruitProducts.size()>0){
                    binding.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_500)));
                }else {
                    binding.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.weight)));
                }
            }
        });

    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.overlay_dark_10), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<All_Images> items;

        private OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, All_Images obj);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<All_Images> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public All_Images getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<All_Images> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final All_Images o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            Glide.with(act).load(Utilities.StorageReference(items.get(position).getData_fullimage_storage_path())).into(image);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }
}