package org.code.parentsplashscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.viewmodels.AuthViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.ActivityLoginBinding;

public class LogIn extends AppCompatActivity {
    private ActivityLoginBinding loginBinding;
    private AuthViewModel authViewModel;
    private String email;
    private String password;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1002;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AuthViewModel.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login is preparing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        /** When User Click On Get Started **/
        loginBinding.getStarted.setOnClickListener(view -> {
            startActivity(new Intent(LogIn.this, SignUp.class));
            finish();
        });

        /** When User Click On Log In **/
        loginBinding.buttonLogin.setOnClickListener(view -> {
            if (loginBinding.textEmail.getText().toString().isEmpty() || loginBinding.textEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Email", Toast.LENGTH_SHORT).show();
                return;
            } else if (loginBinding.textPassword.getText().toString().isEmpty() || loginBinding.textPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                int res = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
                if (res == PackageManager.PERMISSION_GRANTED) {
                    logInProcess();
                } else {
                    Toast.makeText(this, "Application needs phone state permission", Toast.LENGTH_SHORT).show();
                }

            } else {
                logInProcess();
            }
        });
    }

    private void logInProcess() {
        email = loginBinding.textEmail.getText().toString();
        password = loginBinding.textPassword.getText().toString();
        progressDialog.show();
        authViewModel.loginUser(email, password).observe(this, loginResponse -> {
            progressDialog.dismiss();
            if (loginResponse.isSuccess() == false) {
                Toast.makeText(LogIn.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LogIn.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPreferencesManager.getInstance(LogIn.this).saveToken(loginResponse.getData().getToken());
                SharedPreferencesManager.getInstance(LogIn.this).saveUser(loginResponse.getData().getFather());
                startActivity(new Intent(LogIn.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            int res = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
            if (res != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 123);
            }
        }
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(LogIn.this, MainActivity.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LogIn.this, "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}