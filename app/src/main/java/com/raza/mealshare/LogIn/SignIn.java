package com.raza.mealshare.LogIn;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Home;
import com.raza.mealshare.Intro.IntroImageWizard;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment {
    private View view;
    private LoadingDialog loadingDialog;
    private FirebaseAuth mAuth;
    private FirebaseRef ref=new FirebaseRef();
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPref;
    private TextInputEditText editTextCarrierNumber;
    private CountryCodePicker ccp;
    public SignIn() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view= inflater.inflate(R.layout.login_sign_in_fragment, container, false);
        sharedPref = requireActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        loadingDialog = new LoadingDialog(requireContext(), R.style.DialogLoadingTheme);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        final TextView phoneNumber=view.findViewById(R.id.phoneLogin);
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.setClickable(false);
                if (Verified()){
                    Bundle bundle=new Bundle();
                    bundle.putString(ref.user_phone_number,ccp.getFullNumberWithPlus());
                    Fragment fragment=new PhoneNumberVerificationHeader();
                    fragment.setArguments(bundle);
                    loadFragment(fragment,"phoneNumber");
                }else {
                    phoneNumber.setClickable(true);
                }


            }

            private boolean Verified() {
                if (!ccp.isValidFullNumber()){
                    editTextCarrierNumber.setError("Not valid");
                    editTextCarrierNumber.requestFocus();
                    return false;
                }
                if (!ccp.getDefaultCountryCodeWithPlus().contains("+91")){
                    Toast.makeText(requireContext(), "Country not valid", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
        ((TextView) view.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());
        setUpButtons();
        return view;

    }

    private void setUpButtons() {

        ccp =  view.findViewById(R.id.ccp);
        editTextCarrierNumber = view.findViewById(R.id.phoneNumber);
        editTextCarrierNumber.requestFocus();
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        ccp.detectNetworkCountry(true);
    }

    private void loadFragment(Fragment fragment, String tag){
        if (fragment != null) {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, tag);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        loadingDialog.show();
        if (currentUser!=null){
            loadingDialog.dismiss();
            if (sharedPref.contains(ref.NewUser)){
                     if (sharedPref.getBoolean(ref.NewUser,false)){
                         sharedPref.edit().putBoolean(ref.NewUser,false).apply();
                         FirebaseAuth.getInstance().signOut();
                     }else {
                         if (sharedPref.contains("firstTime")){
                             startActivity(new Intent(getContext(), Home.class));
                         }else {
                             startActivity(new Intent(getContext(), IntroImageWizard.class));
                         }
                        requireActivity().finish();
                     }
            }else {
                if (sharedPref.contains("firstTime")){
                    startActivity(new Intent(getContext(), Home.class));
                }else {
                    startActivity(new Intent(getContext(), IntroImageWizard.class));
                }
                requireActivity().finish();
            }
         }else {
            loadingDialog.dismiss();
        }
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(@NotNull final GoogleSignInAccount acct) {
        Log.d("data", "firebaseAuthWithGoogle:" + acct.getId());
        loadingDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("data", "signInWithCredential:success");
                           final FirebaseUser user = mAuth.getCurrentUser();
                           Log.i("value",user.getUid());
                            FirebaseFirestore.getInstance().collection(ref.users).document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot=task.getResult();
                                        if (documentSnapshot!=null && documentSnapshot.contains(ref.user_name)){
                                            Log.i("status","not null");
                                            mGoogleSignInClient.signOut();
                                            updateUI(user);
                                        }else {
                                            Log.i("status","null");
                                            loadingDialog.dismiss();
                                            UploadUserDetails(acct);
                                        }
                                    }else {
                                        loadingDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed to logIn try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                 loadingDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed to logIn try again", Toast.LENGTH_SHORT).show();
                                }
                            }) ;
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("data", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("data", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void UploadUserDetails(@NotNull GoogleSignInAccount acct) {
        Bundle bundle=new Bundle();
        bundle.putString(ref.user_name,acct.getGivenName());
        bundle.putString(ref.LastName,acct.getFamilyName());
        bundle.putString(ref.user_email,acct.getEmail());
        try {
            bundle.putString(ref.user_profile_pic, Objects.requireNonNull(acct.getPhotoUrl()).toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        mGoogleSignInClient.signOut();
        Fragment fragment=new SignUpEmail();
        fragment.setArguments(bundle);
        loadFragment(fragment,"tag");
    }
}
