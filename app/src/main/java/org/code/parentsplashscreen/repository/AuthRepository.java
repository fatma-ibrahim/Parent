package org.code.parentsplashscreen.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.code.parentsplashscreen.network.ApiClient;
import org.code.parentsplashscreen.network.ApiService;
import org.code.parentsplashscreen.responses.LoginResponse;
import org.code.parentsplashscreen.responses.RegisterResponse;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private ApiService apiService;

    public AuthRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<RegisterResponse> createUser(String name, String email, String password, String passwordConfirmation, String mobileNumber, String code, Double latitude, Double longitude, String region) {
        MutableLiveData<RegisterResponse> mutableLiveData = new MutableLiveData<>();
        Single<RegisterResponse> single = apiService.createUser(name, email, password, passwordConfirmation, mobileNumber, code, latitude, longitude, region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<RegisterResponse> observer = new SingleObserver<RegisterResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe-register");
            }

            @Override
            public void onSuccess(RegisterResponse registerResponse) {
                Log.d(TAG, "( onSuccess )-register");
                mutableLiveData.setValue(registerResponse);
            }


            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessageData(responseBody);
                    RegisterResponse registerResponse = new RegisterResponse();
                    registerResponse.setMessage(error);
                    Log.d(TAG, error);
                    mutableLiveData.setValue(registerResponse);
                } else {
                    String body = e.getMessage();
                    RegisterResponse registerResponse = new RegisterResponse();
                    registerResponse.setMessage(body);
                    Log.d(TAG, body);
                    mutableLiveData.setValue(registerResponse);

                }
            }
        };
        single.subscribe(observer);
        return mutableLiveData;
    }

    public LiveData<LoginResponse> loginUser(String email, String password) {
        MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();
        Single<LoginResponse> single = apiService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<LoginResponse> observer = new SingleObserver<LoginResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe-login");
            }

            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Log.d(TAG, "success:login " + loginResponse.getMessage());
                mutableLiveData.setValue(loginResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error:login " + e.toString());
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setMessage(error);
                    mutableLiveData.setValue(loginResponse);
                } else {
                    String body = e.getMessage();
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setMessage("Failed!, Check Your Internet Connection!");
                    Log.d(TAG, body);
                    mutableLiveData.setValue(loginResponse);

                }
            }
        };
        single.subscribe(observer);
        return mutableLiveData;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getErrorMessageData(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("data");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
