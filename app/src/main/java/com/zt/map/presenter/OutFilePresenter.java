package com.zt.map.presenter;

import android.os.Environment;

import com.zt.map.constant.FileContract;
import com.zt.map.constant.OutFileContract;
import com.zt.map.entity.FileBean;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;


public class OutFilePresenter extends BaseMVPPresenter<OutFileContract.View> implements OutFileContract.Presenter {
    private String filePath = FileContract.getOutFilePath();

    @Override
    public void attachView(OutFileContract.View view) {
        super.attachView(view);
        queryFiles();
    }

    @Override
    public void queryFiles() {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity< List<FileBean>>>() {

            @Override
            protected BaseMVPModel.MessageEntity< List<FileBean>> jobContent() {
                BaseMVPModel.MessageEntity< List<FileBean>> msg = new BaseMVPModel.MessageEntity<>();
                msg.setStatus(true);
                File file = new File(filePath);
                if (!file.exists()) {
                    return msg; //既然文件夹不存在 则不存在文件
                }
                File[] fs = file.listFiles();
                if (fs == null) {
                    return msg;
                }
                List<FileBean> fileDatas = new LinkedList<>();
                for (File f :  fs) {
                    if (f.isDirectory()){
                        continue;
                    }
                    if (f.getName().contains(".xls")){
                        FileBean fileBean = new FileBean(f.getName(),f.getPath());
                        fileDatas.add(fileBean);
                    }
                }
                msg.setData(fileDatas);
                return msg;
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity< List<FileBean>> datab) {
                if (getView()!=null){
                    if (datab.isStatus()){
                        getView().queryFiles_success(datab.getData());
                    }else {
                        getView().queryFiles("查询失败");
                    }
                }
            }
        });
    }
}
