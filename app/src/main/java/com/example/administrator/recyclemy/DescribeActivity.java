package com.example.administrator.recyclemy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/4/27.
 */

public class DescribeActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.describe_layout);
        imageView=(ImageView)findViewById(R.id.describe_image);
        String url=getIntent().getStringExtra("meiziurl");
        Picasso.with(this).load(url).into(imageView);
    }
}
