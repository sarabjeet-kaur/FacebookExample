package com.example.facebookexample;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager;
    TextView tv_facebook,txt_name;
    LinearLayout ll_facebook,ll_google,ll_twitter;
    String str_facebookname, str_facebookemail, str_facebookid, str_birthday, str_location,str_gender,str_facebookimage;
    Button btn_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();
        getKeyHash();
        listener();
    }


    private void init() {
        tv_facebook=(TextView)findViewById(R.id.tv_facebook);
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_name.setText("Social Media Corner");
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_logout.setVisibility(View.GONE);
        ll_facebook = (LinearLayout) findViewById(R.id.ll_facebook);
        ll_google = (LinearLayout) findViewById(R.id.ll_google);
        ll_twitter = (LinearLayout) findViewById(R.id.ll_twitter);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    private void listener() {
        ll_facebook.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        ll_twitter.setOnClickListener(this);

    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                   try{
                                       str_facebookname = object.getString("name");
                                   }catch (Exception e) {
                                       str_facebookname = "";
                                       e.printStackTrace();
                                   }

                                    try {
                                        str_facebookemail = object.getString("email");
                                    } catch (Exception e) {
                                        str_facebookemail = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        str_facebookid = object.getString("id");
                                    } catch (Exception e) {
                                        str_facebookid = "";
                                        e.printStackTrace();

                                    }

                                    try {
                                        str_birthday = object.getString("birthday");
                                    } catch (Exception e) {
                                        str_birthday = "";
                                        e.printStackTrace();
                                    }
                                    try {
                                        str_gender = object.getString("gender");
                                    } catch (Exception e) {
                                        str_birthday = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        JSONObject jsonobject_location = object.getJSONObject("location");
                                        str_location = jsonobject_location.getString("name");

                                    } catch (Exception e) {
                                        str_location = "";
                                        e.printStackTrace();
                                    }

                                    fn_profilepic();

                                } catch (Exception e) {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email,gender,birthday,location");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email,user_birthday,user_location"));
                        facebookLogin();
                    }
                }).executeAsync();
            }
            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void fn_profilepic() {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "me/picture", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.e("Response 2", response + "");

                        try {
                            str_facebookimage = (String) response.getJSONObject().getJSONObject("data").get("url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                        intent.putExtra("tag","Facebook");
                        intent.putExtra("image",str_facebookimage);
                        intent.putExtra("name",str_facebookname);
                        intent.putExtra("email",str_facebookemail);
                        intent.putExtra("id",str_facebookid);
                        intent.putExtra("gender",str_gender);
                        intent.putExtra("dob",str_birthday);
                      startActivity(intent);

                    }
                }
        ).executeAsync();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_facebook:
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email,user_birthday,user_location"));
                facebookLogin();
                break;
            case  R.id.ll_google:
                startActivity(new Intent(MainActivity.this,GooglePlusActivity.class));
                break;
            case R.id.ll_twitter:
                startActivity(new Intent(MainActivity.this,TwitterActivity.class));
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
