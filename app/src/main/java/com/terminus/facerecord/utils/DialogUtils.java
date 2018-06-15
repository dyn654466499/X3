package com.terminus.facerecord.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terminus.facerecord.R;

/**
 * 对话框工具类，后续可扩展
 * @author 邓耀宁
 *
 */
public class DialogUtils {

	/**
	 * 通过命令模式设置Dialog按确定键后所执行的命令。
	 * @param context
	 * @param sureCommand 自定义命令（按确定键执行）
	 */
	public static void showDialog(Context context, String content,
								  String leftText, String rightText, final DialogCommand sureCommand) {
		View layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog,null);
		TextView tv_dialog_content = layout.findViewById(R.id.tv_dialog_content);
		tv_dialog_content.setText(content);
		TextView tv_dialog_sure = layout.findViewById(R.id.tv_dialog_sure);
		TextView tv_dialog_cancel = layout.findViewById(R.id.tv_dialog_cancel);
		final Dialog dialog = new AlertDialog.Builder(context)
				.setCancelable(false)
				.create();
		if(!TextUtils.isEmpty(leftText)){
			tv_dialog_sure.setText(leftText);
		}
		if(!TextUtils.isEmpty(rightText)){
			tv_dialog_cancel.setText(rightText);
		}
		tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(sureCommand != null){
					sureCommand.onLeftConfirm();
				}
			}
		});
		tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(sureCommand != null){
					sureCommand.onRightConfirm();
				}
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().setLayout(CommonUtils.dip2px(context,270), LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 通过命令模式设置Dialog按确定键后所执行的命令。
	 * @param context
	 * @param sureCommand 自定义命令（按确定键执行）
	 */
	public static void showSureDialog(Context context, String content, final DialogCommand sureCommand) {
		View layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog_one_sure,null);
		TextView tv_dialog_content = layout.findViewById(R.id.tv_dialog_content);
		tv_dialog_content.setText(content);
		TextView tv_dialog_sure = layout.findViewById(R.id.tv_dialog_sure);
		final Dialog dialog = new AlertDialog.Builder(context)
				.setCancelable(false)
				.create();
		tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(sureCommand != null){
					sureCommand.onLeftConfirm();
				}
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().setLayout(CommonUtils.dip2px(context,270), LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 通过Dialog显示自定义View的内容
	 * @param context
	 * @param title
	 * @param message
	 * @param view
	 */
	public static void showDialog(Context context, String title, String message, View view) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setView(view)
				.setCancelable(false)
				.create();
		dialog.show();
	}


	public static Dialog showDialog(Context context, String title, String message, boolean cancelable, final DialogCommand sureCommand) {
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setCancelable(cancelable)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if(sureCommand != null){
									sureCommand.onLeftConfirm();
								}
							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if(sureCommand != null){
									sureCommand.onRightConfirm();
								}
							}
						}).create();
		dialog.show();
		return dialog;
	}

	public interface DialogCommand{
		void onLeftConfirm();
		void onRightConfirm();
	}
}
