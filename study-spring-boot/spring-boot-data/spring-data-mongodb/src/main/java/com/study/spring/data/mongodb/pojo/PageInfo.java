package com.study.spring.data.mongodb.pojo;

import java.util.List;

public class PageInfo<T> {

    private List<T> data;
    private int pageSize;
    private int pageIndex;
    private int totalPage;
    private long totalCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getSkip() {
        return pageSize * (pageIndex-1);
    }

    public int getLimit() {
        return pageSize;
    }
}
