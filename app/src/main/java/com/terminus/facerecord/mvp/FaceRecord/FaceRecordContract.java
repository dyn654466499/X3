package com.terminus.facerecord.mvp.FaceRecord;

import com.terminus.facedetectsdk.core.FaceInfo;
import com.terminus.facerecord.mvp.BasePresenter;
import com.terminus.facerecord.mvp.BaseView;

import java.util.List;

/**
 * Created by 邓耀宁 on 2018/3/23.
 */

public class FaceRecordContract {
    public interface View extends BaseView<Presenter> {
        void drawTrackView(List<FaceInfo> faces, byte[] frame);
    }

    public interface Presenter extends BasePresenter {
        void startTrack(byte[] frame, int width, int height);
        int addFace(AddFaceCallBack callBack);
        int updateFace(int personId);
    }

    public interface AddFaceCallBack{
        void hasFace(int personId);
        void addSuccess(int personId);
        void notRecognizeFace();
    }
}
