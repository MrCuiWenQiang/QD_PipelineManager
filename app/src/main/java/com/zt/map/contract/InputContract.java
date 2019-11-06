package com.zt.map.contract;

public class InputContract {
    public interface View {
        void inputProjectFail(String msg);
        void inputProjectSuccess();
    }

    public interface Presenter {
        /**+
         * 导入文档
         * @param marterFilePath
         * @param lineFilePath
         */
        void inputProject(String projectname,String marterFilePath,String lineFilePath);
    }

    public interface Model {
    }
}
