package com.fun.multiselectpopupwindows;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fun.multiselectpopupwindows.model.Hole_method;
import com.fun.multiselectpopupwindows.model.Holedata_method;
import com.fun.multiselectpopupwindows.model.Search;
import com.fun.multiselectpopupwindows.widget.MultiSelectPopupWindows;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.net.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
//import java.long.object;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutProductType;
    private List<Search> products;
    private List<Hole_method> holes;
    private List<Holedata_method> holedatas;
    public static List<Holedata_method> holedatas_1;

    private MultiSelectPopupWindows productsMultiSelectPopupWindows;
    private TextView Mtext;
    //

    private Spinner mSpinner;
    private Spinner mSpinner_1;
    private Spinner mSpinner_type;
    private Spinner mSpinner_classes;
    private TextView mTv;
    private TextView mTv_1;
    private TextView mTv_classes;
    private TextView mTv_type;
    private EditText et_s;
    private TextView tv_hole;
    private TextView tv_reality_scale;
    private TextView tv_reality_hole;
    private EditText et_oil;
    private EditText et_oil2;
    private EditText et_drill;
    private TextView tv_reality_num;
    private TextView tv_reality_drill;
    private EditText et_workplace;



//    private
    //


//
    protected final int REQUESTSUCESS = 0;
    protected final int REQUESTNOTFOUND = 1;
    protected final int REQUESTEXCEPTION = 2;

    //get方法传参数到服务器保存
    public String m_people;
    public String s_people;
    public String many_people;
    public String drill;
    public String classes;
    public String drill_name;
    public double int_hole;
//    public String hole_string;
    public static String string1;
    public  String reality_hole;
    public  String reality_scale;



    //
    //
    // 在主线程中定义一个handler
    private Handler handler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            // 区分一下是发送的哪条消息
            switch (msg.what)
            {
                case REQUESTSUCESS:
                    // 代表请求成功
                    String content = (String)msg.obj;
//                    System.out.println("***"+content+"***");
                    //
                    String string = "";
                    String string_type = "";
                    try {
                        //读取sharepreferences值
                        SharedPreferences sf = getSharedPreferences("login_state", MODE_PRIVATE);

                        String classes_1 = sf.getString("classes", "");
                        String m_people_1 = sf.getString("m_people", "");
                        String s_people_1 = sf.getString("s_people", "");
                        String drill_1 = sf.getString("drill", "");
                        //
//                        System.out.println("***"+"try"+"***");
//                        System.out.println(content instanceof String);
//                        System.out.println(content instanceof JSONObject);
                        //String转JSONObject
                        //request后处理respones数据 使用，号
                        JSONObject result = new JSONObject(content);

                        string = result.getString("result");

                        String[] strArr = string.split(",");

                        //其他人员-多选框
                        final ArrayList<String> list = new ArrayList<>(Arrays.asList(strArr));
//                      ------------------------------------result_type_name
                        string_type = result.getString("zjd_type_result");

                        String[] strArr_type = string_type.split(",");

                        //其他人员-多选框
                        ArrayList<String> list_type = new ArrayList<>(Arrays.asList(strArr_type));
//                      -------------------------------------
                        products = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++)
                        {
//                            System.out.println(list.get(i));
                            products.add(new Search(list.get(i), false));
                        }


//                        mTv_classes = (TextView) findViewById(R.id.tv_classes);
//                        System.out.println("***"+list+"***");

                        //设置ArrayAdapter内置的item样式-这里是单行显示样式
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, list);
                        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        //设置Adapter了
                        mSpinner.setAdapter(adapter);
//                        System.out.println("---@"+classes_1+"@---");
                        if (m_people_1!=null){
                            int selectIndex=-1;
                            for (int i=0;i<list.size();i++){
                                if (list.get(i).equals(m_people_1)){
                                    selectIndex=i;
//                                    System.out.println("---@"+selectIndex+"@---");
                                    break;
                                }
                            }
                            adapter.notifyDataSetChanged();
                            mSpinner.setSelection(selectIndex);
                        }
                        //监听Spinner的操作
                        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



                            //选取时候的操作
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                m_people = adapter.getItem(position);

                            }
                            //没被选取时的操作
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
//                                mTv.setText("No anything");
                            }
                        });
                        mSpinner_1.setAdapter(adapter);
//                        System.out.println("---@"+classes_1+"@---");
                        if (s_people_1!=null){
                            int selectIndex=-1;
                            for (int i=0;i<list.size();i++){
                                if (list.get(i).equals(s_people_1)){
                                    selectIndex=i;
//                                    System.out.println("---@"+selectIndex+"@---");
                                    break;
                                }
                            }
                            adapter.notifyDataSetChanged();
                            mSpinner_1.setSelection(selectIndex);
                        }
                        //监听Spinner的操作
                        mSpinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            //选取时候的操作
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                mTv_1.setText(adapter.getItem(position));
                                s_people = adapter.getItem(position);

                            }
                            //没被选取时的操作
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                mTv_1.setText("No anything");
                            }
                        });
                        final ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, list_type);
                        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        //设置Adapter了
                        mSpinner_type.setAdapter(adapter_type);
//                        System.out.println("---@"+classes_1+"@---");
                        if (drill_1!=null){
                            int selectIndex=-1;
                            for (int i=0;i<list_type.size();i++){
                                if (list_type.get(i).equals(drill_1)){
                                    selectIndex=i;
//                                    System.out.println("---@"+selectIndex+"@---");
                                    break;
                                }
                            }
                            adapter_type.notifyDataSetChanged();
                            mSpinner_type.setSelection(selectIndex);
                        }
                        //监听Spinner的操作
                        mSpinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            //选取时候的操作
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                mTv_type.setText(adapter_type.getItem(position));
                                drill = adapter_type.getItem(position);
                            }
                            //没被选取时的操作
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
//                                mTv.setText("No anything");
                            }
                        });
//                       班次
                        ArrayList<String> classes_list = new ArrayList<String>();
                        classes_list.add("---请选择---");
                        classes_list.add("白班");
                        classes_list.add("前夜");
                        classes_list.add("后夜");
                        final ArrayAdapter<String> adapter_classes = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, classes_list);
                        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        //设置Adapter了
                        mSpinner_classes.setAdapter(adapter_classes);
                        //
                        //
                        System.out.println("---@"+classes_1+"@---");
                        if (classes_1!=null){
                            int selectIndex=-1;
                            for (int i=0;i<classes_list.size();i++){
                                if (classes_list.get(i).equals(classes_1)){
                                    selectIndex=i;
                                    System.out.println("---@"+selectIndex+"@---");
                                    break;
                                }
                            }
                            adapter_classes.notifyDataSetChanged();
                            mSpinner_classes.setSelection(selectIndex);
                        }


//                        adapter_classes.notifyDataSetChanged();
//                        mSpinner_classes.setSelection(1);

//                        mSpinner_classes.getChildAt(0).setVisibility(View.INVISIBLE);


                        //
                        //监听Spinner的操作
                        mSpinner_classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            //选取时候的操作
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                mTv_classes.setText(adapter_classes.getItem(position));
                                classes = adapter_classes.getItem(position);

                            }
                            //没被选取时的操作
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
//                                mTv_classes.setText("No anything");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUESTNOTFOUND:
                    Toast.makeText(getApplicationContext(), "请求的资源不存在！", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后......", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    // 在主线程中定义一个handler
    private Handler drillselecthandler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case REQUESTSUCESS:
                    // 代表请求成功
                    String content = (String)msg.obj;
                    //
                    String string_drillname = "";
                    String string_drillfre = "";
                    String string_drillscale = "";
                    try {

                        JSONObject result = new JSONObject(content);

                        string_drillname = result.getString("drillname");
                        string_drillfre = result.getString("drillfre");
                        string_drillscale = result.getString("drillscale");
                        System.out.println("---@"+string_drillname+"--"+string_drillfre+"--"+string_drillscale+"@---");
//                        et_drill = (EditText) findViewById(R.id.et_drill);
                        tv_reality_num = (TextView) findViewById(R.id.tv_reality_num);
                        tv_reality_drill = (TextView) findViewById(R.id.tv_reality_drill);
                        et_drill.setText(string_drillname);
                        tv_reality_num.setText(string_drillfre);
                        tv_reality_drill.setText(string_drillscale);
                        drill_name = et_drill.getText().toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUESTNOTFOUND:
                    Toast.makeText(getApplicationContext(), "请求的资源不存在！", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后......", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    private Handler drilladdhandler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case REQUESTSUCESS:
                    // 代表请求成功
                    String content = (String)msg.obj;
                    //
                    String string_drillname = "";

                    try {

                        JSONObject result = new JSONObject(content);

                        string_drillname = result.getString("drillname");
                        System.out.println("---@"+string_drillname+"@---");
//                        et_drill = (EditText) findViewById(R.id.et_drill);

                        et_drill.setText(string_drillname);

                        drill_name = et_drill.getText().toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUESTNOTFOUND:
                    Toast.makeText(getApplicationContext(), "请求的资源不存在！", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后......", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    private Handler drilldelhandler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case REQUESTSUCESS:
                    // 代表请求成功
                    String content = (String)msg.obj;
                    //
                    String string_result = "";

                    try {

                        JSONObject result = new JSONObject(content);

                        string_result = result.getString("result");

                        System.out.println("---@"+string_result+"@---");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUESTNOTFOUND:
                    Toast.makeText(getApplicationContext(), "请求的资源不存在！", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后......", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    private Handler resulr_handler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case REQUESTSUCESS:
                    // 代表请求成功
                    String content = (String)msg.obj;
                    //
                    String string_result = "";

                    try {

                        JSONObject result = new JSONObject(content);

                        string_result = result.getString("result");

                        System.out.println("-1-@"+string_result+"@--1-");
                        if(string_result.equals("ok")){
                            Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(string_result.equals("okok")){
                            Toast.makeText(getApplicationContext(), "请勿重复上传", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(string_result.equals("exists")){
                            Toast.makeText(getApplicationContext(), "已存在,请勿重复上传", Toast.LENGTH_SHORT).show();
                            break;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUESTNOTFOUND:
                    Toast.makeText(getApplicationContext(), "请求的资源不存在1！", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTEXCEPTION:
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后1......", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    private Context context;

    //---
    // 在主线程中定义一个handler
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        int_hole = 0;
        //spinner
//        tv_hole = (TextView) findViewById(R.id.tv_hole);
        //

        //
        mSpinner = (Spinner) findViewById(R.id.sp_btn);
        mSpinner_1 = (Spinner) findViewById(R.id.sp_btn1);
        mSpinner_type = (Spinner) findViewById(R.id.sp_btn_drill);
        mSpinner_classes = (Spinner) findViewById(R.id.sp_btn_classes);
        //只是为了展示我们的实现效果，故可不要
        mTv = (TextView) findViewById(R.id.tv_content);
        mTv_1 = (TextView) findViewById(R.id.tv_content1);
        mTv_type = (TextView) findViewById(R.id.tv_content_drill);
        //

        mTv_classes = (TextView) findViewById(R.id.tv_classes);
        et_workplace = (EditText) findViewById((R.id.et_workplace));
        Mtext = (TextView) findViewById(R.id.text_line_value);
        tv_hole = (TextView) findViewById(R.id.tv_hole);
        tv_reality_scale = (TextView) findViewById(R.id.tv_reality_scale);
        tv_reality_hole = (TextView) findViewById(R.id.tv_reality_hole);
        et_oil = (EditText) findViewById((R.id.et_oil));
        et_oil2 = (EditText) findViewById((R.id.et_oil2));
        et_drill = (EditText) findViewById(R.id.et_drill);

        updateState();
        //
        holes = new ArrayList<>();
        holedatas = new ArrayList<>();
        holedatas_1 = new ArrayList<>();
        String string1 ="";
        linearLayoutProductType = (LinearLayout) findViewById(R.id.linearLayout_product_type);
        linearLayoutProductType.setOnClickListener(this);
//        getData();
        // 创建一个子线程
        new Thread()
        {
            public void run()
            {
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/test1");
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置发送get请求
                    connection.setRequestMethod("GET"); // get要求大小，其实默认就是get请求
                    // 设置请求超时时间
                    connection.setReadTimeout(5000);
                    // 获取服务器返回的状态码
                    int code = connection.getResponseCode();
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        // 用我们创建的handler助手给系统发消息
                        handler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;
                        handler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
//    多人

    public void onClick(View v) {

        productsMultiSelectPopupWindows = new MultiSelectPopupWindows(this, linearLayoutProductType, 110, products);


        productsMultiSelectPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                ArrayList <String> arlist = new ArrayList <String>();
//                System.out.println("\n----------------------");
                for(int i = 0 ; i<products.size();i++){
//                    System.out.println(products.get(i).isChecked());
                    if (products.get(i).isChecked() == true){
                        arlist.add(products.get(i).getKeyWord());
//                        System.out.println(products.get(i).getKeyWord());

                    }
                }


//                System.out.println("\n----------------------");

                Toast.makeText(MainActivity.this, arlist.toString(), Toast.LENGTH_SHORT).show();
//                Mtext = (TextView) findViewById(R.id.text_line_value);
                if(arlist.size()==0){
                    Mtext.setText("");
                    many_people = "";
                }
                else{
                    String result = arlist.get(0);
                    for(int i = 1; i < arlist.size(); i++) {
                        result = result + "," + arlist.get(i);
                    }
                    Mtext.setText(result);
                    many_people = result;
                }

            }
        });
    }
    public void onSub(View v) {

//        getData();
        // 创建一个子线程
        new Thread()
        {
            public void run()
            {
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/up_zjd?"+
                            "m_people="+m_people+"&"+
                            "s_people="+s_people+"&"+
                            "many_people="+URLEncoder.encode(many_people,"UTF-8"));
                    System.out.println("http://192.168.1.5:8000/up_zjd?"+
                            "m_people="+m_people+"&"+
                            "s_people="+s_people+"&"+
                            "many_people="+ URLEncoder.encode(many_people,"UTF-8"));
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置发送get请求
                    connection.setRequestMethod("GET"); // get要求大小，其实默认就是get请求
                    // 设置请求超时时间
                    connection.setReadTimeout(5000);
                    // 获取服务器返回的状态码
                    int code = connection.getResponseCode();
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        // 用我们创建的handler助手给系统发消息
                        handler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;
                        handler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
    public void onSub1(View v)
    {

//        getData();
        // 创建一个子线程
        new Thread()
        {
            public void run()
            {
                HttpURLConnection connection=null;
                BufferedOutputStream os=null;
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/up_zjd");
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    os = new BufferedOutputStream(connection.getOutputStream());
//                    System.out.println("***"+URLEncoder.encode(many_people,"UTF-8")+"***");
                    String string_par = "m_people="+URLEncoder.encode(m_people,"UTF-8")+"&"+
                                        "s_people="+URLEncoder.encode(s_people,"UTF-8")+"&"+
                                        "many_people="+ URLEncoder.encode(Mtext.getText().toString(),"UTF-8")+"&"+
                                        "classes="+ URLEncoder.encode(classes,"UTF-8")+"&"+
                                        "workplace="+ URLEncoder.encode(et_workplace.getText().toString(),"UTF-8")+"&"+
                            //
                                        "drill_type="+ URLEncoder.encode(drill,"UTF-8")+"&"+
//                                        "Accumulated_scale="+ URLEncoder.encode(tv_reality_drill.getText().toString(),"UTF-8")+"&"+
//                                        "Accumulated_frequency="+ URLEncoder.encode(tv_reality_num.getText().toString(),"UTF-8")+"&"+
                                        "drill_id="+ URLEncoder.encode(et_drill.getText().toString(),"UTF-8")+"&"+
                            //

                                        "scale="+ URLEncoder.encode(tv_reality_scale.getText().toString(),"UTF-8")+"&"+
                                        "frequency="+ URLEncoder.encode(tv_reality_hole.getText().toString(),"UTF-8")+"&"+
                                        "holetext="+ URLEncoder.encode(tv_hole.getText().toString(),"UTF-8")+"&"+
                                        //
                                        "oil="+ URLEncoder.encode(et_oil.getText().toString(),"UTF-8")+"&"+
                                        "oil2="+ URLEncoder.encode(et_oil2.getText().toString(),"UTF-8")
                                        ;
                    System.out.println("***"+string_par+"***");
                    byte[] temp = string_par.getBytes();
                    os.write(temp, 0, temp.length);
                    os.flush();
                    os.close();
                    InputStream is = connection.getInputStream();
//                    is.close();
                    int code = connection.getResponseCode();
                    System.out.println("*code**"+code+"*code**");
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
//                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        // 用我们创建的handler助手给系统发消息
                        resulr_handler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;
                        resulr_handler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    resulr_handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public void onSubH(View v) {
        et_s = (EditText) findViewById(R.id.et_scale);
//        TextView tv_hole = (TextView) findViewById(R.id.tv_hole);
        tv_hole.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_hole.setText(" ");
        String s_et_s= et_s.getText().toString();
        //
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String current = df.format(System.currentTimeMillis());
        System.out.println(current);
        //--sp读取数据
        SharedPreferences sf = getSharedPreferences("login_state", MODE_PRIVATE);
//        reality_hole
        String string_1 = sf.getString("string1", "");
        String reality_hole_1 = sf.getString("reality_hole", "");
        String reality_scale_1 = sf.getString("reality_scale", "");
//        SharedPreferences.Editor editor = sf.edit();
//        editor.putString("reality_scale","");
//        editor.putString("reality_hole","");
//        editor.commit();

//
//        System.out.println("********onSubH********"+reality_hole_1+"*****onSubH***********");
//        System.out.println("********onSubH********"+reality_scale_1+"*****onSubH***********");
        //sp读取数据--

        //--处理sp数据

        //处理sp数据--
        holes.add(new Hole_method(current,drill_name,s_et_s));
//        holes.removeAll(MainActivity.holedatas_1);
//        System.out.println("****************"+holes.size()+"****************");
//        Holedatas = new ArrayList<>();
        string1 ="";
//        System.out.println("*1111111111PersonAdapter.index_o);*:"+ PersonAdapter.index_o);
        if (PersonAdapter.index_o != null && PersonAdapter.index_o.size() != 0) {
//            System.out.println("*System.out.println(\"**\"PersonAdapter.index_o);*:"+PersonAdapter.index_o);
//            System.out.println("*System.out.println(\"**\"PersonAdapter.index_o,size());*:"+PersonAdapter.index_o.size());
//            System.out.println("1*:"+PersonAdapter.index_o.get(0));
//            System.out.println("2*:"+holes.get(PersonAdapter.index_o.get(0)).getvalue());
            for (int i = 0; i < PersonAdapter.index_o.size(); i++)
            {
//                System.out.println("11"+holes);
//                System.out.println("iiiiiiii*:"+PersonAdapter.index_o.get(i));
                int x = PersonAdapter.index_o.get(i);
                holes.remove(x);
//                System.out.println("22"+holes);
            }
        PersonAdapter.index_o.clear();//清空“删除”功能临时list
        }
//      孔数孔次
//
//        TextView tv_reality_scale = (TextView) findViewById(R.id.tv_reality_scale);
//        TextView tv_reality_hole = (TextView) findViewById(R.id.tv_reality_hole);
        String holes_size = ""+holes.size();
        if (reality_hole_1==""){
            double holes_1 = holes.size();
            tv_reality_hole.setText(""+holes_1);
        }else {
            double holes_1 = holes.size()+Double.parseDouble(reality_hole_1);
            reality_hole_1 = ""+Double.parseDouble(reality_hole_1)+holes.size();
            tv_reality_hole.setText(""+holes_1);
        }

//        reality_hole_d=0;
//        定义一个int 在下面每次循环累加
        int_hole =0;
//
        for (int i = 0; i < holes.size(); i++)
            {
//                holes.remove(1);
//            System.out.println("时间:"+holes.get(i).getrtime()+" "+"孔深:"+holes.get(i).getvalue());
//            holedatas.add(new Holedata_method("时间:"+holes.get(i).getrtime()+" "+"孔深:"+holes.get(i).getvalue()));
                string1 = string1+(holes.get(i).getrtime()+"___"+holes.get(i).getdrillid()+"___"+holes.get(i).getvalue())+"\n";
                double a = Double.parseDouble(holes.get(i).getvalue());
                int_hole=int_hole + a;
//                System.out.print("**************************");
//                System.out.print(int_hole);
//                System.out.print("**************************");
                //放数组
            }
        if(reality_scale_1==""){

            String fromDouble = "" + int_hole;
            tv_reality_scale.setText(fromDouble);

        }else {
            double scale_1 = int_hole+Double.parseDouble(reality_scale_1);
//            String fromDouble = "" + int_hole+reality_scale_1;
            tv_reality_scale.setText(""+scale_1);
//            scale_1=0;
        }
//        String fromDouble = "" + int_hole+reality_scale_1;
////        tv_reality_scale.setText(fromDouble);
        tv_hole.setText(string1+string_1);
        }
    public void onUpdateH(View v){
        //比较现在得两个list的size，如果首页的比“查看”里的list多，则就更新（有问题：更新完后，如果添加，在更新，会丢失，但是继续添加，会覆盖）
//        tv_hole = (TextView) findViewById(R.id.tv_hole);
        tv_hole.setText(" ");
        MainActivity.string1 ="";
//        holes.addAll(MainActivity.holedatas_1);
        String[] holes_strs={};
        List<String> holes= Arrays.asList(holes_strs);
        String[] holedatas_1_strs={};
        List<String> holedatas_1= Arrays.asList(holedatas_1_strs);
//        System.out.println("********************holes.addAll(holedatas_1)******************");
        holes.addAll(holedatas_1);
//        比较首页和这个界面的text date__drill__scale，（已经是string list了）已经没有具体value分类可供使用
//        System.out.println(holes);
        for (int i = 0; i <holes.size(); i++)
        {
            string1 = string1+(holes.get(i)+"\n");
//            tv_hole.setText(MainActivity.holedatas_1.get(i).getvalue()+"\n");
        }
//        System.out.println("********************holes.addAll(holedatas_1)******************");
//
//        System.out.println("********************onUpdate******************");
        double scale_1 = 0;
        for (int i = 0; i < MainActivity.holedatas_1.size(); i++)
        {
//            System.out.println("********************onUpdate******************"+string1);
            string1 = string1+(MainActivity.holedatas_1.get(i).getvalue()+"\n");
//            System.out.println("********************onUpdate******************"+string1);
//            String[] strArr = string1.split("___");
//            scale_1 = scale_1+Double.parseDouble(MainActivity.holedatas_1.get(i).getvalue());
//            MainActivity.holedatas_1.get(i).getvalue();
        }
//        System.out.println("********************onUpdate******************");
        tv_hole.setText(string1);
        //\n分组 将1组中以___分组提取最后一个 循环
        String[] strArr1 = string1.split("\n");
        for (int i = 0;i<strArr1.length;i++){
            String[] strArr1_1 = strArr1[i].split("___");
            scale_1 = scale_1+Double.parseDouble(strArr1_1[2]);
        }
        tv_reality_hole.setText(""+MainActivity.holedatas_1.size());
//        使用分割spile___
//        String[] strArr = string1.split("___");
//
        tv_reality_scale.setText(""+scale_1);

        //数据：全局删除添加按钮的里的(holes)，要不然添加会导致删除（复原）失效


    }
    public void onselectdrill(View v){
        new Thread()
        {
            public void run()
            {
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/zjd_drill_select?drill="+drill);
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置发送get请求
                    connection.setRequestMethod("GET"); // get要求大小，其实默认就是get请求
                    // 设置请求超时时间
                    connection.setReadTimeout(5000);
                    // 获取服务器返回的状态码
                    int code = connection.getResponseCode();
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        System.out.println("---content:"+content+"------------");
                        // 用我们创建的handler助手给系统发消息

                        drillselecthandler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;


                        drillselecthandler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
    public void ondeldrill(View v){
        new Thread()
        {
            public void run()
            {
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/zjd_drill_del?drill="+drill+
                            "&drill_name="+drill_name);
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置发送get请求
                    connection.setRequestMethod("GET"); // get要求大小，其实默认就是get请求
                    // 设置请求超时时间
                    connection.setReadTimeout(5000);
                    // 获取服务器返回的状态码
                    int code = connection.getResponseCode();
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        System.out.println("---content:"+content+"------------");
                        // 用我们创建的handler助手给系统发消息

                        drilldelhandler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;


                        drilldelhandler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
    public void onadddrill(View v){
        new Thread()
        {
            public void run()
            {
                try
                {
                    // 获取源码网址路径
//                    String path = et_path.getText().toString().trim();
                    // 创建URL对象，指定我们要访问的网址
//                    String path = 'http://192.168.1.5:8000/test1';
                    URL url = new URL("http://192.168.1.5:8000/zjd_drill_add?drill="+drill);
                    // 拿到HttpURLConnection对象，用于发送或者接收数据
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置发送get请求
                    connection.setRequestMethod("GET"); // get要求大小，其实默认就是get请求
                    // 设置请求超时时间
                    connection.setReadTimeout(5000);
                    // 获取服务器返回的状态码
                    int code = connection.getResponseCode();
                    // 如果code == 200说明请求成功
                    if (code == 200)
                    {
                        // 获取服务器返回的数据，是以流的形式返回的
                        InputStream is = connection.getInputStream();
                        // 使用我们定义的工具类，把is 转换成String
                        String content = StreamTools.readFromStream(is);
                        // 创建message对象
                        Message msg = new Message();
                        msg.what = REQUESTSUCESS;
                        msg.obj = content;
                        System.out.println("---content:"+content+"------------");
                        // 用我们创建的handler助手给系统发消息

                        drilladdhandler.sendMessage(msg);
                    }
                    else
                    {
                        // 请求资源不存在  Toast是一个view 也不能在在线程更新ui
                        Message msg = new Message();
                        msg.what = REQUESTNOTFOUND;


                        drilladdhandler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = REQUESTEXCEPTION;
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
    //save--
    public void onSave(View v){
        SharedPreferences sf = getSharedPreferences("login_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putInt("state", 1);
        editor.putString("workplace", et_workplace.getText().toString());
        editor.putString("m_people", m_people);
        editor.putString("s_people", s_people);
        editor.putString("drill", drill);
        editor.putString("classes", classes);
        editor.putString("many_p",many_people);
        editor.putString("string1",tv_hole.getText().toString());
        editor.putString("reality_scale",tv_reality_scale.getText().toString());
        editor.putString("reality_hole",tv_reality_hole.getText().toString());
        editor.putString("oil",et_oil.getText().toString());
        editor.putString("oil2",et_oil2.getText().toString());
        editor.putString("drill_id",et_drill.getText().toString());




//        System.out.println("***********onSave**********************:"+tv_hole.getText().toString());
        editor.commit();    //提交修改
        updateState();
    }
    public void onClear(View v){
        SharedPreferences sf = getSharedPreferences("login_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putInt("state", 2);
        editor.commit();
        updateState();
    }
    //根据当前登录状态，更新最上方TextView中的显示文字
    private void updateState() {
        SharedPreferences sf = getSharedPreferences("login_state", MODE_PRIVATE);
        int state = sf.getInt("state", 0);
        String workplace = sf.getString("workplace", "");
        String m_people = sf.getString("m_people", "");
        String s_people = sf.getString("s_people", "");
        String many_p = sf.getString("many_p", "");
        String drill = sf.getString("drill", "");
        String classes = sf.getString("classes", "");
        String string1 = sf.getString("string1","");
        String reality_scale = sf.getString("reality_scale","");
        String reality_hole = sf.getString("reality_hole","");
        String oil = sf.getString("oil",et_oil.getText().toString());
        String oil2 = sf.getString("oil2",et_oil2.getText().toString());
        String drill_id = sf.getString("drill_id",et_drill.getText().toString());


        if (state == 2) {
            SharedPreferences.Editor editor = sf.edit();
//            editor.putInt("state", 1);
            editor.putString("workplace", et_workplace.getText().toString());
            editor.putString("m_people", null);
            editor.putString("s_people", null);
            editor.putString("drill", null);
            editor.putString("classes", null);
            editor.putString("many_p",many_people);
            editor.putString("string1",null);
            editor.putString("oil","");
            editor.putString("oil2","");
            editor.putString("drill_id","");
            editor.putString("reality_scale","");
            editor.putString("reality_hole","");

            //

            tv_hole.setText(null);
            tv_reality_scale.setText(null);
            tv_reality_hole.setText(null);
            et_oil.setText(null);
            et_drill.setText(null);

            editor.putInt("state", 0);
            //
            editor.commit();    //提交修改
            System.exit(0);

        } else if (state == 1) {
            et_workplace.setText(workplace);
//            System.out.println("*********************************:"+string1);
            tv_hole.setText(string1);
            tv_reality_hole.setText(reality_hole);
            tv_reality_scale.setText(reality_scale);
            et_oil.setText(oil);
            et_oil2.setText(oil2);
            et_drill.setText(drill_id);
//            System.out.println("*********************************:"+string2);
//            tv_hole.setText(string1);
//            mTv.setText(m_people);
//            mTv_1.setText(s_people);
//            mTv_type.setText(drill);
//            mTv_classes.setText(classes);
            Mtext.setText(many_p);





        }
    }
    //--save
//
//public void restartApplication(Context context) {
//    final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    startActivity(intent);
//    android.os.Process.killProcess(android.os.Process.myPid());
//}
//
//private void restartApplication() {
//    final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    startActivity(intent);
//}

public void onStartActivityWithData(View v) {
    Intent intent = new Intent(MainActivity.this, HoleActivity.class);
    Bundle bundle = new Bundle();
    TextView tv_hole = (TextView) findViewById(R.id.tv_hole);
//    System.out.println("*******************");
//    System.out.println("*******************"+tv_hole.getText());
//    System.out.println("*******************");
    bundle.putString("holedata", tv_hole.getText().toString());
    intent.putExtra("HoleInfo", bundle);
    startActivity(intent);
}
}
