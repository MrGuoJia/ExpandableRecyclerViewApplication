package com.example.expandablerecyclerviewapplication.secondListAdapter;

import java.util.List;
/**
 * 二级列表数据格式
 */
public class DataTree<K, V> {
    private K groupItem;//第一级
    private List<V> subItems;//第二级

    public DataTree(K groupItem, List<V> subItems) {
        this.groupItem = groupItem;
        this.subItems = subItems;
    }

    public K getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(K groupItem) {
        this.groupItem = groupItem;
    }

    public List<V> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<V> subItems) {
        this.subItems = subItems;
    }
}
