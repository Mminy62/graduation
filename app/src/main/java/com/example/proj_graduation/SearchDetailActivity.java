package com.example.proj_graduation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArraySet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;


public class SearchDetailActivity extends AppCompatActivity {
    ArraySet<SpotListViewItem> list;
    ListViewAdapter adapter;
    ListView listView;
    TextView textView;
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

        listView = (ListView) findViewById(R.id.list_view);
        textView = (TextView) findViewById(R.id.title_textView);

        Intent intent = getIntent();
        Parcelable[] parcels = intent.getParcelableArrayExtra("spots");
        textView.setText(intent.getStringExtra("name"));
        spots = new Spot[parcels.length];
        for (int i= 0; i < parcels.length; i++){
            spots[i] = (Spot) parcels[i];
        }


        configureList();
    }

    private void configureList() {
        list = new ArraySet<>();
        for (int i= 0; i < spots.length; i++) {
            list.add(new SpotListViewItem(
                    spots[i].getImageURL(),
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
