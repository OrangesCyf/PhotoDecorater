package com.example.carmepreviewdemo.model;

import java.io.Serializable;

import HaoRan.ImageFilter.IImageFilter;

public class FilterInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -71990611766357734L;
	private String filterName;
	private IImageFilter filter;

	public FilterInfo(IImageFilter aFilter, String name) {
		this.filter = aFilter;
		this.filterName = name;
	}

	public void setFilter(IImageFilter filter) {
		this.filter = filter;
	}

	public IImageFilter getFilter() {
		return filter;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

}
