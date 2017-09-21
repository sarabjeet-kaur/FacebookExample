package com.example.facebookexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_image;
    TextView tv_name, tv_email, tv_id,tv_dob, tv_gender,txt_name;
    String str_name, str_email, str_id, str_birthday,str_gender,str_image,str_tag;
    Button btn_post,btn_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        txt_name = (TextView) findViewById(R.id.txt_name);
        btn_post=(Button)findViewById(R.id.btn_post);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_post.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


        str_birthday=getIntent().getStringExtra("dob");
        str_email=getIntent().getStringExtra("email");
        str_id=getIntent().getStringExtra("id");
        str_image=getIntent().getStringExtra("image");
        str_name=getIntent().getStringExtra("name");
        str_gender=getIntent().getStringExtra("gender");
        str_tag=getIntent().getStringExtra("tag");

        Glide.with(DetailActivity.this).load(str_image).skipMemoryCache(true).into(iv_image);
        tv_dob.setText(str_birthday);
        tv_name.setText(str_name);
        tv_email.setText(str_email);
        tv_id.setText(str_id);
        tv_gender.setText(str_gender);
        txt_name.setText(str_tag);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                if(str_tag.equals("Facebook")){
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(DetailActivity.this,MainActivity.class));
                }else if(str_tag.equals("Google+")){
                    Log.e("in detail","sign out");
                    GooglePlusActivity.listeners.googleSignout(DetailActivity.this);
                    startActivity(new Intent(DetailActivity.this,MainActivity.class));
                }
                break;
            case R.id.btn_post:
                Intent intent=new Intent(DetailActivity.this,PostActivity.class);
                intent.putExtra("tag",str_tag);
                startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Logout To proceed", Toast.LENGTH_SHORT).show();
    }
}
