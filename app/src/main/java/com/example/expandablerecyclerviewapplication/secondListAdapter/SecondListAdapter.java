package com.example.expandablerecyclerviewapplication.secondListAdapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class SecondListAdapter<GVH, SVH extends RecyclerView.ViewHolder> extends RecyclerView
        .Adapter<RecyclerView.ViewHolder> {
    private List<Boolean> groupItemStatus = new ArrayList<>();

    private List<DataTree> dataTrees = new ArrayList<>();


    /**
     * 用来通知刷新某个父view是否展开其子列表
     */
    public void openParentItem(int index) {
        expandOneItem(index);
    }

    private void expandOneItem(int index) {
        if (index > dataTrees.size() - 1) return;//超过下标默认不打
        groupItemStatus.clear();
        for (int i = 0; i < dataTrees.size(); i++) {
            if (index == i) {
                groupItemStatus.add(true);
            } else {
                groupItemStatus.add(false);
            }

        }
    }


    /**
     * 用于刷新数据
     */
    public void notifyNewData(List dataTrees) {
        setDataTrees(dataTrees);
    }

    private void setDataTrees(List dataTrees) {
        this.dataTrees = dataTrees;
        initGroupItemStatus(groupItemStatus);
        notifyDataSetChanged();
    }

    /**
     * 刷新数据，初始化 ，将所有列表的状态都重置为False,不展开列表
     */
    private void initGroupItemStatus(List<Boolean> groupItemStatus) {
        for (int i = 0; i < dataTrees.size(); i++) {
            groupItemStatus.add(false);
        }
    }


    /**
     * 二级列表的父holder
     */
    public abstract RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent);


    /**
     * 二级列表的子holder
     */
    public abstract RecyclerView.ViewHolder subItemViewHolder(ViewGroup viewGroup);


    /**
     * 根据viewType 选择实现不同的项目布局
     */

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == ItemStatus.VIEW_TYPE_GROUPITEM) {

            viewHolder = groupItemViewHolder(parent);

        } else if (viewType == ItemStatus.VIEW_TYPE_SUBITEM) {

            viewHolder = subItemViewHolder(parent);
        }

        return viewHolder;
    }


    /**
     * 抽象方法 设置绑定父类Holder 与获取其下标位置
     */
    public abstract void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int
            groupItemIndex, boolean ifExpand);


    /**
     * 抽象方法 设置绑定子类Holder 与获取其下标位置
     */
    public abstract void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int
            groupItemIndex, int subItemIndex);


    /**
     * 抽象方法 实现二级列表父item是否点击
     */
    public abstract void onGroupItemClick(Boolean isExpand, GVH holder, int groupItemIndex);


    /**
     * 抽象方法 实现二级列表子item的点击
     */
    public abstract void onSubItemClick(SVH holder, int groupItemIndex, int subItemIndex);


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ItemStatus itemStatus = getItemStatusByPosition(position);
        final DataTree dt = dataTrees.get(itemStatus.getGroupItemIndex());

        if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_GROUPITEM) {

            final int groupItemIndex = itemStatus.getGroupItemIndex();
            onGroupItemBindViewHolder(holder, itemStatus.getGroupItemIndex(), groupItemStatus.get(groupItemIndex));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!groupItemStatus.get(groupItemIndex)) {

                        onGroupItemClick(false, (GVH) holder, groupItemIndex);

                        groupItemStatus.set(groupItemIndex, true);
                        notifyItemRangeInserted(holder.getAdapterPosition() + 1, dt.getSubItems
                                ().size());


                    } else {

                        onGroupItemClick(true, (GVH) holder, groupItemIndex);

                        groupItemStatus.set(groupItemIndex, false);
                        notifyItemRangeRemoved(holder.getAdapterPosition() + 1, dt.getSubItems
                                ().size());

                    }

                }
            });

        } else if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_SUBITEM) {

            onSubItemBindViewHolder(holder, itemStatus.getGroupItemIndex(), itemStatus
                    .getSubItemIndex());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onSubItemClick((SVH) holder, itemStatus.getGroupItemIndex(), itemStatus.getSubItemIndex());

                }
            });

        }
    }

    /**
     * 每行该显示哪种item
     */

    @Override
    public int getItemViewType(int position) {
        return getItemStatusByPosition(position).getViewType();
    }


    /***
     * 统计所有的item数量
     * */
    @Override
    public final int getItemCount() {

        int itemCount = 0;

        if (groupItemStatus.size() == 0) {
            return 0;
        }

        for (int i = 0; i < dataTrees.size(); i++) {

            if (groupItemStatus.get(i)) {
                itemCount += dataTrees.get(i).getSubItems().size() + 1;
            } else {
                itemCount++;
            }

        }


        return itemCount;
    }

    /**
     * 根据当前索引 判断是一级还是二级的item
     */
    private ItemStatus getItemStatusByPosition(int position) {

        ItemStatus itemStatus = new ItemStatus();

        int count = 0;
        int i = 0;

        for (i = 0; i < groupItemStatus.size(); i++) {

            if (count == position) {

                itemStatus.setViewType(ItemStatus.VIEW_TYPE_GROUPITEM);
                itemStatus.setGroupItemIndex(i);
                break;

            } else if (count > position) {

                itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUBITEM);
                itemStatus.setGroupItemIndex(i - 1);
                itemStatus.setSubItemIndex(position - (count - dataTrees.get(i - 1).getSubItems
                        ().size()));
                break;

            }

            count++;

            if (groupItemStatus.get(i)) {

                count += dataTrees.get(i).getSubItems().size();

            }


        }

        if (i >= groupItemStatus.size()) {
            itemStatus.setGroupItemIndex(i - 1);
            itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUBITEM);
            itemStatus.setSubItemIndex(position - (count - dataTrees.get(i - 1).getSubItems().size
                    ()));
        }
        return itemStatus;
    }
}
