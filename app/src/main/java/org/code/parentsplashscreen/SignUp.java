package org.code.parentsplashscreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import org.code.parentsplashscreen.responses.RegisterResponse;
import org.code.parentsplashscreen.viewmodels.AuthViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.ActivitySignUpBinding;

import java.util.List;


public class SignUp extends AppCompatActivity implements PermissionsListener {
    private ActivitySignUpBinding signUpBinding;
    // Permission Manager
    private PermissionsManager permissionsManager;
    // Location Manager
    private LocationManager manager;
    // view model
    private AuthViewModel authViewModel;
    // ActivityResultLauncher
    ActivityResultLauncher<Intent> launchSomeActivity;
    // progress bar
    ProgressDialog progressDialog;
    //fields
    String name;
    String email;
    String mobileNumber;
    String password;
    String password_confirmation;
    String code;
    Double latitude;
    Double longitude;
    String region;
    // req code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AuthViewModel.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration is preparing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        /**If User Click On Login Button**/
        signUpBinding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, LogIn.class));
            finish();
        });

        // You need to create a launcher variable inside onAttach or onCreate or global, i.e, before the activity is displayed
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            name = signUpBinding.textFirstName.getText().toString() + " " + signUpBinding.textLastName.getText().toString();
                            email = signUpBinding.textEmail.getText().toString();
                            mobileNumber = signUpBinding.textNumber.getText().toString();
                            password = signUpBinding.textPassword.getText().toString();
                            password_confirmation = signUpBinding.textPasswordConfirm.getText().toString();
                            code = signUpBinding.textSchoolCode.getText().toString();
                            latitude = data.getDoubleExtra("latitude", 0);
                            longitude = data.getDoubleExtra("longitude", 0);
                            region = data.getStringExtra("region");
                            progressDialog.show();
                            authViewModel.createUser(name, email, password, password_confirmation, mobileNumber, code, latitude, longitude, region)
                                    .observe(SignUp.this, new Observer<RegisterResponse>() {
                                        @Override
                                        public void onChanged(RegisterResponse registerResponse) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            if (registerResponse.getData().getCode() != null && registerResponse.getData() != null) {
                                                registerResponse.getData().getCode().forEach(code -> Toast.makeText(SignUp.this, code.toString(), Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getName() != null) {
                                                registerResponse.getData().getName().forEach(name -> Toast.makeText(SignUp.this, name.toString(), Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getEmail() != null) {
                                                registerResponse.getData().getEmail().forEach(email -> Toast.makeText(SignUp.this, email.toString(), Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getMobileNumber() != null) {
                                                registerResponse.getData().getMobileNumber().forEach(mobileNumber -> Toast.makeText(SignUp.this, mobileNumber.toString(), Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getPassword() != null) {
                                                registerResponse.getData().getPassword().forEach(password -> Toast.makeText(SignUp.this, password.toString(), Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getRegion() != null) {
                                                registerResponse.getData().getRegion().forEach(region -> Toast.makeText(SignUp.this, region, Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getLit() != null) {
                                                registerResponse.getData().getLit().forEach(latitude -> Toast.makeText(SignUp.this, latitude, Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.getData().getLng() != null) {
                                                registerResponse.getData().getLng().forEach(longitude -> Toast.makeText(SignUp.this, longitude, Toast.LENGTH_SHORT).show());
                                            }
                                            if (registerResponse.isSuccess() == true) {
                                                startActivity(new Intent(SignUp.this, LogIn.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });

        /**If User Click On Get Started**/
        signUpBinding.buttonGetStarted.setOnClickListener(view -> {
            if (signUpBinding.textFirstName.getText().toString().isEmpty() || signUpBinding.textFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your First Name", Toast.LENGTH_SHORT).show();
                return;
            } else if (signUpBinding.textLastName.getText().toString().isEmpty() || signUpBinding.textLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Last Name", Toast.LENGTH_SHORT).show();
                return;
            } else if (signUpBinding.textEmail.getText().toString().isEmpty() || signUpBinding.textEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Email", Toast.LENGTH_SHORT).show();
                return;
            } else if (signUpBinding.textNumber.getText().toString().isEmpty() || signUpBinding.textNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Number", Toast.LENGTH_SHORT).show();
                return;
            } else if (signUpBinding.textPassword.getText().toString().isEmpty() || signUpBinding.textPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Fill Your Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (PermissionsManager.areLocationPermissionsGranted(SignUp.this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    int res = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        launchSomeActivity.launch(new Intent(SignUp.this, LocationOptionActivity.class));
                    } else {
                        Toast.makeText(this, "Application needs phone state permission", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    launchSomeActivity.launch(new Intent(SignUp.this, LocationOptionActivity.class));
                }
            } else {
                // the permissions is not granted.
                permissionsManager = new PermissionsManager(this);
                //here we request for location from user
                permissionsManager.requestLocationPermissions(SignUp.this);
            }
        });


    }

    /**
     * Permissions Handling
     **/
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(SignUp.this, "Please Accept The permission To Go Through The App.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            Toast.makeText(SignUp.this, "The Permission Is Granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "READ_PHONE_STATE accepted", Toast.LENGTH_SHORT).show();

                }
        }
    }

}