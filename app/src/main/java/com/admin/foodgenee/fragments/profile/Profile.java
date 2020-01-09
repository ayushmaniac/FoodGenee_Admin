package com.admin.foodgenee.fragments.profile;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.profile.profilemodel.ChangePasswordModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UpdateModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UpdatePhotoModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UserModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import network.FoodGenee;
import network.RetrofitClient;
import network.ScalarClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    Button logoutButton;
    SessionManager sessionManager;
    String userId;
    EditText name, email, mobile;
    CircleImageView profilePic;
    TextView onTopNamel, changePassword;
    CardView hiddenLayout;
    Button submitChangeDetails, submitNewPassword;
    Dialog loadingDialog;
    EditText newPassword, conFirmPassword;
    private Uri uri;
    private static final int READ_REQUEST_CODE = 300;
    public static String SERVER_PATH = "http://sansdigitals.com/phpdemos/foodgenee/api/";


    private static final int REQUEST_GALLERY_CODE = 200;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        checkUserPermission();
        logoutButton = view.findViewById(R.id.logoutButtonProfile);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> getUrl = sessionManager.getUserDetail();
        userId = getUrl.get(sessionManager.USER_ID);
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        getServiceBoy();
        logoutButton.setOnClickListener(v -> {

            sessionManager.logout();
        });
        changePassword.setOnClickListener(v -> {

            hiddenLayout.setVisibility(View.VISIBLE);

        });

        submitChangeDetails.setOnClickListener(v -> {

            getValuesAndThrowUpdateCall();

        });

        submitNewPassword.setOnClickListener(v -> throwCallsForPasswordUpdation());

        profilePic.setOnClickListener(v -> {

            uploadProfilePicture();

        });

        return view;

    }

    private void checkUserPermission() {
    }

    private void uploadProfilePicture() {
        selectPicture();

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }
        private void selectPicture() {

        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, Profile.this);


    }


    private void throwCallsForPasswordUpdation() {
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);


        String uPassword = newPassword.getText().toString();
        String nuPassword = conFirmPassword.getText().toString();

        if(uPassword.equals(nuPassword)){

        Call<ChangePasswordModel> httpCall = foodGenee.changePassword("changepassword",userId,uPassword);
        httpCall.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {

                try{

                    if(response.body().getStatus().equals("0")){
                        Toast.makeText(getContext(), response.body().getText(), Toast.LENGTH_SHORT).show();

                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                        hiddenLayout.setVisibility(View.GONE);

                    }
                    else if(response.body().getStatus().equals("1")){

                        Toast.makeText(getContext(), response.body().getText(), Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                        hiddenLayout.setVisibility(View.GONE);


                    }

                }
                catch (Exception e){

                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                    hiddenLayout.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
                hiddenLayout.setVisibility(View.GONE);


            }
        });


        }
        else {

            Toast.makeText(getContext(), "Password not same, Please write same password and confirm it", Toast.LENGTH_SHORT).show();
            loadingDialog.cancel();
            loadingDialog.dismiss();
        }

    }

    private void getValuesAndThrowUpdateCall() {

        String uName = name.getText().toString().trim();
        String uEmail = email.getText().toString().trim();
        String uMobile = mobile.getText().toString().trim();


        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<UpdateModel> call = foodGenee.updateUser("updation", userId, uName,uEmail,uMobile);
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                try{
                    UpdateModel updateModel = response.body();
                    if(updateModel.getStatus().equals("1")){

                        Toast.makeText(getContext(), updateModel.getText(), Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();


                    }
                    else if(updateModel.getStatus().equals("0")){
                        Toast.makeText(getContext(), updateModel.getText(), Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }

                }
                catch (Exception e){

                    Toast.makeText(getContext(), "Some error occured. Try again later", Toast.LENGTH_SHORT).show();

                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {

                Toast.makeText(getContext(), "Some error occured. Try again later", Toast.LENGTH_SHORT).show();
                loadingDialog.cancel();
                loadingDialog.dismiss();


            }
        });


    }

    private void initViews(View view) {
        name = view.findViewById(R.id.editName);
        email = view.findViewById(R.id.editEmail);
        mobile = view.findViewById(R.id.editMobile);
        onTopNamel = view.findViewById(R.id.serviceBoyName);
        submitChangeDetails = view.findViewById(R.id.subimitUpdate);
        submitNewPassword = view.findViewById(R.id.submitNewPassword);
        profilePic = view.findViewById(R.id.boyImage);
        changePassword = view.findViewById(R.id.changePasswordText);
        hiddenLayout = view.findViewById(R.id.cardTwo);
        newPassword = view.findViewById(R.id.updatePassword);
        conFirmPassword = view.findViewById(R.id.confirmPassword);

    }

    private void getServiceBoy() {

        FoodGenee foodGenee = RetrofitClient.getApiClient().create(FoodGenee.class);
        Call<UserModel> call = foodGenee.getUserDetails("serviceboy",userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                try{

                    UserModel  userModel = response.body();
                    if(userModel.getStatus().equals("0")){

                    loadingDialog.dismiss();
                    loadingDialog.cancel();

                    }
                    else if(userModel.getStatus().equals("1")){
                        onTopNamel.setText(userModel.getUsers().getName());
                        name.setText(userModel.getUsers().getName());
                        email.setText(userModel.getUsers().getEmail());
                        mobile.setText(userModel.getUsers().getMobile());

                        Glide.with(getContext())
                                .load(userModel.getUsers().getProfilepic())
                                .into(profilePic);
                        loadingDialog.dismiss();
                        loadingDialog.cancel();



                    }

                }
                catch (Exception e){
                    loadingDialog.dismiss();
                    loadingDialog.cancel();


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                loadingDialog.dismiss();
                loadingDialog.cancel();

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){

            loadingDialog = new Dialog(getContext());
            loadingDialog.setContentView(R.layout.loading_dialog);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            uri = data.getData();
            if(EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, getActivity());
                File file = new File(filePath);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                String action = "com/foodgeene/updateprofile";
                action.trim();
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profilepic", file.getName(), mFile);

                FoodGenee uploadImage = ScalarClient.getApiClient().create(FoodGenee.class);
                Call<ResponseBody> call = uploadImage.updatePic("updateprofilepic", userId, fileToUpload);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                    }
                });
            }else{
            }
        }


    }

}
