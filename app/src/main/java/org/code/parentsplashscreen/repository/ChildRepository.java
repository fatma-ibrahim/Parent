package org.code.parentsplashscreen.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.code.parentsplashscreen.network.ApiClient;
import org.code.parentsplashscreen.network.ApiService;
import org.code.parentsplashscreen.responses.ChildResponse;
import org.code.parentsplashscreen.responses.ChildrensResponse;
import org.code.parentsplashscreen.responses.UpdateChildStatusResponse;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ChildRepository {
    private static final String TAG = "ChildRepository";
    private ApiService apiService;

    public ChildRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<ChildResponse> createChild(String childName, int childAge, String childGender, String childClass, String image_path, String token) {
        MutableLiveData<ChildResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<ChildResponse> single = apiService.createChild(childName, childAge, childGender, childClass, image_path, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ChildResponse> singleObserver = new SingleObserver<ChildResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(ChildResponse childResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(childResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    Log.d(TAG, error);
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage(error);
                    responseMutableLiveData.setValue(childResponse);
                } else {
                    String bodyq = e.getMessage();
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage(bodyq);
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(childResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    public LiveData<ChildrensResponse> getAllChildren(String token) {
        MutableLiveData<ChildrensResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<ChildrensResponse> single = apiService.getAllChildren(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ChildrensResponse> singleObserver = new SingleObserver<ChildrensResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(ChildrensResponse childrensResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(childrensResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    Log.d(TAG, error + "-- http error --");
                    ChildrensResponse childrensResponse = new ChildrensResponse();
                    childrensResponse.setMessage(error);
                    responseMutableLiveData.setValue(childrensResponse);
                } else {
                    String bodyq = e.getMessage();
                    Log.d(TAG, bodyq + "-- Update --");
                    ChildrensResponse childrensResponse = new ChildrensResponse();
                    childrensResponse.setMessage(bodyq);
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(childrensResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    public LiveData<UpdateChildStatusResponse> updateChildStatus(int id, String token) {
        MutableLiveData<UpdateChildStatusResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<UpdateChildStatusResponse> single = apiService.updateChildStatus(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<UpdateChildStatusResponse> singleObserver = new SingleObserver<UpdateChildStatusResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(UpdateChildStatusResponse updateChildStatusResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(updateChildStatusResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    Log.d(TAG, error);
                    UpdateChildStatusResponse updateChildStatusResponse = new UpdateChildStatusResponse();
                    updateChildStatusResponse.setMessage(error);
                    responseMutableLiveData.setValue(updateChildStatusResponse);
                } else {
                    String bodyq = e.getMessage();
                    UpdateChildStatusResponse updateChildStatusResponse = new UpdateChildStatusResponse();
                    updateChildStatusResponse.setMessage("Failed!, Check Your Internet Connection!");
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(updateChildStatusResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }

    public LiveData<ChildResponse> updateChild(int id, String childName, int childAge, String childGender, String childClass, String image_path, String token) {
        MutableLiveData<ChildResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<ChildResponse> single = apiService.updateChild(id, childName, childAge, childGender, childClass, image_path, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ChildResponse> singleObserver = new SingleObserver<ChildResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(ChildResponse childResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(childResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage(error);
                    responseMutableLiveData.setValue(childResponse);
                } else {
                    String bodyq = e.getMessage();
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage("Failed!, Check Your Internet Connection!");
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(childResponse);

                }
            }
        };
        single.subscribe(singleObserver);
        return responseMutableLiveData;
    }


    public LiveData<ChildResponse> deleteChild(int id, String token) {
        MutableLiveData<ChildResponse> responseMutableLiveData = new MutableLiveData<>();
        Single<ChildResponse> single = apiService.deleteChild(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        SingleObserver<ChildResponse> singleObserver = new SingleObserver<ChildResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(ChildResponse childResponse) {
                Log.d(TAG, "onSuccess Update");
                responseMutableLiveData.setValue(childResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "-- Update --");
                if (e instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                    String error = getErrorMessage(responseBody);
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage(error);
                    responseMutableLiveData.setValue(childResponse);
                } else {
                    String bodyq = e.getMessage();
                    ChildResponse childResponse = new ChildResponse();
                    childResponse.setMessage("Failed!, Check Your Internet Connection!");
                    Log.d(TAG, bodyq);
                    responseMutableLiveData.setValue(childResponse);

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
