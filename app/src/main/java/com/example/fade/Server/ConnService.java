package com.example.fade.Server;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ConnService {


    //혜림
//    public static final String URL = "http://192.168.219.103:5000";
    //민정
    public static final String URL = "http://192.168.0.10:5000";
    //다빈
    //public static final String URL = "http://192.168.25.41:3157";


    @GET("/db/GetPerson/{uid}")
    Call<PersonData> getPersonData(@Path("uid") String uid);

    @PUT("/Login/{uid}")
    Call<ResponseBody> putRegisterUser(@Path("uid") String uid);

    @FormUrlEncoded
    @POST("/db/upload/{uid}")
    Call<ResponseBody> postDB(@Path("uid") String uid, @FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/gallery/upload/{uid}")
    Call<ResponseBody> postGalleryImg(@Path("uid") String uid, @FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/reg/person")
    Call<ReturnData> postRegisterPerson(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/reg/group")
    Call<ResponseBody> postRegisterGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/det/{uid}")
    Call<ResponseBody> postDetectionPicture(@Path("uid") String uid, @FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/edit/group")
    Call<ResponseBody> postEditGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/delete/group")
    Call<ResponseBody> DeleteGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/delete/person")
    Call<ResponseBody> DeletePerson(@FieldMap HashMap<String, Object> param);

}




