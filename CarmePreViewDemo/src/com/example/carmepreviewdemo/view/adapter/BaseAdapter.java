package com.example.carmepreviewdemo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class BaseAdapter<T> extends ArrayAdapter<T> {

	protected ArrayList<T> mDatas = new ArrayList<T>();
	protected Context mContext;

	protected LayoutInflater mLayoutInflater;

	public BaseAdapter(Context context, ArrayList<T> mDatas) {
		super(context, 0);
		this.mContext = context;
		this.mDatas = mDatas;

		mLayoutInflater = LayoutInflater.from(mContext);
	}

	protected void setDefaultDrawable(int resId) {
	}

	@Override
	public int getCount() {
		if (mDatas == null) {
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return this.getView(position, convertView);
	}

	protected View getView(final int position, View convertView) {
		final Holder aHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(getResourceId(), null);
			aHolder = (Holder) getViewHolder(convertView);
			convertView.setTag(aHolder);
		} else {
			aHolder = (Holder) convertView.getTag();
		}
		setViewData(convertView, aHolder, position);
		return convertView;
	}

	protected abstract int getResourceId();

	protected abstract void setViewData(View convertView, Holder holder,
			int position);

	protected abstract Holder getViewHolder(View convertView);

	public void cleanViewMap() {
		this.mDatas.clear();
	}

	public interface Holder {

	}

	final protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
