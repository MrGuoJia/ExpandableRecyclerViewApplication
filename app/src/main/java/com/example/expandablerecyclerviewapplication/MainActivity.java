package com.example.expandablerecyclerviewapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expandablerecyclerviewapplication.custom.SlideRecyclerView;
import com.example.expandablerecyclerviewapplication.secondListAdapter.DataTree;
import com.example.expandablerecyclerviewapplication.secondListAdapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnParentItemClickListener, RecyclerAdapter.OnSubItemClickListener {

    private SlideRecyclerView ry;
    private List<DataTree<String, String>> dataTrees = new ArrayList<>();
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ry = findViewById(R.id.ry);

        initAdapter();
    }

    private void initAdapter() {

        List<String> professionList1 = new ArrayList<>();
        professionList1.add("Android 开发工程师");
        professionList1.add("后台开发");

        List<String> professionList2 = new ArrayList<>();
        professionList2.add("WEB前端工程师");

        dataTrees.add(new DataTree<String, String>("JAVA", professionList1));
        dataTrees.add(new DataTree<>("JavaScript", professionList2));
        //二级列表初始化

        adapter = new RecyclerAdapter(this, this, this);
        ry.setLayoutManager(new LinearLayoutManager(this));
        ry.setAdapter(adapter);

        adapter.setData(dataTrees);

        //这个是实现填充数据后，默认展开某个父级列表下的子item
        // 传入的下标超过数据集合的索引则默认不打开，可不调用该方法
        adapter.expand(0);

    }

    @Override
    public void onParentClick(View view, int groupItemIndex) {
        switch (view.getId()) {

            case R.id.tv_delete://父item左滑删除按钮
                ry.closeMenu();//将侧滑删除按钮关闭
                Toast.makeText(this, "删除：" + (groupItemIndex + 1), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onChildClick(int groupItemIndex, int subItemIndex) {
        Toast.makeText(this, "点击二级列表：" +
                        dataTrees.get(groupItemIndex).getSubItems().get(subItemIndex),
                Toast.LENGTH_SHORT).show();
    }
}
