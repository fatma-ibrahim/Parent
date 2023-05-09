package org.code.parentsplashscreen.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.code.parentsplashscreen.models.Child;
import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.ChildDialogViewBinding;

import java.io.File;

public class ChildDialogInfo extends AppCompatDialogFragment {
    private ChildDialogViewBinding binding;
    private String gender;
    private String childImage;
    ChildAdded childAdded;
    // Activity Result Launcher
    ActivityResultLauncher<Object> cropActivityResultLauncher;
    ActivityResultContract<Object, Uri> activityResultContract;
    // image_path
    private String image_path;
    // progress bar
    ProgressDialog progressDialog;
    // child
    private Child child;

    public ChildDialogInfo(ChildAdded childAdded) {
        this.childAdded = childAdded;
    }

    public ChildDialogInfo(ChildAdded childAdded, Child child) {
        this.childAdded = childAdded;
        this.child = child;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.child_dialog_view, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        final AlertDialog dialog = builder.create();
        progressDialog = new ProgressDialog(getActivity());

        binding.spinner.setItems("Male", "Female");
        gender = binding.spinner.getItems().get(0).toString();
        binding.spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            gender = item;
        });

        /** When User Click On Image **/
        binding.childImage.setOnClickListener(view -> {
            cropActivityResultLauncher.launch(null);
        });
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
                uploadImage(result);
                binding.childImage.setImageURI(result);
                childImage = result.toString();
            }
        });

        if (child == null) {
            /** When User Click On Save button **/
            binding.saveChildInfo.setOnClickListener(view -> {
                if (binding.childName.getText().toString().trim().isEmpty() || binding.childName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please fill your child name", Toast.LENGTH_SHORT).show();
                    Log.d("hossam", "please fill your child name");
                    return;
                } else if (binding.childAge.getText().toString().trim().isEmpty() || binding.childAge.getText().toString().isEmpty() || binding.childAge.getText().toString().equals(".")) {
                    Toast.makeText(getActivity(), "please fill your child age", Toast.LENGTH_SHORT).show();
                    return;
                } else if (binding.childClass.getText().toString().trim().isEmpty() || binding.childClass.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please fill your child class", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gender.isEmpty()) {
                    Toast.makeText(getActivity(), "please choose your child gender", Toast.LENGTH_SHORT).show();
                    return;
                } else if (image_path == null) {
                    Toast.makeText(getActivity(), "please choose your child image", Toast.LENGTH_SHORT).show();
                    return;
                }
                childAdded.onChildAdded(new Child(
                        gender,
                        image_path,
                        binding.childName.getText().toString(),
                        binding.childClass.getText().toString(),
                        Integer.parseInt(binding.childAge.getText().toString())));

            });

        } else {
            binding.saveChildInfo.setText("Update");
            image_path = child.getImagePath();
            Picasso.get().load(child.getImagePath()).into(binding.childImage);
            binding.childName.setText(child.getName());
            binding.childAge.setText(String.valueOf(child.getAge()));
            binding.childClass.setText(child.getJsonMemberClass());
            binding.spinner.setText(child.getGender());
            binding.saveChildInfo.setOnClickListener(view -> {
                if (binding.childName.getText().toString().trim().isEmpty() || binding.childName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please fill your child name", Toast.LENGTH_SHORT).show();
                    Log.d("hossam", "please fill your child name");
                    return;
                } else if (binding.childAge.getText().toString().trim().isEmpty() || binding.childAge.getText().toString().isEmpty() || binding.childAge.getText().toString().equals(".")) {
                    Toast.makeText(getActivity(), "please fill your child age", Toast.LENGTH_SHORT).show();
                    return;
                } else if (binding.childClass.getText().toString().trim().isEmpty() || binding.childClass.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please fill your child class", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gender.isEmpty()) {
                    Toast.makeText(getActivity(), "please choose your child gender", Toast.LENGTH_SHORT).show();
                    return;
                } else if (child.getImagePath() == null) {
                    Toast.makeText(getActivity(), "please choose your child image", Toast.LENGTH_SHORT).show();
                    return;
                }
                childAdded.onChildUpdated(new Child(
                        child.getId(),
                        gender,
                        image_path,
                        binding.childName.getText().toString(),
                        binding.childClass.getText().toString(),
                        Integer.parseInt(binding.childAge.getText().toString())));
            });
        }
        return dialog;

    }


    public interface ChildAdded {
        void onChildAdded(Child child);

        void onChildUpdated(Child child);
    }

    private void uploadImage(Uri imageURI) {
        File file = new File(String.valueOf(imageURI));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("children_images");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        storageRef.child(file.getName()).putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Image added Successfully", Toast.LENGTH_SHORT).show();
                        // get upload url
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                image_path = String.valueOf(task.getResult());
                                progressDialog.dismiss();
                                Log.d("dialog", image_path);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("dialog", e.getMessage());
                        progressDialog.dismiss();
                    }
                });

    }

}
