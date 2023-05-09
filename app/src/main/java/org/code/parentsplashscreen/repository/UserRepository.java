package org.code.parentsplashscreen.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.code.parentsplashscreen.network.ApiClient;
import org.code.parentsplashscreen.network.ApiService;
import org.code.parentsplashscreen.responses.ShowUserResponse;
import org.code.parentsplashscreen.responses.UpdateResponse;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class UserRepository {
    private static final String TAG = "userRepository";
    private ApiService apiService;

    public UserRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<ShowUserResponse> getUserInfo(String token) {
        MutableLiveData<ShowUserResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<ShowUserResponse> single = apiService.showUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ShowUserResponse> singleObserver = new SingleObserver<ShowUserResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(ShowUserResponse showUserResponse) {
                Log.d(TAG, "onSuccess");
                responseMutableLiveData.setValue(showUserResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    ShowUserResponse showUserResponse = new ShowUserResponse();
                    showUserResponse.setMessage(error);
                    responseMutableLiveData.setValue(showUserResponse);
                } else {
                    String body = e.getMessage();
                    ShowUserResponse showUserResponse = new ShowUserResponse();
                    showUserResponse.setMessage(body);
                    Log.d(TAG, body);
                    responseMutableLiveData.setValue(showUserResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    public LiveData<UpdateResponse> updateUser(String fatherName, String email, String mobileNumber, Double latitude, Double longitude, String region, String image_path, String token) {
        MutableLiveData<UpdateResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<UpdateResponse> single = apiService.updateUser(fatherName, email, mobileNumber, latitude, longitude, region, image_path, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<UpdateResponse> singleObserver = new SingleObserver<UpdateResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(UpdateResponse updateResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(updateResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setMessage(error);
                    responseMutableLiveData.setValue(updateResponse);
                } else {
                    String bodyq = e.getMessage();
                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setMessage("Failed!, Check Your Internet Connection!");
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(updateResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }


}
