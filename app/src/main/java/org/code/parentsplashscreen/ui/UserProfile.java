package org.code.parentsplashscreen.ui;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.code.parentsplashscreen.LocationOptionActivity;
import org.code.parentsplashscreen.LogIn;
import org.code.parentsplashscreen.models.Father;
import org.code.parentsplashscreen.responses.ShowUserResponse;
import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.viewmodels.UserViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.FragmentUserProfileBinding;

import java.io.File;


import stream.customalert.CustomAlertDialogue;


public class UserProfile extends Fragment {
    private static final String TAG = "UserProfile-Fragment";
    // Binding
    private FragmentUserProfileBinding userProfileBinding;
    // Activity Result Launcher for image
    ActivityResultLauncher<Object> cropActivityResultLauncher;
    ActivityResultContract<Object, Uri> activityResultContract;
    // ActivityResultLauncher for activity
    ActivityResultLauncher<Intent> launchSomeActivity;
    // Permission Manager
    private PermissionsManager permissionsManager;
    // Location Manager
    private LocationManager manager;
    // parent location
    private Location parentLocation;
    // view model
    private UserViewModel userViewModel;
    // token
    private String token;
    // region & lat & long
    private String region;
    private Double latitude;
    private Double longitude;
    // fields
    private String fatherName;
    private String email;
    private String mobileNumber;
    // progress bar
    ProgressDialog progressDialog;
    // image
    private String imagePath;
    private Uri imageURI;

    public UserProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        token = SharedPreferencesManager.getInstance(getActivity()).getToken();
        // progress bar
        progressDialog = new ProgressDialog(getActivity());
        return userProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** when user clicks on save/update button **/
        userProfileBinding.saveUpdate.setOnClickListener(view13 -> {
            updateUserData();
        });

        /** Load User Data **/
        loadUserData();


        activityResultContract = new ActivityResultContract<Object, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                /** createIntent() — accepts input data and creates an intent, which will be later launched by calling launch() **/
                return CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1, 1)
                        .getIntent(getActivity().getApplicationContext());
            }

            @Override
            public Uri parseResult(int resultCode, @Nullable Intent intent) {
                /** parseResult() — is responsible for returning the result, handling resultCode, and parsing the data. **/
                if (CropImage.getActivityResult(intent) != null) {
                    return CropImage.getActivityResult(intent).getUri();
                }
                return null;
            }
        };

        cropActivityResultLauncher = registerForActivityResult(activityResultContract, result -> {
            if (result != null) {
                imageURI = result;
                uploadImage(result);
                userProfileBinding.profileImage.setImageURI(result);
            }
        });

        // when user click on image
        userProfileBinding.profileImage.setOnClickListener(view1 -> {
            cropActivityResultLauncher.launch(null);
        });
        /** When User Click On LogOut **/
        userProfileBinding.logOut.setOnClickListener(view22 -> {
            showDialog();
        });

        /** ACTIVITY FOR TAKING NEW LOCATION FROM THE USER MANUALLY OR AUTOMATICALLY **/
        // when user click on edit location
        userProfileBinding.editLocation.setOnClickListener(view12 -> {
            launchSomeActivity.launch(new Intent(getActivity(), LocationOptionActivity.class));
        });
        // You need to create a launcher variable inside onAttach or onCreate or global, i.e, before the activity is displayed
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            latitude = data.getDoubleExtra("latitude", 0);
                            longitude = data.getDoubleExtra("longitude", 0);
                            region = data.getStringExtra("region");
                            userProfileBinding.location.setText(region);
                            Toast.makeText(getActivity(), region, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * Dialog If User Want To Log Out
     **/
    private void showDialog() {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setCancelable(false)
                .setTitle("Log Out")
                .setMessage("Do you want to log out?")
                .setPositiveText("Confirm")
                .setPositiveColor(R.color.negative)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        SharedPreferencesManager.getInstance(getActivity())
                                .clear();
                        startActivity(new Intent(getActivity(), LogIn.class));
                        getActivity().finish();
                        Toast.makeText(getActivity(), "You Logged Out", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeText("Cancel")
                .setNegativeColor(R.color.positive)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    /**
     * Load User Data
     **/
    private void loadUserData() {
        /*
        userProfileBinding.nameText.setText(SharedPreferencesManager.getInstance(getActivity()).getUser().getName());
        userProfileBinding.email.setText(SharedPreferencesManager.getInstance(getActivity()).getUser().getEmail());
        userProfileBinding.location.setText(SharedPreferencesManager.getInstance(getActivity()).getUser().getRegion());
        userProfileBinding.editMobile.setText(SharedPreferencesManager.getInstance(getActivity()).getUser().getMobileNumber());
        userProfileBinding.idText.setText("ID:" + SharedPreferencesManager.getInstance(getActivity()).getUser().getId());
        latitude = Double.valueOf(SharedPreferencesManager.getInstance(getActivity()).getUser().getLit());
        longitude = Double.valueOf(SharedPreferencesManager.getInstance(getActivity()).getUser().getLng());
        region = SharedPreferencesManager.getInstance(getActivity()).getUser().getRegion();
        if (SharedPreferencesManager.getInstance(getActivity()).getUser().getImagePath() != null) {
            imagePath = SharedPreferencesManager.getInstance(getActivity()).getUser().getImagePath();
            Picasso.get().load(SharedPreferencesManager.getInstance(getActivity()).getUser().getImagePath()).into(userProfileBinding.profileImage);
        }*/
        progressDialog.setTitle("getting user info");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        userViewModel.getUserInfo("Bearer " + token).observe(getActivity(), new Observer<ShowUserResponse>() {
            @Override
            public void onChanged(ShowUserResponse showUserResponse) {
                progressDialog.dismiss();
                if (showUserResponse != null && showUserResponse.isSuccess()) {
                    Toast.makeText(getActivity(), showUserResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Picasso.get().load(showUserResponse.getData().get(0).getImagePath()).into(userProfileBinding.profileImage);
                    userProfileBinding.nameText.setText(showUserResponse.getData().get(0).getName());
                    userProfileBinding.email.setText(showUserResponse.getData().get(0).getEmail());
                    userProfileBinding.location.setText(showUserResponse.getData().get(0).getRegion());
                    userProfileBinding.editMobile.setText(showUserResponse.getData().get(0).getMobileNumber());
                    userProfileBinding.idText.setText("ID:" + showUserResponse.getData().get(0).getId());
                    latitude = Double.valueOf(showUserResponse.getData().get(0).getLit());
                    longitude = Double.valueOf(showUserResponse.getData().get(0).getLng());
                    region = showUserResponse.getData().get(0).getRegion();
                } else if (showUserResponse != null && !showUserResponse.isSuccess()) {
                    showDialog2("Sorry", showUserResponse.getMessage());
                }
            }
        });

    }

    /**
     * Function Of Updating User Info
     **/
    private void updateUserData() {
        fatherName = userProfileBinding.nameText.getText().toString();
        email = userProfileBinding.email.getText().toString();
        mobileNumber = userProfileBinding.editMobile.getText().toString();
        if (fatherName.isEmpty() || fatherName.trim().isEmpty()) {
            Toast.makeText(getActivity(), "please fill your name", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty() || email.trim().isEmpty()) {
            Toast.makeText(getActivity(), "please fill your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (mobileNumber.isEmpty() || mobileNumber.trim().isEmpty()) {
            Toast.makeText(getActivity(), "please fill your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (latitude == null || longitude == null || region.isEmpty()) {
            Toast.makeText(getActivity(), "please specify again your location", Toast.LENGTH_SHORT).show();
            return;
        } else if (imagePath == null) {
            Toast.makeText(getActivity(), "please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }
        // progress dialog
        progressDialog.setTitle("Updating is preparing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Log.d("token", token);
        userViewModel.updateUser(fatherName, email, mobileNumber, latitude, longitude, region, imagePath, "Bearer " + token)
                .observe(getActivity(), updateResponse -> {
                    progressDialog.dismiss();
                    if (updateResponse.isSuccess() && updateResponse.getData() != null) {
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Father father = new Father(String.valueOf(longitude), mobileNumber,
                                String.valueOf(latitude), fatherName, updateResponse.getData().getId(), region, email, imagePath);
                        SharedPreferencesManager.getInstance(getActivity()).saveUser(father);
                    } else {
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(userProfileBinding.framelayout, "Information did not updated!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                });


    }

    private void uploadImage(Uri imageURI) {

        File file = new File(String.valueOf(imageURI));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");
        progressDialog.setTitle("Updating Image");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        storageRef.child(String.valueOf(SharedPreferencesManager.getInstance(getActivity()).getUser().getId())).putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        // get upload url
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imagePath = String.valueOf(task.getResult());
                                progressDialog.dismiss();
                                Log.d(TAG, imagePath);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

    private void showDialog2(String title, String message) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(title)
                .setMessage(message)
                .setNegativeText("OK")
                .setNegativeColor(R.color.purple_200)
                .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }


}
