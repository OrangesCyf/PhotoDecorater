package com.example.carmepreviewdemo.util;

import java.util.ArrayList;

import HaoRan.ImageFilter.CleanGlassFilter;
import HaoRan.ImageFilter.ComicFilter;
import HaoRan.ImageFilter.FilmFilter;
import HaoRan.ImageFilter.FocusFilter;
import HaoRan.ImageFilter.Gradient;
import HaoRan.ImageFilter.LomoFilter;
import HaoRan.ImageFilter.PaintBorderFilter;
import HaoRan.ImageFilter.SceneFilter;

import com.example.carmepreviewdemo.model.FilterInfo;

public class FilterManager {

	private static ArrayList<FilterInfo> mFilterInfoList = new ArrayList<FilterInfo>();

	public static ArrayList<FilterInfo> builderFilterInfoList() {
		if (mFilterInfoList.isEmpty()) {
			mFilterInfoList.add(new FilterInfo(new ComicFilter(), "滤镜一"));
			mFilterInfoList.add(new FilterInfo(new SceneFilter(5f, Gradient
					.Scene()), "滤镜二"));
			mFilterInfoList.add(new FilterInfo(new SceneFilter(5f, Gradient
					.Scene1()), "滤镜三"));
			mFilterInfoList.add(new FilterInfo(new SceneFilter(5f, Gradient
					.Scene2()), "滤镜四"));
			mFilterInfoList.add(new FilterInfo(new SceneFilter(5f, Gradient
					.Scene3()), "滤镜五"));
			mFilterInfoList.add(new FilterInfo(new FilmFilter(80f), "滤镜六"));
			mFilterInfoList.add(new FilterInfo(new FocusFilter(), "滤镜七"));
			mFilterInfoList.add(new FilterInfo(new CleanGlassFilter(), "滤镜八"));
			mFilterInfoList.add(new FilterInfo(new PaintBorderFilter(0x00FF00),
					"滤镜九"));
			mFilterInfoList.add(new FilterInfo(new PaintBorderFilter(0x00FFFF),
					"滤镜十"));
			mFilterInfoList.add(new FilterInfo(new PaintBorderFilter(0xFF0000),
					"滤镜十一"));
			mFilterInfoList.add(new FilterInfo(new LomoFilter(), "滤镜十二"));
		}
		return mFilterInfoList;
	}

}
