package com.zt.map.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zt.map.R;
import com.zt.map.entity.TreeBean;

import java.util.List;

/**
 * 不止用于marker
 * 还适用于Line   类名称不做改变，会误解
 */

public class SettingMarkerChildAdapter extends RecyclerView.Adapter<SettingMarkerChildAdapter.ChildViewHolder> {

    private List<String> datas;
    private onItemListener onItemListener;

    public SettingMarkerChildAdapter() {
    }

    public SettingMarkerChildAdapter(List<String> datas) {
        this.datas = datas;
    }

    public void setDataMap(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemListener(SettingMarkerChildAdapter.onItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_marker_child,parent,false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, final int position) {
        final String name = datas.get(position);
        holder.tvValue.setText(name);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onDelete(name,position);
            }
        });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onRevise(name,position);
            }
        });
    }

    public interface onItemListener{
        void onDelete(String itemName,int position);
        void onRevise(String itemName,int position);
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    protected class ChildViewHolder extends RecyclerView.ViewHolder{
        private TextView tvValue;
        private TextView tvEdit;
        private TextView tvDelete;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tv_value);
            tvEdit = itemView.findViewById(R.id.tv_edit);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
