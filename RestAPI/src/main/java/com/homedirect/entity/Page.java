package com.homedirect.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

	private int pageNo;
	private int pageSize;
	private float totalElements;
	private float totalPage;
	private List<T> content;

	public Page(int pageNo, int pageSize, float totalElements, float totalPage) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPage = totalPage;
	}

//	public Page(int pageNo, int pageSize, float totalElements, float totalPage, List<T> data) {
//		this.pageNo = pageNo;
//		this.pageSize = pageSize;
//		this.content = data;
//		this.totalElements = totalElements;
//		this.totalPage = totalPage;
//	}

//	public Page() {
//	}
//
//	public float getTotalElements() {return totalElements;}
//
//	public void setTotalElements(float totalElements) {this.totalElements = totalElements;}
//
//	public float getTotalPage() {return totalPage;}
//
//	public void setTotalPage(float totalPage) {this.totalPage = totalPage;}
//
//	public int getPageNo() {return pageNo;}
//
//	public void setPageNo(int pageNo) {this.pageNo = pageNo;}
//
//	public int getPageSize() {return pageSize;}
//
//	public void setPageSize(int pageSize) {this.pageSize = pageSize;}
//
//	public List<T> getContent() {return content;}
//
//	public void setContent(List<T> data) {this.content = data;}
}
