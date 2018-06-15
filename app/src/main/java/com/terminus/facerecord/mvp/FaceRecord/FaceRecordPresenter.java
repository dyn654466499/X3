package com.terminus.facerecord.mvp.FaceRecord;

import android.support.v4.util.SimpleArrayMap;

import com.terminus.facedetectsdk.core.FaceDetect;
import com.terminus.facedetectsdk.core.FaceInfo;

import java.util.List;

/**
 * Created by 邓耀宁 on 2018/3/16.
 */

public class FaceRecordPresenter implements FaceRecordContract.Presenter {
    private final FaceRecordContract.View mView;
    private SimpleArrayMap<Integer, FaceInfo> trackingMap;
    private static final int PERSON_DONT_KNOW = -111;//不认识此人
    private static final int PERSON_NO_RECOGNIZE_FACE = -1;//未识别到人脸

    public FaceRecordPresenter(FaceRecordContract.View view){
        mView = view;
        if(view == null) {
            throw new IllegalArgumentException("FaceRecordContract View can not be null");
        }
        mView.setPresenter(this);
        initTrackMap();
    }

    private void initTrackMap(){
        if (trackingMap != null && trackingMap.size() != 0) {
            trackingMap.clear();
        }
        trackingMap = new SimpleArrayMap<>();
    }

    int count;
    @Override
    public void startTrack(final byte[] frame, int width, int height) {
        final List<FaceInfo> faces = FaceDetect.getInstance().trackMulti(frame, width, height);
//        try {
//            if (faces != null && faces.size() > 0) {
//                count++;
//                if(count > 10){
//                    count = 0;
//                    if (trackingMap.size() > 50) trackingMap.clear();
//                    //只对最大人脸框进行识别
//                    for (int i = 0; i < faces.size(); i++) {
//                        final FaceInfo ymFace = faces.get(i);
//                        final int anaIndex = i;
//                        ThreadPoolUtils.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    final byte[] yuvData = new byte[frame.length];
//                                    System.arraycopy(frame, 0, yuvData, 0, frame.length);
//
//                                    boolean next = true;
//                                    final float[] headposes = ymFace.getHeadpose();
//                                    if ((Math.abs(headposes[0]) > 30
//                                            || Math.abs(headposes[1]) > 30
//                                            || Math.abs(headposes[2]) > 30)) {
//                                        //角度不佳不再识别
//                                        next = false;
//                                    }
//                                    int faceQuality = FaceDetect.getInstance().getFaceQuality(anaIndex);
//
//                                    if (faceQuality < 85) {
//                                        //人脸质量不佳，不再识别
//                                        next = false;
//                                    }
//                                    long time = System.currentTimeMillis();
//                                    int identifyPerson = -11;
//                                    if (next) {
//                                        final int trackId = ymFace.getTrackId();
//                                        if (!trackingMap.containsKey(trackId) ||
//                                                trackingMap.get(trackId).getPersonId() <= 0) {
//                                            identifyPerson = FaceDetect.getInstance().identifyPerson(anaIndex);
//                                            int confidence = FaceDetect.getInstance().getRecognitionConfidence();
////                                        saveImageFromCamera(identifyPerson, yuvData);
//                                            ymFace.setIdentifiedPerson(identifyPerson, confidence);
//                                            trackingMap.put(trackId, ymFace);
//                                        }
//                                    }
//
//                                    DLog.d("identify end " + identifyPerson + " time :" + (System.currentTimeMillis() - time)
//                                            + "  faceQuality: " + faceQuality
//                                    );
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//
//                 }
//                for (int i = 0; i < faces.size(); i++) {
//                    final FaceInfo ymFace = faces.get(i);
//                    final int trackId = ymFace.getTrackId();
//                    if (trackingMap.containsKey(trackId)) {
//                        FaceInfo face = trackingMap.get(trackId);
//                        ymFace.setIdentifiedPerson(face.getPersonId(), face.getConfidence());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        mView.drawTrackView(faces, frame);
    }

    @Override
    public int addFace(FaceRecordContract.AddFaceCallBack callBack) {
        int personId = FaceDetect.getInstance().identifyPerson(0);
        if (personId == PERSON_DONT_KNOW) {
            personId = FaceDetect.getInstance().addPerson(0);
            if(callBack != null){
                callBack.addSuccess(personId);
            }
        } else if(personId == PERSON_NO_RECOGNIZE_FACE){
            if(callBack != null){
                callBack.notRecognizeFace();
            }
        } else {
            if(callBack != null){
                callBack.hasFace(personId);
            }
        }
        return personId;
    }

    @Override
    public int updateFace(int personId) {
        int i = FaceDetect.getInstance().updatePerson(personId, 0);
        return i;
    }

}
