package network;

import com.admin.foodgenee.loginmodel.LoginModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FoodGenee {

    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<LoginModel> getUser(
            @Field("action") String action,
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<ResponseBody> sendStatus(
            @Field("action") String action,
            @Field("usersid") String userid,
            @Field("loginstatus") String status,
            @Field("pushid") String pushid
    );
}
