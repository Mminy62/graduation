package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.ArraySet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {
    public static com.example.proj_graduation.SearchActivity sa;
    ArraySet<ListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListViewItem lv = (ListViewItem) adapterView.getAdapter().getItem(i);
            Toast.makeText(getApplicationContext(), lv.getTitle(), Toast.LENGTH_SHORT).show();
            lv.getTitle();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.list_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // 검색 버튼 누를 때 호출
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 검색창에서 글자가 변경이 일어날 때마다 호출
//                return true;
//            }
//        });

        configureList();
        configureIntent();
    }

    private void configureList() {
        list = new ArraySet<ListViewItem>();
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.ic_arrow),
                "그 해 우리는",
                "설명"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.ic_arrow),
                "오징어 게임",
                "설명"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.ic_arrow),
                "도깨비",
                "설명"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.ic_arrow),
                "사랑의 불시착",
                "설명"));
        list.add(new ListViewItem(ContextCompat.getDrawable(this, R.drawable.ic_arrow),
                "이태원 클라스",
                "설명"));

        adapter = new ListViewAdapter(); //(this, android.R.layout.simple_list_item_1, list);
        adapter.addItems(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    private void configureIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITRY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            doMySearch(query);
        }
    }

    public void doMySearch(String query) {
//        List<String> result = list.stream()
//                .filter(str -> str.trim().contains(query))
//                .collect(Collectors.toList());

//        if (adapter.isEmpty()) {
//            Toast.makeText(SearchActivity.this, "No Match", Toast.LENGTH_SHORT).show();
//        } else {
//            for (String res: result) {
//                adapter.getFilter().filter(res);
////                adapter.in
//            }
//        }
    }
}
