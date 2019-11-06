package com.zt.map.presenter;

import android.text.TextUtils;

import com.zt.map.contract.InputContract;
import com.zt.map.entity.db.tab.Tab_Project;
import com.zt.map.util.input.InputLineUtil;
import com.zt.map.util.input.InputUtil;

import java.util.Date;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class InputPresenter  extends BaseMVPPresenter<InputContract.View> implements InputContract.Presenter  {

    String typeName = "xls";

    @Override
    public void inputProject(final String projectname, final String marterFilePath, final String lineFilePath) {
        if (TextUtils.isEmpty(projectname)){
            getView().inputProjectFail("请填写项目名称");
            return;
        }
        if (TextUtils.isEmpty(marterFilePath) && TextUtils.isEmpty(lineFilePath)){
            getView().inputProjectFail("管点表和管线表至少选择一样");
            return;
        }
        if (!TextUtils.isEmpty(marterFilePath) ){
            if (!isExcel(marterFilePath)){
                getView().inputProjectFail("请选择excle类型文件");
                return;
            }

        }
        if (!TextUtils.isEmpty(lineFilePath)){
            if (!isExcel(lineFilePath)){
                getView().inputProjectFail("请选择excle类型文件");
                return;
            }
        }
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity>() {

            @Override
            protected BaseMVPModel.MessageEntity jobContent() throws Exception {
                int p_count = LitPalUtils.selectCount(Tab_Project.class,"name =?",projectname);
                if (p_count>0){
                    return BaseMVPModel.MessageEntity.fail("项目名重复");
                }
                Tab_Project project = new Tab_Project();
                project.setName(projectname);
                project.setCreatePhoto(true);
                project.setCreateTime(new Date());
                project.save();
                if (!TextUtils.isEmpty(marterFilePath)){
                    InputUtil.inputMarker(marterFilePath,project.getId());
                }
                if (!TextUtils.isEmpty(lineFilePath)){
                    InputLineUtil.input(lineFilePath,project.getId());
                }
                return BaseMVPModel.MessageEntity.success();
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity messageEntity) {
                if (messageEntity.isStatus()){
                    if (getView()!=null){
                        getView().inputProjectSuccess();
                    }
                }else {
                    if (getView()!=null){
                        getView().inputProjectFail(messageEntity.getMessage());
                    }
                }
            }
        });

    }
    private boolean isExcel(String path){
        int last = path.lastIndexOf(".");
        String lastType = path.substring(last+1,path.length());
        return typeName.equals(lastType);
    }
}
