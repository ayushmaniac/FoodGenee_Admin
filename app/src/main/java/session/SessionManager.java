package session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.admin.foodgenee.Login;
import com.admin.foodgenee.loginmodel.LoginModel;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";
    public static final String WEB = "WEB_URL";

    private static final String SHARED_PREF_NAME = "my_shared_preff";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user_id, String webUrl){

        editor.putBoolean(LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.putString(WEB, webUrl);

        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }



    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(WEB, sharedPreferences.getString(WEB, null));

        return user;
    }


    public void saveUserId(LoginModel registerModel){

        editor.putString("userid", registerModel.getUsersid());
        editor.putString("getText", registerModel.getText());
        editor.putString("getStatus", registerModel.getStatus());

    }


    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}