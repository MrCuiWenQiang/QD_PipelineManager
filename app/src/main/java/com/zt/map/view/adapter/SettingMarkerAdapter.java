package com.zt.map.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zt.map.R;
import com.zt.map.entity.TreeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingMarkerAdapter extends RecyclerView.Adapter<SettingMarkerAdapter.TitleViewHolder> {

    private Map<String, List<String>> dataMap;
    private List<String> params;
    private OnItemSettingListener onItemSettingListener;

    public void setDataMap(Map<String, List<String>> dataMap) {
        this.dataMap = dataMap;

        if (params != null) {
            params.clear();
        } else {
            params = new ArrayList<>();
        }

        if (dataMap != null) {
            for (String key : dataMap.keySet()) {
                params.add(key);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemSettingListener(OnItemSettingListener onItemSettingListener) {
        this.onItemSettingListener = onItemSettingListener;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_marker_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        final String title = params.get(position);
        holder.tv_title.setText(title);
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSettingListener.onSave(title);
            }
        });

        SettingMarkerChildAdapter adapter = new SettingMarkerChildAdapter();
        holder.rv_child_item_list.setAdapter(adapter);//为性能优化 后期加入缓存
        adapter.setDataMap(dataMap.get(title));
        adapter.setOnItemListener(new SettingMarkerChildAdapter.onItemListener() {
            @Override
            public void onDelete(String itemName, int position) {
                onItemSettingListener.onDelete(title,itemName,position);
            }

            @Override
            public void onRevise(String itemName, int position) {
                onItemSettingListener.onRevise(title,itemName,position);
            }
        });
    }
    public interface OnItemSettingListener{
        void onDelete(String fatherName,String itemName,int position);
        void onRevise(String fatherName,String itemName,int position);
        void onSave(String fatherName);
    }
    @Override
    public int getItemCount() {
        return params == null ? 0 : params.size();
    }

    protected class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_add;
        private TextView tv_title;
        private RecyclerView rv_child_item_list;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_add = itemView.findViewById(R.id.tv_add);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_child_item_list = itemView.findViewById(R.id.rv_child_item_list);
            rv_child_item_list.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
