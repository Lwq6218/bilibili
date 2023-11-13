package com.lwq.bilibili.domain;

import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private Integer total;

    public PageResult(Integer total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
