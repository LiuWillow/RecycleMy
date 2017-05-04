package com.example.administrator.recyclemy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class MeiziFragment extends Fragment {
    private static RecyclerView recyclerView;
    private MyAdapter meiziAdapter;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem ;
    private int page=1;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.meizi_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        setListener();
        new GetData().execute("http://gank.io/api/data/福利/10/1");
        super.onViewCreated(view, savedInstanceState);
    }
    public void initView(View view){
        recyclerView=(RecyclerView)view.findViewById(R.id.recycle);
        mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.line_swipe_refresh);
    }
    public void setListener(){

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
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
    private class GetData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置加载视图出现
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
        }
        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            if(meiziAdapter==null){
                meiziAdapter=new MyAdapter(getContext());
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
            }
            //加载视图消失
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
