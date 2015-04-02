package com.example.carmepreviewdemo.view.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.carmepreviewdemo.R;
import com.example.carmepreviewdemo.util.FilterManager;
import com.example.carmepreviewdemo.view.adapter.FilterTypeAdapter;

public class FilterPicker {
	private Context mContext;
	private PopupWindow mPopupWindow;
	private View parent;
	private OnItemClickListener mItemClickListener;

	public FilterPicker(Context mContext, View parent,
			OnItemClickListener mItemClickListener) {
		this.mContext = mContext;
		this.parent = parent;
		this.mItemClickListener = mItemClickListener;
		createPopupWindow(parent);
	}

	public void showPopupWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.showAsDropDown(parent);
		}
	}

	public void dismissPopupWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	@SuppressWarnings("deprecation")
	private void createPopupWindow(View parent) {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(createMyUI(), parent.getWidth(),
					LayoutParams.WRAP_CONTENT);
		}
		mPopupWindow.setFocusable(true);// 使其聚集
		mPopupWindow.setOutsideTouchable(true);// 设置是否允许在外点击消失
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句。
		mPopupWindow.update();
		mPopupWindow.showAsDropDown(parent);
	}

	private View createMyUI() {
		View view = View.inflate(mContext,
				R.layout.filterpicker_type_pop_listview, null);
		ListView mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setAdapter(new FilterTypeAdapter(mContext, FilterManager
				.builderFilterInfoList()));
		mListView.setOnItemClickListener(mItemClickListener);
		return view;
	}
}
