package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.ArraySet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

public class SearchActivity extends AppCompatActivity {
    ArraySet<ListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;


    private AdapterView.OnItemClickListener itemClickListener = (adapterView, view, i, l) -> {
        ListViewItem lv = (ListViewItem) adapterView.getAdapter().getItem(i);
        Toast.makeText(getApplicationContext(), lv.getTitle(), Toast.LENGTH_SHORT).show();
        lv.getTitle();

        Intent intent = new Intent(getApplicationContext(), SearchDetailActivity.class);
        startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.list_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
        }
    }
}
