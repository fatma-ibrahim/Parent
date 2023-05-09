package org.code.parentsplashscreen.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.code.parentsplashscreen.repository.AuthRepository;
import org.code.parentsplashscreen.responses.LoginResponse;
import org.code.parentsplashscreen.responses.RegisterResponse;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<RegisterResponse> createUser(String name, String email, String password, String passwordConfirmation, String mobileNumber, String code, Double latitude, Double longitude, String region) {
        return authRepository.createUser(name, email, password, passwordConfirmation, mobileNumber, code, latitude, longitude, region);
    }

    public LiveData<LoginResponse> loginUser(String email, String password) {
        return authRepository.loginUser(email, password);
    }
}
