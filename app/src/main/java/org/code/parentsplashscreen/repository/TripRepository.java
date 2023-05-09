package org.code.parentsplashscreen.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.code.parentsplashscreen.network.ApiClient;
import org.code.parentsplashscreen.network.ApiService;
import org.code.parentsplashscreen.responses.ShowTripResponse;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class TripRepository {
    private static final String TAG = "AuthRepository";
    private ApiService apiService;

    public TripRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }


    public LiveData<ShowTripResponse> showTrip(String token) {
        MutableLiveData<ShowTripResponse> mutableLiveData = new MutableLiveData<>();
        Single<ShowTripResponse> single = apiService.showTrip(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ShowTripResponse> observer = new SingleObserver<ShowTripResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe-SchoolArrived");
            }

            @Override
            public void onSuccess(ShowTripResponse showTripResponse) {
                Log.d(TAG, "( onSuccess )-SchoolArrived");
                mutableLiveData.setValue(showTripResponse);
            }


            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    Log.d(TAG, error);

                    ShowTripResponse showTripResponse = new ShowTripResponse();
                    showTripResponse.setData(error);
                    mutableLiveData.setValue(showTripResponse);
                } else {
                    String body = e.getMessage();
                    ShowTripResponse showTripResponse = new ShowTripResponse();
                    showTripResponse.setData(body);
                    Log.d(TAG, body);
                    mutableLiveData.setValue(showTripResponse);

                }
            }
        };
        single.subscribe(observer);
        return mutableLiveData;
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
