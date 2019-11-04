package com.zt.map.view.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.map.R;
import com.zt.map.entity.db.tab.Tab_Project;

import java.util.List;

import cn.faker.repaymodel.util.DateUtils;
import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

/**
 * 新项目选择列表对话框
 */
public class ProjectDialog extends BasicDialog {

    private RecyclerView rv_list;
    private ProjectAdapter adapter = new ProjectAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.dg_project;
    }

    @Override
    public void initview(View v) {
        rv_list = v.findViewById(R.id.rv_list);

        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    public ProjectDialog setTab_projects(List<Tab_Project> tab_projects) {
        adapter.setTab_projects(tab_projects);
        return this;
    }

    public ProjectDialog setOnItemListener(OnItemListener onItemListener) {
        adapter.setOnItemListener(onItemListener);
        return this;
    }

    protected class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

        private List<Tab_Project> tab_projects;

        private OnItemListener onItemListener;

        public void setTab_projects(List<Tab_Project> tab_projects) {
            this.tab_projects = tab_projects;
        }

        public void setOnItemListener(OnItemListener onItemListener) {
            this.onItemListener = onItemListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Tab_Project project = tab_projects.get(position);
            holder.tv_name.setText(project.getName());
            holder.tv_date.setText("创建于" + DateUtils.dateToString(project.getCreateTime(), DateUtils.DATE_FORMAT_CHINESE));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListener.onClick(project);
                }
            });

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListener.onDelete(project.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return tab_projects == null ? 0 : tab_projects.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_name;
            private TextView tv_date;
            private ImageView iv_delete;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_date = itemView.findViewById(R.id.tv_date);
                iv_delete = itemView.findViewById(R.id.iv_delete);
            }
        }

    }

    public interface OnItemListener {
        void onDelete(long id);

        void onClick(Tab_Project tabProject);
    }
}
