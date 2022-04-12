package com.example.proj_graduation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArraySet;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchDetailActivity extends AppCompatActivity {
    ArraySet<SpotListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;
    Spot[] spots;

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

        Intent intent = getIntent();
        Parcelable[] parcels = intent.getParcelableArrayExtra("spots");
        spots = new Spot[parcels.length];
        for (int i= 0; i < parcels.length; i++){
            spots[i] = (Spot) parcels[i];
        }

        listView = (ListView) findViewById(R.id.list_view);

        configureList();
    }

    private void configureList() {
        list = new ArraySet<>();
        for (int i= 0; i < spots.length; i++) {
            list.add(new SpotListViewItem("https://ww.namu.la/s/0ec826b267ef49b1aca68a00ebec19ef4b0f719378d1ae6dad1dcb03902358753ca1b7b702622850d6b603256cfe2d5fb292e8a52c6b96721c44ee5190e52f2df1977b0e5f7584f91491946be4da64858096a20ba356cd81da9a44eeb9b30435",
                    spots[i].getName(),
                    spots[i].getDesc(),
                    spots[i].getLatitude(),
                    spots[i].getLongitude())
            );
        }
        adapter = new ListViewAdapter();
        adapter.addSpotItems(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }
}
