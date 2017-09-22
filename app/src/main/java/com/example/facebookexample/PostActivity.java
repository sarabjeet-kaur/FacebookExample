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

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tv_post;
    private ShareButton fb_share_button;
    private TextView txt_name;
    private Button btn_logout, twitter_share, google_share;
    private TextView facebook_share;
    private String str_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        initView();


        //getting intent and set tag
        str_tag = getIntent().getStringExtra("tag");
        txt_name.setText(str_tag);

        //Google Sharing
        google_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv_post.getText().toString();
                Intent shareIntent = new PlusShare.Builder(PostActivity.this)
                        .setType("text/plain")
                        .setText(text)
                        .getIntent();
                startActivityForResult(shareIntent, 0);
            }
        });

        //Twitter Sharing
        twitter_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv_post.getText().toString();
                TweetComposer.Builder builder = new TweetComposer.Builder(PostActivity.this)
                        .text(text);
                builder.show();
            }
        });

        //Facebook Sharing
        facebook_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOnFacebook();
                fb_share_button.performClick();
            }
        });

        //Enable button according to tag
        if (str_tag.equals("Facebook")) {
            facebook_share.setVisibility(View.VISIBLE);
        } else if (str_tag.equals("Google+")) {
            google_share.setVisibility(View.VISIBLE);

        } else if (str_tag.equals("Twitter")) {
            twitter_share.setVisibility(View.VISIBLE);

        }
    }

    //initialise view
    private void initView() {

        tv_post = (EditText) findViewById(R.id.tv_post);
        txt_name = (TextView) findViewById(R.id.txt_name);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        twitter_share = (Button) findViewById(R.id.twitter_share);
        facebook_share = (TextView) findViewById(R.id.facebook_share);
        btn_logout.setOnClickListener(this);
        fb_share_button = (ShareButton) findViewById(R.id.fb_share_button);
        google_share = (Button) findViewById(R.id.google_share);
    }

    //method to post data on facebook
    private void postOnFacebook() {
        String text = tv_post.getText().toString();
        Log.e("text for facebbok", text);
        ShareContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com"))
                .setQuote(text)
                .build();
        fb_share_button.setShareContent(content);
    }

    //click for logout
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (str_tag.equals("Facebook")) {

                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                } else if (str_tag.equals("Google+")) {
                    GooglePlusActivity.listeners.googleSignout(PostActivity.this);
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                } else if (str_tag.equals("Twitter")) {
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
                break;
        }

    }
}
