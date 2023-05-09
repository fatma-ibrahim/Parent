package org.code.parentsplashscreen.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.code.parentsplashscreen.repository.UserRepository;
import org.code.parentsplashscreen.responses.ShowUserResponse;
import org.code.parentsplashscreen.responses.UpdateResponse;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<ShowUserResponse> getUserInfo(String token) {
        return userRepository.getUserInfo(token);
    }

    public LiveData<UpdateResponse> updateUser(String fatherName, String email, String mobileNumber, Double latitude, Double longitude, String region, String image_path, String token) {
        return userRepository.updateUser(fatherName, email, mobileNumber, latitude, longitude, region, image_path, token);
    }

}
