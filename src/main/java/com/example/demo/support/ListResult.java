package com.example.demo.support;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组、列表类型统一返回结果定义.
 *
 * @param <T> 类型
 * @author yuan.cheng
 */
@Data
@AllArgsConstructor
class ListResult<T> {

    private List<T> items;

    public void addData(T item) {
        this.items.add(item);
    }

    public ListResult() {
        items = new ArrayList<>();
    }
}
