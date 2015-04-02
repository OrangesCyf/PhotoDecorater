package com.example.carmepreviewdemo.view.adapter;

import java.util.ArrayList;

import com.example.carmepreviewdemo.R;
import com.example.carmepreviewdemo.model.FilterInfo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class FilterTypeAdapter extends BaseAdapter<FilterInfo> {

	public FilterTypeAdapter(Context context, ArrayList<FilterInfo> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId() {
		// TODO Auto-generated method stub
		return R.layout.filterpicker_type_list_item;
	}

	@Override
	protected void setViewData(View convertView, Holder holder, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) holder;
		viewHolder.filterName.setText(mDatas.get(position).getFilterName());
	}

	@Override
	protected Holder getViewHolder(View convertView) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.filterName = (TextView) convertView
				.findViewById(R.id.item_tv);
		return viewHolder;
	}

	class ViewHolder implements Holder {
		TextView filterName;
	}

}