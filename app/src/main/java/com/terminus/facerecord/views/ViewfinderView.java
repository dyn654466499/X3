package com.terminus.facerecord.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public final class ViewfinderView extends View {
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 80L;
	private Paint paint;
	private int scannerAlpha;

	private int i = 0;// ��ӵ�
	private Rect mRect;// ɨ�������߽�
	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = context.getResources().getDisplayMetrics().density;
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(), display.getHeight());
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels * 0.8);
		int height = (int) (width * 1);

		int leftOffset = (screenResolution.x - width) / 2;
		int topOffset = (screenResolution.y - height) / 4;
		mRect = new Rect(leftOffset, topOffset, leftOffset + width,
				topOffset + height);
		scannerAlpha = 0;
	}

	public Rect getRect(){
		return mRect;
	}

	@Override
	public void onDraw(Canvas canvas) {

		Rect frame = mRect;
		if (frame == null) {
			return;
		}
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setAlpha(110);
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// ͷ��
		canvas.drawRect(0, 0, width, frame.top, paint);
		// ���
		canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
		// �ұ�
		canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
		// �ײ�
		canvas.drawRect(0, frame.bottom, width, height, paint);

		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		// �����ĸ���
		paint.setColor(Color.WHITE);
		// ���Ͻ�
		canvas.drawRect(frame.left, frame.top, frame.left + 30 * density,
				frame.top + 5, paint);
		canvas.drawRect(frame.left, frame.top, frame.left + 5,
				frame.top + 30 * density, paint);
		// ���Ͻ�
		canvas.drawRect(frame.right - 30 * density, frame.top, frame.right,
				frame.top + 5, paint);
		canvas.drawRect(frame.right - 5, frame.top, frame.right,
				frame.top + 30 * density, paint);
		// ���½�
		canvas.drawRect(frame.left, frame.bottom - 5, frame.left + 30 * density,
				frame.bottom, paint);
		canvas.drawRect(frame.left, frame.bottom - 30 * density, frame.left + 5,
				frame.bottom, paint);
		// ���½�
		canvas.drawRect(frame.right - 30 * density, frame.bottom - 5, frame.right,
				frame.bottom, paint);
		canvas.drawRect(frame.right - 5, frame.bottom - 30 * density, frame.right,
				frame.bottom, paint);
	}
}
