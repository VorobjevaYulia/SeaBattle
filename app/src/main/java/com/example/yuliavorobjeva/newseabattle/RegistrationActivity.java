package com.example.yuliavorobjeva.newseabattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends BaseForRegistrationActivity implements View.OnClickListener {

    private EditText mEmailEditText;
    private EditText mPassEditText;
    private Button mSignInBtn;
    private Button mCreateUserBtn;
    private TextView mEnterEmailTV;
    private TextView mEnterPasswordTV;

    SharedPreferences sharedPreferences;


    private FirebaseAuth mAuth;
    private Typeface typeface;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mSignInBtn = (Button) findViewById(R.id.sign_in_btn);
        mCreateUserBtn = (Button) findViewById(R.id.create_user_btn);
        mEmailEditText = (EditText) findViewById(R.id.email_et);
        mPassEditText = (EditText) findViewById(R.id.pass_et);
        mEnterEmailTV = (TextView) findViewById(R.id.enter_email_tv);
        mEnterPasswordTV = (TextView) findViewById(R.id.enter_pass_tv);

        mSignInBtn.setOnClickListener(this);
        mCreateUserBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        mEmailEditText.setText(sharedPreferences.getString("email", ""));
        mPassEditText.setText(sharedPreferences.getString("pass", ""));


        typeface = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        mSignInBtn.setTypeface(typeface);
        mCreateUserBtn.setTypeface(typeface);
        mEmailEditText.setTypeface(typeface);
        mPassEditText.setTypeface(typeface);
        mEnterPasswordTV.setTypeface(typeface);
        mEnterEmailTV.setTypeface(typeface);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            Log.e("mytag", "HAS USER");
        } else {
            Log.e("mytag", "NO USER");
        }
    }

    private void createAccount(String email, String password) {
        Log.e("my_tag", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("my_log", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
                            SharedPreferences.Editor ed = sharedPreferences.edit();
                            ed.putString("email",mEmailEditText.getText().toString());
                            ed.putString("pass", mPassEditText.getText().toString());
                            ed.apply();

                            signIn(mEmailEditText.getText().toString(), mPassEditText.getText().toString());

                        } else {

                            Log.e("my_log", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed",
                                    Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d("my_log", "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("my_log", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("my_log", "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed. Please, try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed. Please, try again", Toast.LENGTH_LONG);
                        }
                        hideProgressDialog();
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Required.");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }

        String password = mPassEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassEditText.setError("Required.");
            valid = false;
        } else {
            mPassEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("email", mEmailEditText.getText().toString());
        if(view.getId() == mCreateUserBtn.getId()) {
            createAccount(mEmailEditText.getText().toString(), mPassEditText.getText().toString());
            startActivity(intent);

        } else {
            signIn(mEmailEditText.getText().toString(), mPassEditText.getText().toString());
            startActivity(intent);
        }
    }

}
