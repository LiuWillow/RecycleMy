package com.example.administrator.recyclemy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/27.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Meizi> meizis;
    private Context mContext;
    public MyAdapter(Context context){
        mContext=context;

    }

    public void setMeizis(List<Meizi> list){
        if(meizis==null){
            meizis=list;
        }
        else {
            meizis.addAll(list);
            notifyDataSetChanged();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.meizi_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Picasso.with(mContext).load(meizis.get(position).getUrl()).into(((MyViewHolder)holder).imageView);
        ((MyViewHolder) holder).textView.setText(meizis.get(position).getDesc());
        ((MyViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DescribeActivity.class);
                intent.putExtra("meiziurl",meizis.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(meizis==null)
            return 0;
        return meizis.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.image);
            textView=(TextView)view.findViewById(R.id.dec_text);
        }
    }
}
