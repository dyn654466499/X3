package com.terminus.facerecord.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomEditText extends AppCompatEditText {
	private final String TAG = this.getClass().getSimpleName();
	private Drawable dRight;
	private Drawable dLeft;
	private Rect rBounds;
	private boolean isShowIcon;
	private Context mContext;
	final int DRAWABLE_LEFT = 0;
	final int DRAWABLE_TOP = 1;
	final int DRAWABLE_RIGHT = 2;
	final int DRAWABLE_BOTTOM = 3;


	public CustomEditText(Context paramContext) {
		super(paramContext);
		mContext = paramContext;
		initEditText();
	}

	public CustomEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initEditText();
	}

	public CustomEditText(Context paramContext, AttributeSet paramAttributeSet,
                          int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		initEditText();
	}

	// 初始化edittext 控件
	private void initEditText() {
		if("isShowIcon".equals(getTag()))isShowIcon = true;
		setEditTextDrawable();
		addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
			@Override
			public void afterTextChanged(Editable paramEditable) {
			}

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
				CustomEditText.this.setEditTextDrawable();
			}
		});
	}

	// 控制图片的显示
	public void setEditTextDrawable() {
		if (!isShowIcon) {
			if (getText().toString().length() == 0) {
				setCompoundDrawables(null, null, null, null);
			} else {
				setCompoundDrawables(null, null, dRight, null);
			}
		} else {
			setCompoundDrawables(this.dLeft, null, this.dRight, null);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.dRight = null;
		this.rBounds = null;

	}

	/**
	 * 添加触摸事件 点击之后 出现 清空editText的效果
	 */
	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
			this.rBounds = this.dRight.getBounds();
			int i = (int) paramMotionEvent.getRawX();// 距离屏幕的距离
			if (i > getRight() - 3 * this.rBounds.width()) {
				if (mRightListener != null) {
					mRightListener.onDrawableRightClick(this) ;
				}
				setText("");
				paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		switch (paramMotionEvent.getAction()) {
			case MotionEvent.ACTION_UP:
				if (mRightListener != null) {
					Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT] ;
					if (drawableRight != null && paramMotionEvent.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
						mRightListener.onDrawableRightClick(this) ;
						return true ;
					}
				}

				if (mLeftListener != null) {
					Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT] ;
					if (drawableLeft != null && paramMotionEvent.getRawX() <= (getLeft() + drawableLeft.getBounds().width())) {
						mLeftListener.onDrawableLeftClick(this) ;
						return true ;
					}
				}
				break;
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	/**
	 * 显示右侧X图片的
	 * 
	 * 左上右下
	 */
	@Override
	public void setCompoundDrawables(Drawable paramDrawable1,
                                     Drawable paramDrawable2, Drawable paramDrawable3,
                                     Drawable paramDrawable4) {
		if (paramDrawable3 != null){
			this.dLeft = paramDrawable1;
			this.dRight = paramDrawable3;
		}
		super.setCompoundDrawables(paramDrawable1, paramDrawable2,
				paramDrawable3, paramDrawable4);
	}

	private DrawableLeftListener mLeftListener ;
	private DrawableRightListener mRightListener ;

	public void setDrawableLeftListener(DrawableLeftListener listener) {
		this.mLeftListener = listener;
	}

	public void setDrawableRightListener(DrawableRightListener listener) {
		this.mRightListener = listener;
	}

	public interface DrawableLeftListener {
		void onDrawableLeftClick(View view) ;
	}

	public interface DrawableRightListener {
		void onDrawableRightClick(View view) ;
	}
}
