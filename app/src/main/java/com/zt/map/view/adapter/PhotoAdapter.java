package com.zt.map.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zt.map.R;
import com.zt.map.entity.db.tab.Tab_marker_photo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    List<Tab_marker_photo> datas;
    private boolean selectStatus = false;

    private Map<Integer, Boolean> statuMap = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener onItemClickListener;

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }

    public void setDatas(List<Tab_marker_photo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<Tab_marker_photo> getSelectData() {
        List<Tab_marker_photo> selectdatas = new ArrayList<>();
        if (statuMap != null) {
            for (int key : statuMap.keySet()) {
                if (statuMap.get(key)) {
                    selectdatas.add(datas.get(key));
                }
            }
        }
        return selectdatas;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder holder, final int position) {
        Tab_marker_photo photo = datas.get(position);
        Picasso.with(holder.iv_icon.getContext()).load("file://" + photo.getPath())
                .error(R.mipmap.error).into(holder.iv_icon);
        if (selectStatus) {
            holder.cb_select.setVisibility(View.VISIBLE);
            if (statuMap.containsKey(position)) {
                holder.cb_select.setChecked(statuMap.get(position));
            } else {
                holder.cb_select.setChecked(false);
            }
        }
        holder.tv_name.setText(photo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectStatus) {
                    holder.cb_select.setChecked(!holder.cb_select.isChecked());
                    statuMap.put(position, holder.cb_select.isChecked());
                } else {
                    onItemClickListener.onItemClick(null, null, position, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
        private CheckBox cb_select;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            cb_select = itemView.findViewById(R.id.cb_select);
            cb_select.setClickable(false);
        }
    }
}
