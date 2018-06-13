package com.terminus.facerecord.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

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
	 * @param title
	 * @param message
	 * @param sureCommand 自定义命令（按确定键执行）
	 */
	public static void showDialog(Context context, String title, String message, final DialogCommand sureCommand) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// TODO Auto-generated method stub
								if(sureCommand != null){
									sureCommand.onSure();
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
									sureCommand.onCancel();
								}
							}
						}).create().show();
	}

	/**
	 * 通过命令模式设置Dialog按确定键后所执行的命令。
	 * @param context
	 * @param title
	 * @param message
	 * @param sureCommand 自定义命令（按确定键执行）
	 */
	public static void showSureDialog(Context context, String title, String message, final DialogCommand sureCommand) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								// TODO Auto-generated method stub
								if(sureCommand != null){
									sureCommand.onSure();
								}
							}
						})
				.create().show();
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
									sureCommand.onSure();
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
									sureCommand.onCancel();
								}
							}
						}).create();
		dialog.show();
		return dialog;
	}

	public interface DialogCommand{
		void onSure();
		void onCancel();
	}
}
