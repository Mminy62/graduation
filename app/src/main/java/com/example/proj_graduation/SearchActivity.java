package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class SearchActivity extends AppCompatActivity {
    public static com.example.proj_graduation.SearchActivity sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // 검색 버튼 누를 때 호출
                Log.i("ming", "query" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // 검색창에서 글자가 변경이 일어날 때마다 호출
                Log.i("ming", "newText" + newText);

                return true;
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITRY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

//            doMySearch(query);
        }
    }

    public void doMySearch(String str) {
        Log.i("mingming", "doMySearch: " + str);
    }
}
