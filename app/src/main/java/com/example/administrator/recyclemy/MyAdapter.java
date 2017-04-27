package com.example.administrator.recyclemy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
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
    public List<Meizi> getMeizis(){
        return meizis;
    }
    public void setMeizis(List<Meizi> list){
        if(meizis==null||meizis.size()==0){
            meizis=list;
        }
        else {
            meizis.addAll(list);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.meizi_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Picasso.with(mContext).load(meizis.get(position).getUrl()).into(((MyViewHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return meizis.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.image);
        }
    }
}
