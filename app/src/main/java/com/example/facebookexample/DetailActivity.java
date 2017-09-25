package com.example.facebookexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.TwitterCore;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_image;
    TextView tv_name, tv_email, tv_id, tv_dob, tv_gender, txt_name;
    String str_name, str_email, str_id, str_birthday, str_gender, str_image, str_tag;
    Button btn_post, btn_logout;
    long twitter_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        clickListener();
        gettingIntent();
        setData();

    }


    //Initialise the view
    private void initView() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        txt_name = (TextView) findViewById(R.id.txt_name);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_logout = (Button) findViewById(R.id.btn_logout);

    }

    //method for click listener
    private void clickListener() {
        btn_post.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }


    //method for getting intent
    private void gettingIntent() {
        str_birthday = getIntent().getStringExtra("dob");
        str_email = getIntent().getStringExtra("email");
        str_id = getIntent().getStringExtra("id");
        twitter_id = getIntent().getLongExtra("id", 0);
        str_image = getIntent().getStringExtra("image");
        str_name = getIntent().getStringExtra("name");
        str_gender = getIntent().getStringExtra("gender");
        str_tag = getIntent().getStringExtra("tag");
    }


    //method for set data to view
    private void setData() {
        Glide.with(DetailActivity.this).load(str_image).skipMemoryCache(true).into(iv_image);
        tv_dob.setText(str_birthday);
        tv_name.setText(str_name);
        tv_email.setText(str_email);
        tv_gender.setText(str_gender);
        txt_name.setText(str_tag);
        if (str_tag.equals("Facebook") || str_tag.equals("Google+")) {
            tv_id.setText(str_id);
        } else {
            tv_id.setText(twitter_id + "");
        }

    }


    //click for logout and post
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (str_tag.equals("Facebook")) {
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                } else if (str_tag.equals("Google+")) {
                    Log.e("in detail", "sign out");
                    GooglePlusActivity.listeners.googleSignout(DetailActivity.this);
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                } else if (str_tag.equals("Twitter")) {
                    Log.e("in detail", "sign out twitter");
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                }
                break;
            case R.id.btn_post:
                Intent intent = new Intent(DetailActivity.this, PostActivity.class);
                intent.putExtra("tag", str_tag);
                startActivity(intent);
        }
    }
}
