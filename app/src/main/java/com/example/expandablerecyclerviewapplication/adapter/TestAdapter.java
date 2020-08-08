package com.example.expandablerecyclerviewapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandablerecyclerviewapplication.R;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<String> list;
    private onAdapterItemClickListener itemClickListener;
    public TestAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_text,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_content.setText(list.get(position));
        holder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_content,tv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_delete=itemView.findViewById(R.id.tv_delete);
        }
    }


    public void reFreshData(List<String> list){
        this.list=list;
        notifyDataSetChanged();
    }


    public interface onAdapterItemClickListener{
        void onItemClick(View view,int selectPosition);
    }

    public void setItemClickListener(onAdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
