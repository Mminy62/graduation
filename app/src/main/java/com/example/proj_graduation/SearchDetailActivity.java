package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SearchDetailActivity extends AppCompatActivity {
    ArraySet<ListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;

    private AdapterView.OnItemClickListener itemClickListener = (adapterView, view, i, l) -> {
//        ListViewItem lv = (ListViewItem) adapterView.getAdapter().getItem(i);
//        lv.getTitle();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent,1);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        listView = (ListView) findViewById(R.id.list_view);

        configureList();
    }

    private void configureList() {
        list = new ArraySet<>();
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.loc_logo),
                "서울 중앙 고등학교",
                "연수와 웅의 고등학생 시절"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.loc_logo),
                "수원 화성",
                "피크닉 데이트"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.loc_logo),
                "논산 온빛 자연휴양림",
                "납치되어 갔던 곳"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.loc_logo),
                "고인돌 공원",
                "소나기를 피하던 장소"));

        adapter = new ListViewAdapter();
        adapter.addItems(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }
}
