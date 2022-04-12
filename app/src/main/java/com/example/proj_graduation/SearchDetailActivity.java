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
        list.add(new ListViewItem("https://ww.namu.la/s/0ec826b267ef49b1aca68a00ebec19ef4b0f719378d1ae6dad1dcb03902358753ca1b7b702622850d6b603256cfe2d5fb292e8a52c6b96721c44ee5190e52f2df1977b0e5f7584f91491946be4da64858096a20ba356cd81da9a44eeb9b30435",
                "서울 중앙 고등학교",
                "연수와 웅의 고등학생 시절"));
        list.add(new ListViewItem("https://img1.daumcdn.net/thumb/R1280x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/clgS/image/mJxQLof_fqhZZHkm_gFfiFa__d8.png",
                "수원 화성 - 방화수류정",
                "피크닉 데이트"));
        list.add(new ListViewItem("https://blog.kakaocdn.net/dn/ELmyJ/btrqkUnusqF/PL7ApwGRzJCj1DUf67W960/img.png",
                "논산 온빛 자연휴양림",
                "납치되어 갔던 곳"));
        list.add(new ListViewItem("https://static.hubzum.zumst.com/hubzum/2022/01/21/13/89292c00239141c08bdc4a99ff476a5e.png",
                "고인돌 공원",
                "소나기를 피하던 장소"));

        adapter = new ListViewAdapter();
        adapter.addItems(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }
}
