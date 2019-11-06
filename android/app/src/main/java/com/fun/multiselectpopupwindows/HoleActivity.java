package com.fun.multiselectpopupwindows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoleActivity extends AppCompatActivity {

    TextView tvhole1;
    public List<Person> PersonList = new ArrayList<>();
//    public  List<Person> PersonList_1 = new ArrayList<>();
    public  static PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //TODO: 将获得的数据显示在界面上
        //
        initPerson();
        adapter = new PersonAdapter(HoleActivity.this, R.layout.activity_item, PersonList);
        ListView listView = (ListView) findViewById(R.id.list_view);
//        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        //
//        tvhole1.setText(holedata);
    }
    private void initPerson(){
        //填充array 数据
        //  处理数据
        //TODO: 获得控件对象
//        tvhole1 = (TextView) findViewById(R.id.tv_hole1);
        //TODO: 获得上一个Activity发送来的数据
        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getBundleExtra("HoleInfo");
        String holedata = bundle.getString("holedata");
        String[] strArr = holedata.split("\n");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(strArr));
        //
        for (int i = 0; i < list.size(); i++)
        {
//                            System.out.println(list.get(i));

            PersonList.add(new Person(list.get(i)));
        }
//        Person person = new Person("Alex");
//        PersonList.add(person);
    }
//    public void onStartActivityWithData(View v) {
//        Intent intent = new Intent(HoleActivity.this, MainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("holedataupdate", tv_hole.getText().toString());
//        intent.putExtra("HoleInfoupdate", bundle);
//        startActivity(intent);
//    }
}
