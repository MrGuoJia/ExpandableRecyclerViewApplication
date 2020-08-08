package com.example.expandablerecyclerviewapplication.secondListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expandablerecyclerviewapplication.R;

import java.util.List;

public class RecyclerAdapter extends SecondListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder> {

    private Context context;

    private List<DataTree<String, String>> dts;
    private OnParentItemClickListener parentItemClickListener;
    private OnSubItemClickListener subItemClickListener;
    private int expandIndex = -1;//打开某个父级item的子列表的下标，默认不打开

    public RecyclerAdapter(Context context, OnParentItemClickListener parentItemClickListener, OnSubItemClickListener subItemClickListener) {
        this.context = context;
        this.parentItemClickListener = parentItemClickListener;
        this.subItemClickListener = subItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_level, parent, false);
        return new GroupItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_second_level, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, final int groupItemIndex, boolean ifExpand) {
        ((GroupItemViewHolder) holder).tv_group.setText("第" + (groupItemIndex + 1) + "个" + dts.get(groupItemIndex).getGroupItem());
        ((GroupItemViewHolder) holder).tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentItemClickListener.onParentClick(v, groupItemIndex);
            }
        });

        if (groupItemIndex == expandIndex)
            ((GroupItemViewHolder) holder).img_right.setRotation(ifExpand ? 90 : 0);


    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        ((SubItemViewHolder) holder).tv_name.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex));

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
        //isExpand:false 展开  true：收起来
        holder.img_right.setRotation(isExpand ? 0 : 90);
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        subItemClickListener.onChildClick(groupItemIndex, subItemIndex);
    }


    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_group, tv_delete;
        ImageView img_right;
        LinearLayout ly_parent;

        public GroupItemViewHolder(View itemView) {
            super(itemView);

            tv_group = itemView.findViewById(R.id.tv_group);
            img_right = itemView.findViewById(R.id.img_right);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            ly_parent = itemView.findViewById(R.id.ly_parent);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }


    public void setData(List data) {
        dts = data;
        notifyNewData(dts);

    }


    public void expand(int position) {
        expandIndex = position;
        openParentItem(position);
        notifyNewData(dts);
    }


    /**
     * 父列表控件点击监听
     */
    public interface OnParentItemClickListener {
        void onParentClick(View view, int groupItemIndex);
    }

    /**
     * 二级列表整个item点击监听
     */
    public interface OnSubItemClickListener {
        void onChildClick(int groupItemIndex, int subItemIndex);
    }
}
