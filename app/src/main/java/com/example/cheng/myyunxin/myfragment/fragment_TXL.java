package com.example.cheng.myyunxin.myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cheng.myyunxin.R;
import com.example.cheng.myyunxin.mybean.Bean_TXL;

import java.util.ArrayList;

/**
 * Created by a452542253 on 2016/11/15.
 */

public class fragment_TXL extends Fragment {
    private String[] xitong = {"验证提醒", "讨论组", "高级群", "黑名单", "我的电脑"};
    private int[] xitongid = {R.mipmap.icon_verify_remind, R.mipmap.ic_secretary, R.mipmap.ic_advanced_team, R.mipmap.ic_black_list, R.mipmap.ic_my_computer};
    private ListView lv_xitong;
    private ArrayList<Bean_TXL> data = new ArrayList<>();
    private MyAdapter adapter;

    private ListView lv_haoyou;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_txl, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //初始化系统的listview中的数据
        initData();
        //初始化系统中的adapter
        initAdapter();

        initdata_hao();
    }

    private void initdata_hao() {

    }

    private void initAdapter() {
        adapter = new MyAdapter();
        lv_xitong.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < xitongid.length; i++) {
            data.add(new Bean_TXL(xitong[i], xitongid[i]));
        }
    }

    private void initView(View view) {
        lv_xitong = (ListView) view.findViewById(R.id.lv_xitong);
        lv_haoyou = (ListView) view.findViewById(R.id.lv_haoyou);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.txl_lv_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv.setImageResource(data.get(position).getImg());
            holder.tv.setText(data.get(position).getName());

            return convertView;
        }


        public class ViewHolder {
            ImageView iv;
            TextView tv;

            public ViewHolder(View rootView) {
                iv = (ImageView) rootView.findViewById(R.id.iv_touxiang);
                tv = (TextView) rootView.findViewById(R.id.tv_name);
            }

        }
    }
}
