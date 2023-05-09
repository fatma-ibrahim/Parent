package org.code.parentsplashscreen.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.code.parentsplashscreen.network.ApiClient;
import org.code.parentsplashscreen.network.ApiService;
import org.code.parentsplashscreen.responses.DriverInfoResponse;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class DriverRepository {
    private static final String TAG = "driverRepository";
    private ApiService apiService;

    public DriverRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<DriverInfoResponse> getDriverInfo(String token) {
        MutableLiveData<DriverInfoResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<DriverInfoResponse> single = apiService.DriverInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<DriverInfoResponse> singleObserver = new SingleObserver<DriverInfoResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(DriverInfoResponse showDriverResponse) {
                Log.d(TAG, "onSuccess");
                System.out.println("====Debug Line===");
                System.out.println(showDriverResponse);
                responseMutableLiveData.setValue(showDriverResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    DriverInfoResponse driverInfoResponse = new DriverInfoResponse();
                    driverInfoResponse.setMessage(error);
                    Log.d(TAG, error);
                    responseMutableLiveData.setValue(driverInfoResponse);
                } else {
                    String body = e.getMessage();
                    DriverInfoResponse driverInfoResponse = new DriverInfoResponse();
                    driverInfoResponse.setMessage(body);
                    Log.d(TAG, body + "b");
                    responseMutableLiveData.setValue(driverInfoResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("data");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
