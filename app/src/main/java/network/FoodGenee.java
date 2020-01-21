package network;

import com.admin.foodgenee.forgotpassword.model.ForgotPasswordModel;
import com.admin.foodgenee.forgotpassword.model.VerifyModel;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.AcceptedModel;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.acceptedmodel.DeliverModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.AcceptModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.NewOrderModel;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.newordermodel.RejectModel;
import com.admin.foodgenee.fragments.orders.OrdersModel.OrdersModel;
import com.admin.foodgenee.fragments.profile.profilemodel.ChangePasswordModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UpdateModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UserModel;
import com.admin.foodgenee.loginmodel.LoginModel;
import com.admin.foodgenee.loginmodel.LoginStatusModel;
import com.admin.foodgenee.orderdetails.model.OrderDetailsModel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<NewOrderModel> getNewOrders(
            @Field("action") String action,
            @Field("usersid") String usersid
    );


    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<AcceptedModel> getAcceptedOrders(
            @Field("action") String action,
            @Field("usersid") String usersid
    );

    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<AcceptModel> acceptNewOrder(
            @Field("action") String action,
            @Field("orderid") String orderid,
            @Field("usersid") String usersid,
            @Field("preparetime") Integer preparetime
    );


    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<RejectModel> rejecttNewOrder(
            @Field("action") String action,
            @Field("orderid") String orderid,
            @Field("usersid") String usersid
    );


    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<DeliverModel> deliverOrder(
            @Field("action") String action,
            @Field("orderid") String orderid,
            @Field("usersid") String usersid
    );

    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<OrdersModel> ordersByDate(
            @Field("action") String action,
            @Field("orderdate") String orderdate,
            @Field("usersid") String usersid
    );

    @FormUrlEncoded
    @POST("serviceboy/orders.php")
    Call<OrderDetailsModel> orderDetails(
            @Field("action") String action,
            @Field("orderid") String orderdate,
            @Field("usersid") String usersid
    );


    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<UserModel> getUserDetails(
            @Field("action") String action,
            @Field("usersid") String usersid
    );


    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<UpdateModel> updateUser(
            @Field("action") String action,
            @Field("usersid") String usersid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile

            );


    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<ChangePasswordModel> changePassword(
            @Field("action") String action,
            @Field("usersid") String usersid,
            @Field("password") String password


    );

    @Multipart
    @POST("serviceboy/boy-registration.php")
    Call<ResponseBody> updatePic(
            @Part("action") String action,
            @Part("usersid") String usersid,
            @Part MultipartBody.Part profilepic
    );

    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<ForgotPasswordModel> forgotPassword(
            @Field("action") String action,
            @Field("username") String username
    );
    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<VerifyModel> verifyOtp(
            @Field("action") String action,
            @Field("usersid") String usersid,
            @Field("otp") String otp

            );


    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<com.admin.foodgenee.forgotpassword.model.ChangePasswordModel> changePasswordSplash(
            @Field("action") String action,
            @Field("usersid") String usersid,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("serviceboy/boy-registration.php")
    Call<LoginStatusModel> checkLoginStatus(
            @Field("action") String action,
            @Field("usersid") String usersid
    );

}

