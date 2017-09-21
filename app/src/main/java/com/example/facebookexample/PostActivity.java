package com.example.facebookexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.plus.PlusShare;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    EditText tv_post;
    ShareButton fb_share_button;
    Button google_share;
    TextView txt_name;
    Button btn_logout;
    String str_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        tv_post=(EditText)findViewById(R.id.tv_post);
        txt_name = (TextView) findViewById(R.id.txt_name);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        fb_share_button=(ShareButton) findViewById(R.id.fb_share_button);
        google_share=(Button) findViewById(R.id.google_share);

        str_tag=getIntent().getStringExtra("tag");
        txt_name.setText(str_tag);

        postOnFacebook();

        google_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=tv_post.getText().toString();
                Intent shareIntent = new PlusShare.Builder(PostActivity.this)
                        .setType("text/plain")
                        .setText(text)
                        .getIntent();
                startActivityForResult(shareIntent, 0);
            }
        });

        if(str_tag.equals("Facebook")){
            fb_share_button.setVisibility(View.VISIBLE);
        }
        else if(str_tag.equals("Google+")) {

        }
    }

   private void postOnFacebook(){
       String text=tv_post.getText().toString();
       Log.e("text for facebbok",text);
       ShareContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com"))
               .setQuote(text)
               .setContentDescription(text)
               .build();
       fb_share_button.setShareContent(content);
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                if(str_tag.equals("Facebook")){

                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }else if(str_tag.equals("Google+")){
                    try{

//                        if (GooglePlusActivity.mGoogleApiClient.isConnected()){
//                            Auth.GoogleSignInApi.signOut(GooglePlusActivity.mGoogleApiClient);
//                            Log.e("google client in detail",GooglePlusActivity.mGoogleApiClient+"");
//                        }
                        GooglePlusActivity.listeners.googleSignout(PostActivity.this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }

                break;

        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Logout", Toast.LENGTH_SHORT).show();
    }
}
