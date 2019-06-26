package com.zt.map.constant;

import com.zt.map.entity.FileBean;

import java.util.List;

public class OutFileContract {
    public interface View{
        void queryFiles_success(List<FileBean> files);
        void queryFiles(String msg);
    }

    public interface Presenter{
        void queryFiles();
    }

    public interface Model{
    }
}
