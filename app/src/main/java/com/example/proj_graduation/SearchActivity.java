package com.example.proj_graduation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ArraySet<ListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;


    private final AdapterView.OnItemClickListener itemClickListener = (adapterView, view, i, l) -> {
//        ListViewItem lv = (ListViewItem) adapterView.getAdapter().getItem(i);
//        lv.getTitle();

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
        List dramaList = jsonParsing(getJsonString());

        for (int i=0; i<dramaList.size(); i++){
            Drama drama = (Drama) dramaList.get(i);
            list.add(new ListViewItem(
                    drama.getImageURL(),
                    drama.getName(),
                    drama.getDesc())
            );
        }

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

    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("spot.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }

    private List jsonParsing(String json)
    {
        List dramaList = new ArrayList();
        try{
            JSONObject jsonObject = new JSONObject(json);
//            JSONArray dramaTitles = jsonObject.names();
            JSONArray spotArray = jsonObject.getJSONArray("drama");
//            for (int i=0 ; i < dramaTitles.length() ; i++) {
//                JSONArray spotArray = jsonObject.getJSONArray(dramaTitles.getString(i));
//
                for (int j=0; j<spotArray.length(); j++)
                {
                    JSONObject spotObject = spotArray.getJSONObject(j);

                    Drama drama = new Drama();

                    drama.setName(spotObject.getString("name"));
                    drama.setDesc(spotObject.getString("desc"));
                    drama.setImageURL(spotObject.getString("poster"));
                    drama.setSpots(spotObject.getJSONArray("spots"));
                    dramaList.add(drama);
                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("@@@@", String.valueOf(dramaList));

        return dramaList;
    }
}
