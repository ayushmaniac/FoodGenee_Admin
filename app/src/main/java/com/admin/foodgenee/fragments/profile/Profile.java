package com.admin.foodgenee.fragments.profile;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.foodgenee.FileUtils;
import com.admin.foodgenee.R;
import com.admin.foodgenee.Root;
import com.admin.foodgenee.fragments.profile.profilemodel.ChangePasswordModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UpdateModel;
import com.admin.foodgenee.fragments.profile.profilemodel.UserModel;
import com.bumptech.glide.Glide;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import network.FoodGenee;
import network.RetrofitClient;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements Root.ActivityResultListener {

    Button logoutButton;
    SessionManager sessionManager;
    String userId;
    EditText name, email, mobile,editLocation;
    CircleImageView profilePic;
    TextView onTopNamel, changePassword;
    CardView hiddenLayout;
    Button submitChangeDetails, submitNewPassword;
    Dialog loadingDialog;
    EditText newPassword, conFirmPassword;
    Uri imageUri;
    private static final  int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    public static String SERVER_PATH = "http://sansdigitals.com/phpdemos/foodgenee/api/";


    private static final int REQUEST_GALLERY_CODE = 200;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((Root)getActivity()).setActivityResultListener(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Root)getActivity()).removeActivityResultListener();

    }

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

            changeProfilePic();
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
        editLocation=view.findViewById(R.id.editLocation);
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
                        editLocation.setText(userModel.getUsers().getStorename());

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

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {

            loadingDialog = new Dialog(getActivity());
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(true);
            uri = data.getData();
            if(EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = com.admin.foodgenee.FileUtils.getPath(getActivity(), uri);
                Log.e("filePath",filePath);

              //  uploadMultipart(filePath);
                uploadMultipart(decodeFile(filePath,600,600));


            }else{
                EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if(requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {


            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profilePic.setImageBitmap(bitmap);
                String path = FileUtils.getPath(getActivity(), imageUri);
                loadingDialog = new Dialog(getActivity());
                loadingDialog.setContentView(R.layout.loading_dialog_layout);
                loadingDialog.show();
                loadingDialog.setCancelable(true);
                loadingDialog.setCanceledOnTouchOutside(true);
               // uploadMultipart(path);
                uploadMultipart(decodeFile(path,600,600));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

           /* Bitmap photo = (Bitmap) data.getExtras().get("data");

            Uri imageUri = FileUtils.getImageUri(UpdateProfile.this, photo);
            changePic.setImageBitmap(photo);*/





        }


    }


    public void uploadMultipart(String path) {

        if (path == null) {


        } else {


            try {
                new MultipartUploadRequest(getActivity(), "http://sansdigitals.com/phpdemos/foodgenee/api/serviceboy/boy-profilepic.php")
                        .setMethod("POST")
                        .addFileToUpload(path, "profilepic")
                        .addParameter("userid",userId)
                        .setMaxRetries(2)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                Log.e("error","errir");
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                Log.e("error","errir");
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                Log.d("Profile", "onCompleted: "+serverResponse.getBodyAsString());

                                loadingDialog.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());

                                    String status = jsonObject.getString("status");
                                    if (status.equals("1")) {
                                        String profilepic = jsonObject.getString("profilepic");

                                        Glide.with(getActivity())
                                                .load(profilepic)
                                                .into(profilePic);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void myResult(int requestCode, int resultCode, Intent data) {

    }


    private void changeProfilePic() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    checkPermission();

                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);*/
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    selectPicture();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


        // selectPicture();

    }

    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(
                getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(),Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                PERMISSIONS_MULTIPLE_REQUEST
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSIONS_MULTIPLE_REQUEST
                );
            }
        }else {
            // Do something, when permissions are already granted
            //Toast.makeText(UpdateProfile.this,"Permissions already granted",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_MULTIPLE_REQUEST:{
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);

                }else {
                    // Permissions are denied
                    Toast.makeText(getActivity(),"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }


        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());


    }
    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }



}
