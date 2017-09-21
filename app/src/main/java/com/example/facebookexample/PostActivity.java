package com.example.facebookexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.plus.PlusShare;

public class PostActivity extends AppCompatActivity{
    EditText tv_post;
    ShareButton fb_share_button;
    Button google_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        tv_post=(EditText)findViewById(R.id.tv_post);
        fb_share_button=(ShareButton) findViewById(R.id.fb_share_button);
        google_share=(Button) findViewById(R.id.google_share);


        postOnFacebook();

        google_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new PlusShare.Builder(PostActivity.this).setType("text/plain")
                        .setText("Welcome to the Google+ platform.")
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);



            }
        });
    }

   private void postOnFacebook(){
       String text=tv_post.getText().toString();
       ShareContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com"))
               .setQuote(text)
               .build();
       fb_share_button.setShareContent(content);
   }
}
