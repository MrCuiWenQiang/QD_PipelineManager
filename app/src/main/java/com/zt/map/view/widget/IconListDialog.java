package com.zt.map.view.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.map.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.util.SpaceItemDecoration;
import cn.faker.repaymodel.util.SpacesItemDecoration;
import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

public class IconListDialog extends BasicDialog {

    private RecyclerView rl_list;
    private IconListAdapter adapter = new IconListAdapter();


    @Override
    public int getLayoutId() {
        return R.layout.dg_iconlist;
    }

    @Override
    public void initview(View v) {
        rl_list = v.findViewById(R.id.rl_list);

        rl_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rl_list.setAdapter(adapter);
        rl_list.addItemDecoration(new SpaceItemDecoration(10));
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1, -2);
    }

    public IconListDialog setBitmap(Map<String, Bitmap> bitmap) {
        adapter.setDatabitmap(bitmap);
        return this;
    }

    public IconListDialog setOnItemClickListener(IconListDialog.onIconDialogItem onItemClickListener) {
        adapter.setOnIconDialogItem(onItemClickListener);
        return this;
    }


    class IconListAdapter extends RecyclerView.Adapter<IconListAdapter.IconViewHolder> {

        private Map<String, Bitmap> databitmap;
        private List<String> names;
        private onIconDialogItem onIconDialogItem;
        public void setDatabitmap(Map<String, Bitmap> databitmap) {
            this.databitmap = databitmap;
            if (databitmap != null) {
                if (names == null) {
                    names = new ArrayList<>();
                }
                names.clear();
                for (String key :
                        databitmap.keySet()) {
                    names.add(key);
                }
            }
        }

        public void setOnIconDialogItem(IconListDialog.onIconDialogItem onIconDialogItem) {
            this.onIconDialogItem = onIconDialogItem;
        }

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_list, parent, false);
            return new IconViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IconViewHolder holder, final int position) {
            final String name = names.get(position);
            Bitmap bitmap = databitmap.get(name);
            holder.tv_name.setText(name);
            holder.iv_icon.setImageBitmap(bitmap);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    onIconDialogItem.onClick(name);
                }
            });
        }

        @Override
        public int getItemCount() {
            return databitmap == null ? 0 : databitmap.size();
        }

        protected class IconViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public IconViewHolder(View itemView) {
                super(itemView);
                iv_icon = itemView.findViewById(R.id.iv_icon);
                tv_name = itemView.findViewById(R.id.tv_name);
            }
        }
    }
    public interface onIconDialogItem{
        void onClick(String name);
    }
}
