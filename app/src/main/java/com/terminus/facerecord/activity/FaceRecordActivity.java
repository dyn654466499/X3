package com.terminus.facerecord.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.terminus.facedetectsdk.core.FaceConstant;
import com.terminus.facedetectsdk.core.FaceDetect;
import com.terminus.facedetectsdk.core.FaceDetectManager;
import com.terminus.facedetectsdk.core.FaceInfo;
import com.terminus.facedetectsdk.utils.CommonUtils;
import com.terminus.facedetectsdk.utils.DrawViewUtils;
import com.terminus.facedetectsdk.utils.UserFaceUtils;
import com.terminus.facerecord.R;
import com.terminus.facerecord.constants.Config;
import com.terminus.facerecord.mvp.FaceRecord.FaceRecordContract;
import com.terminus.facerecord.mvp.FaceRecord.FaceRecordPresenter;
import com.terminus.facerecord.utils.DialogUtils;
import com.terminus.facerecord.utils.LogUtils;
import com.terminus.facerecord.views.ViewfinderView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import dou.helper.CameraHelper;
import dou.helper.CameraParams;
import dou.utils.BitmapUtil;
import dou.utils.DLog;
import dou.utils.DisplayUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static com.terminus.facerecord.activity.FaceRecordActivity.FaceDirection.DOWN;
import static com.terminus.facerecord.activity.FaceRecordActivity.FaceDirection.FRONT;
import static com.terminus.facerecord.activity.FaceRecordActivity.FaceDirection.SIDE_LEFT;
import static com.terminus.facerecord.activity.FaceRecordActivity.FaceDirection.SIDE_RIGHT;
import static com.terminus.facerecord.activity.FaceRecordActivity.FaceDirection.UP;

public class FaceRecordActivity extends BaseActivity implements FaceRecordContract.View{
    protected SurfaceView camera_view;
    protected SurfaceView draw_view;
    protected CameraHelper mCameraHelper;
    protected FaceRecordContract.Presenter presenter;
    private TextView tv_timer, tv_record_step, tv_record_tips;
    private static final int PERSON_NO_RECOGNIZE = -111;
    private int mPersonId = PERSON_NO_RECOGNIZE;
    private boolean canAdd = false;
    private byte[] frame;
    private Bitmap head;
    float scale_bit;
    int iw;
    int ih;
    private FaceInfo theFace = new FaceInfo();
    private String CUR_ADD_TEXT = "请把脸对准框内，确保光线充足";
    private String CUR_TIP_TEXT = "";
    private boolean isFrontCamera = true;

    /**
     * 当前人脸需要录入的方向
     */
    enum FaceDirection{
        FRONT, SIDE_LEFT, SIDE_RIGHT, UP, DOWN, END
    }
    private FaceDirection CUR_FACE_DIRECTION = FRONT;

    private boolean isShowTips, canShoot, isTimerStarted;
    CountDownTimer timer;
    private String age, gender;
    /**
     * 第一张人脸正脸的特征值
     */
    private float[] firstFaceFuture;
    private static final int FACE_DOWN_CONFIDENCE = 80;
    private static final int FACE_SIDE_CONFIDENCE = 80;
    private static final int FACE_UP_CONFIDENCE = 70;
    private static final int FACE_QUALITY = 95;

    private static final String FACE_FRONT_TIP = "请摆好正脸";
    private static final String FACE_SIDE_LEFT_TIP = "请左转头";
    private static final String FACE_SIDE_RIGHT_TIP = "请右转头";
    private static final String FACE_DOWN_TIP = "最后一步，请低头";
    private static final String FACE_UP_TIP = "请抬头";
    private static final String FACE_RECORD_DONE = "叮";

//    private static final String FACE_FRONT_TIP = "叮";
//    private static final String FACE_SIDE_LEFT_TIP = "叮";
//    private static final String FACE_SIDE_RIGHT_TIP = "叮";
//    private static final String FACE_DOWN_TIP = "叮";
//    private static final String FACE_UP_TIP = "叮";

    private TextToSpeech textToSpeech;
    private static final Locale LANGUAGE = Locale.CHINA;
    private String personId = "daemond";
    private ImageView iv_record_step1,iv_record_step2,
            iv_record_step3,iv_record_step4,iv_record_step5;
    private ViewfinderView view_scanner;
    private Handler mHandler;
    private static final int MSG_UPLOAD_SUCCESS = 0;
    private static final int MSG_UPLOAD_FAIL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_record);
        FaceDetectManager.initDetectInstance(this);
        initView();
        initCameraHelper();
        presenter = new FaceRecordPresenter(this);
        timer = new CountDownTimer(2 * 1000 + 990,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(tv_timer != null){
//                    String count = String.valueOf(millisUntilFinished / 1000);
//                    tv_timer.setText(count + "秒");
//                    // 设置透明度渐变动画
//                    final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//                    //设置动画持续时间
//                    alphaAnimation.setDuration(1000);
//                    tv_timer.startAnimation(alphaAnimation);
//                    // 设置缩放渐变动画
//                    final ScaleAnimation scaleAnimation =new ScaleAnimation(0.5f, 2f, 0.5f,2f,
//                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    scaleAnimation.setDuration(1000);
//                    tv_timer.startAnimation(scaleAnimation);
//                    speech(count);
                }
            }

            @Override
            public void onFinish() {
                canShoot = true;
                isTimerStarted = false;
                tv_timer.setText("");
            }
        };

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_UPLOAD_SUCCESS:
                        dismissLoadingDialog();
                        showTip("上传成功");
                        finish();
                        break;

                    case MSG_UPLOAD_FAIL:
                        dismissLoadingDialog();
                        DialogUtils.showDialog(FaceRecordActivity.this, "上传失败，请重新上传",
                                "取消","重新上传",new DialogUtils.DialogCommand() {
                            @Override
                            public void onLeftConfirm() {
                                resetData();
                            }

                            @Override
                            public void onRightConfirm() {
                                uploadFaceImages();
                            }
                        });
                        break;
                }
            }
        };
    }

    protected void initView(){
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int supported = textToSpeech.setLanguage(LANGUAGE);
                    if(supported == TextToSpeech.LANG_NOT_SUPPORTED){
                        DLog.d("语音","系统不支持该语言");
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    }else{
                        DLog.d("语音","系统支持该语言");
                    }
                }
            }
        });

        camera_view = findViewById(R.id.camera_preview);

        draw_view = findViewById(R.id.pointView);
        tv_record_step = findViewById(R.id.tv_record_step);
        tv_record_step.setText(CUR_ADD_TEXT);
        tv_record_tips = findViewById(R.id.tv_record_tips);
        tv_record_tips.setText(CUR_TIP_TEXT);
        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_timer = findViewById(R.id.tv_timer);
//        tv_timer.setText(FACE_FRONT_TIP);

        draw_view.setZOrderOnTop(true);
        draw_view.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        iv_record_step1 = findViewById(R.id.iv_record_step1);
        iv_record_step2 = findViewById(R.id.iv_record_step2);
        iv_record_step3 = findViewById(R.id.iv_record_step3);
        iv_record_step4 = findViewById(R.id.iv_record_step4);
        iv_record_step5 = findViewById(R.id.iv_record_step5);

        view_scanner = findViewById(R.id.view_scanner);
//        view_scanner.post(new Runnable() {
//            @Override
//            public void run() {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) camera_view.getLayoutParams();
//                Rect scanner = view_scanner.getRect();scanner
//                params.width = scanner.right - scanner.left;
//                params.height = scanner.bottom - scanner.top;
//                camera_view.setLayoutParams(params);
//            }
//        });
    }

    private void resetData(){
        CUR_FACE_DIRECTION = FRONT;
        iv_record_step1.setImageResource(R.drawable.tp_icon_step1);
        iv_record_step2.setImageResource(R.drawable.tp_icon_step2);
        iv_record_step3.setImageResource(R.drawable.tp_icon_step3);
        iv_record_step4.setImageResource(R.drawable.tp_icon_step4);
        iv_record_step5.setImageResource(R.drawable.tp_icon_step5);
    }

    private void speech(String text){
         if(textToSpeech != null && !textToSpeech.isSpeaking()) {
             speechRate = 0;
             textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    /**
     * 获取部分属性值
     */
    private void getFaceAttribute(){
        saveFirstFaceFuture(FaceDetect.getInstance().getFaceFeature(0));
        int gender_confidence = FaceDetect.getInstance().getGenderConfidence(0);
        gender = " ";
        if (gender_confidence >= 90)
            gender = FaceDetect.getInstance().getGender(0) == 0 ? "女" : "男";
        age = String.valueOf(computingAge(FaceDetect.getInstance().getAge(0)));
    }

    /**
     * 记录第一张脸的特征值
     * @param future
     */
    private void saveFirstFaceFuture(float[] future){
        firstFaceFuture = new float[future.length];
        System.arraycopy(future, 0, firstFaceFuture, 0, future.length);
    }

    public static int computingAge(int age) {
        int ran = new Random().nextInt(3) + 3;
        age = (age / ran) * ran;
        return age;
    }

    public void initCameraHelper() {
        //预设Camera参数，方便扩充
        CameraParams params = new CameraParams();
        //优先使用的camera Id,
        params.firstCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        if(params.firstCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            isFrontCamera = false;
        }
        params.surfaceView = camera_view;
        params.preview_width = 640;
        params.preview_height = 480;
        params.camera_ori = 90;
        params.camera_ori_front = 90;
        params.pre_rate = 90;
        params.previewFrameListener = new CameraHelper.PreviewFrameListener() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                initParams();
                if(!isShowTips) {
                    presenter.startTrack(bytes, iw, ih);
                    if (CUR_FACE_DIRECTION == FRONT) {
                        frame = new byte[bytes.length];
                        System.arraycopy(bytes, 0, frame, 0, bytes.length);
                    }
                }
            }
        };
        mCameraHelper = new CameraHelper(this, params);
    }

    protected void initParams(){
        if(iw == 0) {

            int surface_w = camera_view.getLayoutParams().width;
            int surface_h = camera_view.getLayoutParams().height;

            iw = mCameraHelper.getPreviewSize().width;
            ih = mCameraHelper.getPreviewSize().height;

            int sw = DisplayUtil.getScreenWidthPixels(this);
            int sh = DisplayUtil.getScreenHeightPixels(this);
            int orientation;
            if (sw < sh) {
                scale_bit = surface_w / (float) ih;
                if (isFrontCamera) {
                    orientation = FaceConstant.FACE_270;
                } else {
                    orientation = FaceConstant.FACE_90;
                }

                if(CommonUtils.isPad(this)){
                    orientation = FaceConstant.FACE_90;
                }
            } else {
                scale_bit = surface_h / (float) ih;
                orientation = FaceConstant.FACE_0;
            }
            FaceDetect.getInstance().setOrientation(orientation);
            ViewGroup.LayoutParams params = draw_view.getLayoutParams();
            params.width = surface_w;
            params.height = surface_h;
            draw_view.requestLayout();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(FaceRecordContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        stopCamera();
    }

    private int timerRate;
    private int speechRate;

    @Override
    public void drawTrackView(List<FaceInfo> faces, byte[] frame) {
//        DrawViewUtils.drawFaceTackView(this, faces, draw_view, scale_bit, mCameraHelper.getCameraId());
        if(faces != null && faces.size() > 1){
            Toast.makeText(this, "不能同时录入多个人的脸", Toast.LENGTH_SHORT);
            return;
        }
        boolean hasFaces = faces != null && faces.size() > 0;
//        if(hasFaces){
//            Rect scanner = view_scanner.getRect();
//            FaceInfo face = faces.get(0);
//            float[] temp = face.getRect();
//            Rect faceRect = new Rect((int)temp[0],(int)temp[1],(int)temp[0] + (int)temp[2],(int)temp[1] + (int)temp[3]);
//            if(!scanner.contains(faceRect)){
//                CUR_ADD_TEXT = "请把脸对准框内，确保光线充足";
//                CUR_TIP_TEXT = "";
//                tv_record_step.setText(CUR_ADD_TEXT);
//                tv_record_tips.setText(CUR_TIP_TEXT);
//                return;
//            }
//        }
        switch (CUR_FACE_DIRECTION){
            case FRONT:
                int quality = -1;
                if(hasFaces) {
                    quality = FaceDetect.getInstance().getFaceQuality(0);
                    canAdd = UserFaceUtils.isFrontFace(faces.get(0)) && quality > FACE_QUALITY ;
                    theFace.setRect(faces.get(0).getRect());
                }else{
                    canAdd = false;
                }

                if(canAdd && !isShowTips) {
                    if(!isTimerStarted && !canShoot) {
                        timer.start();
                        isTimerStarted = true;
                    }
                    if (canShoot) {
                        canShoot = false;
//                        getFaceAttribute();
                        saveImageFromCamera(personId, FRONT.ordinal(), frame);
                        CUR_FACE_DIRECTION = SIDE_LEFT;
                        //TODO 语音完成“叮”
                        iv_record_step1.setImageResource(R.drawable.tp_icon_done);
                        speech(FACE_SIDE_LEFT_TIP);
                    }
                }
                CUR_ADD_TEXT = "正脸照片";
                CUR_TIP_TEXT = "";
                if(!canAdd) {
                    boolean direction = !hasFaces || !UserFaceUtils.isFrontFace(faces.get(0));
                    if(direction){
                        CUR_TIP_TEXT = "角度不对";
                    }
                    if (canResetTimer() && (direction || quality < 75)) {
                        resetTimer(FACE_FRONT_TIP, hasFaces);
                    }
                }

                break;

            case SIDE_LEFT:
                canAdd = hasFaces && UserFaceUtils.isLeftSideFace(faces.get(0));
                if(canAdd){
                    if(!isTimerStarted && !canShoot) {
                        timer.start();
                        isTimerStarted = true;
                    }
                    if (canShoot) {
//                        float[] future = FaceDetect.getInstance().getFaceFeature(0);
//                        int confidence = FaceDetect.getInstance().compareFaceFeature(firstFaceFuture, future);
//                        Toast.makeText(this, "与正脸的特征值比对置信度 = " + confidence, Toast.LENGTH_LONG).show();
//                        if(confidence < FACE_SIDE_CONFIDENCE) {
//                            resetTimer(FACE_SIDE_LEFT_TIP);
//                            return;
//                        }
                        canShoot = false;
                        saveImageFromCamera(personId, SIDE_LEFT.ordinal(), frame);
                        iv_record_step2.setImageResource(R.drawable.tp_icon_done);
                        CUR_FACE_DIRECTION = SIDE_RIGHT;
                        speech(FACE_SIDE_RIGHT_TIP);
                    }
                }
                CUR_ADD_TEXT = "左脸照片";
                CUR_TIP_TEXT = "";
                if(!canAdd) {
                    CUR_TIP_TEXT = "角度不对";
                    if(canResetTimer()) {
                        resetTimer(FACE_SIDE_LEFT_TIP, hasFaces);
                    }
                }

                break;

            case SIDE_RIGHT:
                canAdd = hasFaces && UserFaceUtils.isRightSideFace(faces.get(0));
                if(canAdd){
                    if(!isTimerStarted && !canShoot) {
                        timer.start();
                        isTimerStarted = true;
                    }
                    if (canShoot) {
//                        float[] future = FaceDetect.getInstance().getFaceFeature(0);
//                        int confidence = FaceDetect.getInstance().compareFaceFeature(firstFaceFuture, future);
//                        DLog.d("confidence = " + confidence);
//                        Toast.makeText(this, "与正脸的特征值比对置信度 = " + confidence, Toast.LENGTH_LONG).show();
//                        if(confidence < FACE_SIDE_CONFIDENCE) {
//                            resetTimer(FACE_SIDE_RIGHT_TIP);
//                            return;
//                        }
                        canShoot = false;
                        saveImageFromCamera(personId, SIDE_RIGHT.ordinal(), frame);
                        iv_record_step3.setImageResource(R.drawable.tp_icon_done);
                        CUR_FACE_DIRECTION = UP;
                        speech(FACE_UP_TIP);
                    }
                }
                CUR_ADD_TEXT = "右脸照片";
                CUR_TIP_TEXT = "";
                if(!canAdd) {
                    CUR_TIP_TEXT = "角度不对";
                    if(canResetTimer()) {
                        resetTimer(FACE_SIDE_LEFT_TIP, hasFaces);
                    }
                }

                break;

            case UP:
                canAdd = hasFaces && UserFaceUtils.isUpFace(faces.get(0));
                if(canAdd){
                    if(!isTimerStarted && !canShoot) {
                        timer.start();
                        isTimerStarted = true;
                    }
                    if (canShoot) {
//                        float[] future = FaceDetect.getInstance().getFaceFeature(0);
//                        int confidence = FaceDetect.getInstance().compareFaceFeature(firstFaceFuture, future);
//                        DLog.d("confidence = " + confidence);
//                        Toast.makeText(this, "与正脸的特征值比对置信度 = " + confidence, Toast.LENGTH_LONG).show();
//                        if(confidence < FACE_UP_CONFIDENCE) {
//                            resetTimer(FACE_UP_TIP);
//                            return;
//                        }
                        canShoot = false;
                        saveImageFromCamera(personId, UP.ordinal(), frame);
                        iv_record_step4.setImageResource(R.drawable.tp_icon_done);
                        CUR_FACE_DIRECTION = DOWN;
                        speech(FACE_DOWN_TIP);
                    }
                }
                CUR_ADD_TEXT = "抬头照片";
                CUR_TIP_TEXT = "";
                if(!canAdd){
                    CUR_TIP_TEXT = "角度不对";
                    if(canResetTimer()) {
                        resetTimer(FACE_UP_TIP, hasFaces);
                    }
                }

                break;

            case DOWN:
                canAdd = hasFaces && UserFaceUtils.isDownFace(faces.get(0));
                if(canAdd){
                    if(!isTimerStarted && !canShoot) {
                        timer.start();
                        isTimerStarted = true;
                    }
                    if (canShoot) {
//                        float[] future = FaceDetect.getInstance().getFaceFeature(0);
//                        int confidence = FaceDetect.getInstance().compareFaceFeature(firstFaceFuture, future);
//                        DLog.d("confidence = " + confidence);
//                        Toast.makeText(this, "与正脸的特征值比对置信度 = " + confidence, Toast.LENGTH_LONG).show();
//                        if(confidence < FACE_DOWN_CONFIDENCE) {
//                            resetTimer(FACE_DOWN_TIP);
//                            return;
//                        }
                        canShoot = false;
                        saveImageFromCamera(personId, DOWN.ordinal(), frame);
                        iv_record_step5.setImageResource(R.drawable.tp_icon_done);
                        CUR_FACE_DIRECTION = FaceDirection.END;
                        uploadFaceImages();
                    }
                }
                CUR_ADD_TEXT = "低头照片";
                CUR_TIP_TEXT = "";
                if(!canAdd){
                    CUR_TIP_TEXT = "角度不对";
                    if(canResetTimer()) {
                        resetTimer(FACE_DOWN_TIP, hasFaces);
                    }
                }

                break;

            case END:
                CUR_ADD_TEXT = "";
                CUR_TIP_TEXT = "";
                canAdd = true;
                break;
        }
        if(faces == null || faces.size() == 0){
            CUR_ADD_TEXT = "请把脸对准框内，确保光线充足";
            CUR_TIP_TEXT = "";
        }
        tv_record_step.setText(CUR_ADD_TEXT);
        tv_record_tips.setText(CUR_TIP_TEXT);
    }

    private boolean canResetTimer(){
        timerRate++;
        if(timerRate < 20){
            return false;
        }
        timerRate = 0;
        return true;
    }

    private boolean canSpeech(){
        speechRate++;
        if (speechRate < 6) {
            return false;
        }
        speechRate = 0;
        return true;
    }

    private void resetTimer(String tip){
        resetTimer(tip, false);
    }

    private void resetTimer(String tip, boolean speech){
        timer.cancel();
//        tv_timer.clearAnimation();
//        tv_timer.setText(tip);
        isTimerStarted = false;
        canShoot = false;
        if(speech && canSpeech()) {
            speech(tip);
        }
    }

    public void saveImageFromCamera(String personId, int count, byte[] yuvBytes) {
        String path = Environment.getExternalStorageDirectory().getPath();
        File tmpFile = new File(path + "/" + personId);
        if (!tmpFile.exists()) tmpFile.mkdirs();
        tmpFile = new File(path + "/" + personId + "/img_" + count + ".jpg");
        saveImage(tmpFile, yuvBytes);
    }

    private void saveImage(File file, byte[] yuvBytes) {
        Bitmap image = BitmapUtil.getBitmapFromYuvByte(yuvBytes, iw, ih);
        float[] rect = theFace.getRect();
        Matrix matrix = new Matrix();
        if(CommonUtils.isPad(this)) {
            //pad竖屏需旋转90度
            matrix.postRotate(isFrontCamera ? 90 : 270);
            int y = ih - (int) rect[0] - (int) (rect[2]);
            int x = (int) rect[1];
            int width = (int) (rect[3]);
            int height = (int) (rect[2]);
            if (y + height > image.getHeight()) {
                height = image.getHeight() - y;
            }
            //创建截取脸部的bitmap
            head = Bitmap.createBitmap(image, x, y, width, height, matrix, true);
//            head = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        }else{
            //手机竖屏需旋转270度
            matrix.postRotate(isFrontCamera ? 270 : 90);
//            head = Bitmap.createBitmap(image, iw - (int) rect[1] - (int) rect[3], (int) rect[0],
//                    (int) rect[3], (int) rect[2], matrix, true);
            head = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        }
        BitmapUtil.saveBitmap(head, file);
//        FileOutputStream fos = null;
//        try {
//            YuvImage image = new YuvImage(yuvBytes, ImageFormat.NV21, iw, ih, null);
//            fos = new FileOutputStream(file);
//            image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                assert fos != null;
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private int getCurFaceCount(){
        int count = 0;
        switch (CUR_FACE_DIRECTION){
            case SIDE_LEFT:
                count = 1;
                break;
            case DOWN:
                count = 2;
                break;
            case UP:
                count = 3;
                break;
        }
        return count;
    }

    private void stopCamera(){
        if(mCameraHelper != null){
            mCameraHelper.stopCamera();
        }
    }

    /**
     * 上传图片
     */
    private void uploadFaceImages(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog(FaceRecordActivity.this, "上传照片中");
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    RequestBody body = new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException {

                        }
                    };
                    Request request = new Request.Builder()
                            .url(Config.BASE_HOST + "time")
//                    .post(body)
                            .get()
                            .build();//创建Request 对象
                    Response response = client.newCall(request).execute();//得到Response 对象
                    LogUtils.d("kwwl", "response.code()==" + response.code());
                    if(response.code() == 200){
                        String json = response.body().string();
                        LogUtils.d("kwwl", "response.message()==" + response.message());
                        LogUtils.d("kwwl", "res==" + json);
                        JSONObject data = new JSONObject(json);
                        mHandler.sendMessageDelayed(Message.obtain(mHandler,MSG_UPLOAD_SUCCESS), 5000);
                    }else{
                        mHandler.sendMessageDelayed(Message.obtain(mHandler,MSG_UPLOAD_FAIL), 5000);
                    }
                    response.close();
                }catch (Exception e){
                    e.printStackTrace();
                    mHandler.sendMessageDelayed(Message.obtain(mHandler,MSG_UPLOAD_FAIL), 5000);
                }
            }
        });

    }
}
