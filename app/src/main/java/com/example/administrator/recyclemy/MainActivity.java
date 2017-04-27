package com.example.administrator.recyclemy;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    MyAdapter meiziAdapter;
    List<Meizi> meizis;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem ;
    private int page=1;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        initView();

        setListener();
        new GetData().execute("http://gank.io/api/data/福利/10/1");
    }

    public void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.line_swipe_refresh);
    }
    public void setListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //                0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                //               滑动状态停止并且剩余两个item时自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //                获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
    }

    private class GetData extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置加载视图出现
            swipeRefreshLayout.setRefreshing(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            if(meiziAdapter==null){
                meiziAdapter=new MyAdapter(MainActivity.this);
                recyclerView.setAdapter(meiziAdapter);
            }
            if(!TextUtils.isEmpty(result)){
                JSONObject jsonObject;
                Gson gson=new Gson();
                String jsonData=null;
                try{
                    jsonObject=new JSONObject(result);
                    jsonData=jsonObject.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    List<Meizi> more=gson.fromJson(jsonData,new TypeToken<List<Meizi>>(){}.getType());
                    meiziAdapter.setMeizis(more);

                    meiziAdapter.notifyDataSetChanged();
            }
            //加载视图消失
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
