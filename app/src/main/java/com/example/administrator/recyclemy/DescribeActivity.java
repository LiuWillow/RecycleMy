package com.example.administrator.recyclemy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.path;

/**
 * Created by Administrator on 2017/4/27.
 */

public class DescribeActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap pic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.describe_layout);
        imageView=(ImageView)findViewById(R.id.describe_image);
        final String url=getIntent().getStringExtra("meiziurl");
        Picasso.with(this)
                .load(url)
                .into(imageView);


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO 保存图片到手机
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pic=getPic(url);
                        if(pic==null)
                        {
                            Log.d("DescribeActivity","url转换失败");
                        }
                        insert(DescribeActivity.this,pic);
                    }
                }).start();
                Toast.makeText(DescribeActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    public void insert(Context context, Bitmap bitmap){

        String state = Environment.getExternalStorageState();
//如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("OnlongClickListener","存储状态错误");
            return;
        }
        //获取外部存储的根路径加自定义的后缀
        File appdir=new File(Environment.getExternalStorageDirectory(),"LookRecyle");
        if(!appdir.exists()){
            appdir.mkdir();
        }
        String filename=System.currentTimeMillis()+".jpg";
        File file=new File(appdir,filename);
        try{
            FileOutputStream fos=new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path)));
    }


    public Bitmap getPic(String url){
        Bitmap bitmap=null;
        try{
            bitmap=Picasso.with(DescribeActivity.this)
                    .load(url)
                    .get();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
