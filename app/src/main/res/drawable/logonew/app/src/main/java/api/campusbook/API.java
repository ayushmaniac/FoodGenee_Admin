package api.campusbook;

import java.util.ArrayList;
import java.util.List;


public interface API {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("loginotp")
    Call<LoginOtpResponse> loginOtp(

            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("signup")
    Call<SignupResponse> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String phone,
            @Field("referral") String ref
    );

    @FormUrlEncoded
    @POST("getstudentconnections")

    Call<List<ConnectionList>> connections(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("unfriendconnection")
    Call<ResponseBody> unfriend(

            @Field("studentid") Integer  studentid,
            @Field("friendid") String friendid
    );

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<ForgotPasswordResponse> forgotPassword(

            @Field("username") String  userinput

    );

    @FormUrlEncoded
    @POST("resetpassword")
    Call<ResponseBody> resetPassword(

            @Field("studentid") Integer studentid,
            @Field("password") String  password

    );

    @FormUrlEncoded
    @POST("signupmobile")
    Call<SignupOtpResponse> signupotp(

            @Field("mobile") String phone

    );

    @FormUrlEncoded
    @POST("loginotpverify")
    Call<LoginOtpVerifyResponse> otpVerifyRequest(

            @Field("mobile") String phone,
            @Field("otp") String otp

    );

    @POST("feedcategorieslist")
    Call<List<CateogoryList>> getfeedCateogory(
    );

    @POST("postcategorieslist")
    Call<List<CateogoryList>> getCateogory(
    );


    @FormUrlEncoded
    @POST("feedpage")
    Call<List<FeedList>> fetchFeed(
            @Field("studentid")Integer id,
            @Field("start") Integer val
    );

    @FormUrlEncoded
    @POST("studentschool")
    Call<SelfSchool> schooldetails(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("likepost")
    Call<ResponseBody> like(

            @Field("studentid") Integer  studentid,
            @Field("postid") Integer postid
    );


    @Multipart
    @POST("studentcreatepost")
    Call<ResponseBody> studentpost(

            @Part("studentid") Integer studentid,
            @Part("postcontent") String content,
            @Part("categoryarray[]") ArrayList<Integer> categoryid,
            @Part MultipartBody.Part postimage  //Values changed after SHA builds

    );

    @Multipart
    @POST("signup-step3")
    Call<ResponseBody> uploadProfilePic(

            @Part("studentid") Integer studentid,
            @Part MultipartBody.Part profilepic  //Values changed after SHA builds
    );

    @FormUrlEncoded
    @POST("connectionrequestlist")
    Call<List<RequestList>> friendrequests(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("opportunitieslist")
    Call<List<OpportunityList>> opportunities(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("connectionaccept")
    Call<ResponseBody> acceptRequest(

            @Field("studentid") Integer  studentid,
            @Field("friendid") Integer friendid
    );

    @FormUrlEncoded
    @POST("connectionreject")
    Call<ResponseBody> rejectRequest(

            @Field("studentid") Integer  studentid,
            @Field("friendid") Integer friendid
    );

    @FormUrlEncoded
    @POST("schoolpost")
    Call<List<SchoolFeedList>> schoolFeeds(

            @Field("studentid") Integer  studentid,
            @Field("schoolid") Integer schoolid
    );


    @FormUrlEncoded
    @POST("fcmpushid")
    Call<ResponseBody> fcm(

            @Field("studentid") Integer  studentid,
            @Field("push_id") String push_id
    );

    @FormUrlEncoded
    @POST("studentprofile")
    Call<FriendsAccountList> friendAc(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("getsearchresults")
    Call<List<SearchList>> search(
            @Field("studentid") Integer id,
            @Field("term") String keyword
    );

    @FormUrlEncoded
    @POST("studentpost")
    Call<List<FriendPost>> friendsPost(
            @Field("studentid") Integer id
    );



    @FormUrlEncoded
    @POST("getnotificationsresults")
    Call<List<NotificationList>> notifilist(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("categorypost")
    Call<List<FeedList>> catFeed(
            @Field("studentid")Integer studentid,
            @Field("category") Integer catid,
            @Field("start") Integer start
    );


    @FormUrlEncoded
    @POST("categoryselection")
    Call<ResponseBody> catSelect(
            @Field("studentid")Integer studentid,
            @Field("category[]") ArrayList<Integer> catid

    );

    @FormUrlEncoded
    @POST("reportspam")
    Call<ResponseBody> report(

            @Field("studentid") Integer  studentid,
            @Field("postid") Integer post_id
    );

    @FormUrlEncoded
    @POST("deletepost")
    Call<ResponseBody> delete(

            @Field("studentid") Integer  studentid,
            @Field("postid") Integer post_id
    );

    @FormUrlEncoded
    @POST("mypreferences")
    Call<List<CateogoryList>> myPref(

            @Field("studentid") Integer  studentid

    );

    @FormUrlEncoded
    @POST("wallet")
    Call<CashList> walletDetails(

            @Field("studentid") Integer  studentid
    );

    @FormUrlEncoded
    @POST("wallettransactions")
    Call<List<TransactionList>> walletTranscation(

            @Field("studentid") Integer  studentid
    );


    @FormUrlEncoded
    @POST("testing-session.php")
    Call<TestUrlList> testId(

            @Field("studentid") Integer  studentid,
            @Field("action") String testing
    );

    @FormUrlEncoded
    @POST("careersession.php")
    Call<TestUrlList> careersession(

            @Field("studentid") Integer  studentid
    );

}
