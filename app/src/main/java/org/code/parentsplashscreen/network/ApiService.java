package org.code.parentsplashscreen.network;

import org.code.parentsplashscreen.responses.ChildResponse;
import org.code.parentsplashscreen.responses.ChildrensResponse;
import org.code.parentsplashscreen.responses.DriverInfoResponse;
import org.code.parentsplashscreen.responses.LoginResponse;
import org.code.parentsplashscreen.responses.RegisterResponse;
import org.code.parentsplashscreen.responses.ShowTripResponse;
import org.code.parentsplashscreen.responses.ShowUserResponse;
import org.code.parentsplashscreen.responses.UpdateChildStatusResponse;
import org.code.parentsplashscreen.responses.UpdateResponse;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/father/register")
    Single<RegisterResponse> createUser(@Field("name") String fatherName,
                                        @Field("email") String email,
                                        @Field("password") String password,
                                        @Field("password_confirmation") String passwordConfirmation,
                                        @Field("mobileNumber") String mobileNumber,
                                        @Field("code") String code,
                                        @Field("lit") Double latitude,
                                        @Field("lng") Double longitude,
                                        @Field("region") String region);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/father/login")
    Single<LoginResponse> loginUser(@Field("email") String email,
                                    @Field("password") String password);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/father/update")
    Single<UpdateResponse> updateUser(@Field("name") String fatherName,
                                      @Field("email") String email,
                                      @Field("mobileNumber") String mobileNumber,
                                      @Field("lit") Double latitude,
                                      @Field("lng") Double longitude,
                                      @Field("region") String region,
                                      @Field("image_path") String image_path,
                                      @Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/father/show")
    Single<ShowUserResponse> showUser(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/father/showBusDriver")
    Single<DriverInfoResponse> DriverInfo(@Header("Authorization") String token);


    /**
     * Child Operation
     **/
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("api/child/create")
    Single<ChildResponse> createChild(@Field("name") String childName,
                                      @Field("age") int childAge,
                                      @Field("gender") String childGender,
                                      @Field("class") String childClass,
                                      @Field("image_path") String image_path,
                                      @Header("Authorization") String token
    );

    @Headers("Accept: application/json")
    @GET("api/childrens")
    Single<ChildrensResponse> getAllChildren(@Header("Authorization") String token);


    @Headers("Accept: application/json")
    @PUT("api/child/updateStatus/{id}")
    Single<UpdateChildStatusResponse> updateChildStatus(@Path("id") int id, @Header("Authorization") String token);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("api/child/update/{id}")
    Single<ChildResponse> updateChild(
            @Path("id") int id,
            @Field("name") String childName,
            @Field("age") int childAge,
            @Field("gender") String childGender,
            @Field("class") String childClass,
            @Field("image_path") String image_path,
            @Header("Authorization") String token);

    @Headers("Accept: application/json")
    @DELETE("api/child/delete/{id}")
    Single<ChildResponse> deleteChild(@Path("id") int id, @Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/father/showTrip")
    Single<ShowTripResponse> showTrip(@Header("Authorization") String token);


    @Headers("Accept: application/json")
    @POST("api/father/show")
    Single<ShowUserResponse> showFather(@Header("Authorization") String token);

}
