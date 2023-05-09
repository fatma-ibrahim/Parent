package org.code.parentsplashscreen.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import org.code.parentsplashscreen.models.Driver;
import org.code.parentsplashscreen.models.Vehicle;
import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.viewmodels.DriverViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.FragmentDriverBinding;

import stream.customalert.CustomAlertDialogue;

public class DriverFragment extends Fragment {

    private static final String TAG = "DriverFragment-Fragment";
    // Binding
    private FragmentDriverBinding driverBinding;
    private LocationManager manager;
    // view model
    private DriverViewModel driverViewModel;
    // token
    private String token;
    //driver item, vehicle item models
    private Vehicle vehicleItem;
    private Driver driverItem;
    // progress bar
    ProgressDialog progressDialog;


    public DriverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        driverBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver, container, false);
        driverViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(DriverViewModel.class);
        token = SharedPreferencesManager.getInstance(getActivity()).getToken();

        // progress bar
        progressDialog = new ProgressDialog(getActivity());

        return driverBinding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDriverInfo();
    }

    private void getDriverInfo() {
        progressDialog.setTitle("Getting Driver Info.");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        driverViewModel.getDriverInfo("Bearer " + token).observe(getActivity(), driverInfoResponse -> {
            progressDialog.dismiss();
            if (driverInfoResponse != null) {
                if (driverInfoResponse.isSuccess() != false) {
                    Toast.makeText(getActivity(), driverInfoResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    driverItem = driverInfoResponse.getData().getDriver();
                    driverBinding.nameText.setText(driverItem.getName());
                    driverBinding.idText.setText(Integer.toString(driverItem.getId()));
                    driverBinding.editEmail.setText(driverItem.getEmail());
                    driverBinding.editMobile.setText(driverItem.getMobileNumber());
                    driverBinding.editLicense.setText(driverItem.getLicenseNumber());
                    Picasso.get().load(driverInfoResponse.getData().getDriver().getImagePath()).error(R.drawable.ic_error).into(driverBinding.driverImage);
                    vehicleItem = driverInfoResponse.getData().getVehicle();
                    driverBinding.vehicleID.setText(Integer.toString(vehicleItem.getId()));
                    driverBinding.vehicleNameTxt.setText(vehicleItem.getModel());
                    driverBinding.vehicleNumberTxt.setText(vehicleItem.getLicensePlate());
                    driverBinding.vehicleColorTxt.setBackgroundColor(Color.parseColor(vehicleItem.getColor()));
                } else {
                    showDialog2("Sorry", driverInfoResponse.getMessage());
                }
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