package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static com.example.proj_graduation.SearchActivity sa;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ListView listView;

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
        list = new ArrayList<>();
        list.add("그 해 우리는");
        list.add("오징어 게임");
        list.add("도깨비");
        list.add("사랑의 볼시착");
        list.add("이태원 클라스");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
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
        if (list.contains(query)) {
            adapter.getFilter().filter(query);
        } else {
            Toast.makeText(SearchActivity.this, "No Match", Toast.LENGTH_SHORT).show();
        }
    }
}
