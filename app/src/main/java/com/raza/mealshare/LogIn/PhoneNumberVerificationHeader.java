package com.raza.mealshare.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Home;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class PhoneNumberVerificationHeader extends Fragment implements IOnBackPressed {
    private View view;
    private Bundle bundle;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private OtpTextView otpTextView;
    private LoadingDialog loadingDialog;
    private FirebaseAuth mAuth;
    private FirebaseRef ref=new FirebaseRef();
    private TextView tv_coundown;
    private CountDownTimer countDownTimer;
    public PhoneNumberVerificationHeader() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_phone_number_verification_header, container, false);
        loadingDialog = new LoadingDialog(requireContext(), R.style.DialogLoadingTheme);
        mAuth=FirebaseAuth.getInstance();
        bundle=getArguments();
        TextInputEditText phoneNumber=view.findViewById(R.id.phoneNumber);
        phoneNumber.setText(bundle.getString(ref.user_phone_number));
        tv_coundown = view.findViewById(R.id.tv_coundown);
        countDownTimer();
        initToolbar();
        view.findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!otpTextView.getOTP().isEmpty()){
                        verifyPhoneNumberWithCode(mVerificationId,otpTextView.getOTP());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        setUpOTP();
        return view;
    }

    private void setUpOTP() {
        loadingDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                bundle.getString(ref.user_phone_number),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                requireActivity(),               // Activity (for callback binding)
                mCallbacks);

        otpTextView = view.findViewById(R.id.otp_view);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
            }
        });

    }

    private void countDownTimer() {
        view.findViewById(R.id.resendButton).setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long l) {
                try {
                    String text = String.format(Locale.getDefault(), "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                    tv_coundown.setText(getString(R.string.waiting_for_otp)+text);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                try {
                    tv_coundown.setText("00:00");
                    view.findViewById(R.id.resendButton).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.resendButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resendVerificationCode(bundle.getString(ref.user_phone_number), mResendToken);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        countDownTimer.start();
    }
    private void initToolbar() {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                requireActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks   mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            loadingDialog.dismiss();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.i("sms", "onVerificationFailed" +e.getMessage());
            loadingDialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.i("sms", "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
            loadingDialog.dismiss();
            countDownTimer.start();

        }
    };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loadingDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.i("user", "verified");

                                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                                    if (user!=null)
                                    {
                                        FirebaseFirestore.getInstance().collection(ref.users).document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                loadingDialog.dismiss();
                                                if (task.isSuccessful()){
                                                    if (Objects.requireNonNull(task.getResult()).exists()){
                                                        startActivity(new Intent(getContext(), Home.class));
                                                        requireActivity().finish();
                                                    }else {
                                                        Bundle bundle=new Bundle();
                                                        Fragment fragment=new SignUpEmail();
                                                        fragment.setArguments(bundle);
                                                        loadFragment(fragment);
                                                    }
                                                }else {
                                                    if (getContext()!=null){
                                                        Toast.makeText(getContext(), "Try again later", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    loadingDialog.dismiss();
                                    if(getContext()!=null){
                                        Toast.makeText(getContext(), "not Verified", Toast.LENGTH_SHORT).show();
                                        Log.i("user", "notVerified");
                                    }
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("user", "notVerified");
                loadingDialog.dismiss();
            }
        });
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void loadFragment(Fragment fragment){
        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "SignIn");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public boolean onBackPressed() {
        try {
            FirebaseAuth.getInstance().signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
        loadFragment(new SignIn());
        Log.i("test","test3");
        return true;
    }
}
