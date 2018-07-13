package com.hit.ddmonkey.app.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hit.ddmonkey.app.BillRecord;
import com.hit.ddmonkey.app.DDUser;
import com.hit.ddmonkey.app.Goal;
import com.hit.ddmonkey.app.MyRecycleViewAdapter;
import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.database.Bill_B;
import com.hit.ddmonkey.app.database.Goal_B;
import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
import com.hit.ddmonkey.app.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends BaseActivity {

    DrawerLayout drawerLayout;
    NavigationView view;
    RecyclerView recyclerview;

    List<BillRecord> items;

    TextView goalDay;
    TextView goalMon;

    Goal goalNow;
    List<Goal> goalList;

    MonkeyDatabaseHelper monkeyDatabaseHelper;

    String deleteFlag;
    Integer currentGoalId = -1;
    String currentuserId;
    DDUser currentUser;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUser = DDUser.getCurrentUser(MainActivity.this, DDUser.class);
        currentuserId = currentUser.getObjectId();

        Log.i("mageji","msg:"+currentuserId);
        monkeyDatabaseHelper = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类

        /*用ToolBar代替ActionBar*/
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("欢迎使用");
        setSupportActionBar(toolbar);

        goalDay = (TextView)findViewById(R.id.goalDayTV);
        goalMon = (TextView)findViewById(R.id.goalMonTV);
        /*初始化目标显示*/
        goalDay.setText("DDMonKey");
        goalMon.setText("当前没有计划");

        goalList = new ArrayList<>();
        initGoalShow(0);


        recyclerview = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        items = new ArrayList<>();
        initItems();
        final MyRecycleViewAdapter myadapter = new MyRecycleViewAdapter(items,R.layout.left_card);
        recyclerview.setAdapter(myadapter);

//        myadapter.setmOnItemClickListener(new MyRecycleViewAdapter.OnItemClickerListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "hao", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//                Toast.makeText(MainActivity.this, "123hao", Toast.LENGTH_SHORT).show();
//            }
//        });

        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                deleteItem(position);
                myadapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);


        FloatingActionButton fab_out = (FloatingActionButton) findViewById(R.id.bill_out_fab);
        fab_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,AddBillActivity.class);

                Bundle iobundle = new Bundle();
                iobundle.putString("ioflag","-");
                iobundle.putInt("currentGoalId",currentGoalId);
                intent.putExtras(iobundle);
                startActivity(intent);
            }
        });

        FloatingActionButton fab_in = (FloatingActionButton) findViewById(R.id.bill_in_fab);
        fab_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,AddBillActivity.class);
                Bundle iobundle = new Bundle();
                iobundle.putString("ioflag","+");
                iobundle.putInt("currentGoalId",currentGoalId);
                intent.putExtras(iobundle);
                startActivity(intent);
            }
        });

        /*侧滑菜单*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            MenuItem preMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //首先将选中条目变为选中状态 即checked为true,后关闭Drawer，以前选中的Item需要变为未选中状态
                if (preMenuItem != null)
                    preMenuItem.setChecked(false);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    preMenuItem = menuItem;

                //不同item
                switch (menuItem.getItemId()) {
                    case R.id.nav_by_date_search:

                        Intent intentdate = new Intent(MainActivity.this,ByDateActivity.class);
                        intentdate.putExtra("currentGoalId",currentGoalId);
                        intentdate.putExtra("currentuserId",currentuserId);
                        startActivity(intentdate);

                        break;
                    case R.id.nav_by_category_search:
                        Intent intentcategory = new Intent(MainActivity.this,ByCategoryActivity.class);
                        intentcategory.putExtra("currentGoalId",currentGoalId);
                        intentcategory.putExtra("currentuserId",currentuserId);
                        startActivity(intentcategory);
                        break;
                    case R.id.nav_analyst:
                        Intent intentAn = new Intent(MainActivity.this,AnalyzeActivity.class);
                        intentAn.putExtra("currentGoalId",currentGoalId);
                        intentAn.putExtra("currentuserId",currentuserId);
                        startActivity(intentAn);
                        break;
                    case R.id.add_goal:
                        Intent intent = new Intent(MainActivity.this,GaolSetActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.switch_goal:
                        Intent switch_goal_intent = new Intent(MainActivity.this,SwitchGoalActivity.class);
                        switch_goal_intent.putExtra("currentuserId",currentuserId);
                        startActivity(switch_goal_intent);
                        break;
                    case R.id.user_logout:
                        logout();
                        break;
                    case R.id.data_upload:
                        upload_data();
                        break;
//                    case R.id.data_download:
//                        download_data();
//                        break;
                }
                return true;
            }

        });


        /*侧滑菜单“三”的标识*/
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.openDrawer , R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super .onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super .onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }



    @Override
    public void onResume() {
        super.onResume();
        int goalposition = getIntent().getIntExtra("goal",0);
        initGoalShow(goalposition);
        items = new ArrayList<>();
        initItems();
        MyRecycleViewAdapter myadapter = new MyRecycleViewAdapter(items,R.layout.left_card);
        recyclerview.setAdapter(myadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (monkeyDatabaseHelper != null)
            monkeyDatabaseHelper.close();
    }


    public void initItems(){
        if (goalNow!=null){
            currentGoalId = goalNow.getId();
        }
        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
        Cursor cursor = monkeyDatabase.query("Bill",null,"goalid=? and userid=?",new String[]{currentGoalId.toString(),currentuserId.toString()},null,null,null);
        items.clear();
        if(cursor.moveToFirst()){
            do{
                BillRecord BillRecordItem = new BillRecord();
                BillRecordItem.setIO(cursor.getString(cursor.getColumnIndex("io")));
                BillRecordItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                BillRecordItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
                BillRecordItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                BillRecordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                BillRecordItem.setDateId(cursor.getString(cursor.getColumnIndex("dateid")));
                items.add(BillRecordItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }

    public void initGoal(){
        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库

        Cursor cursor = monkeyDatabase.query("Goal",null,"userid=?",new String[]{currentuserId.toString()},null,null,null);
        goalList.clear();
        if(cursor.moveToFirst()){
            do{
                Goal goalItem = new Goal();
                goalItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
                goalItem.setName(cursor.getString(cursor.getColumnIndex("goalname")));
                goalItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                goalItem.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time"))));
                goalList.add(goalItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }

    public void initGoalShow(int goalposition){
        initGoal();
        if(goalList.size() != 0)
        {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            goalNow = goalList.get(goalposition);
            currentGoalId = goalNow.getId();
            toolbar.setTitle(goalNow.getName());
            goalDay.setText("天数："+goalNow.getTime());
            goalMon.setText("金额："+goalNow.getMoney());

        }
    }

    public void deleteItem(int p){
        deleteFlag = items.get(p).getDateId();
        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();
        monkeyDatabase.delete("Bill","dateid = ?",new String[]{deleteFlag});

        items.remove(p);
        MyRecycleViewAdapter myadapter = new MyRecycleViewAdapter(items,R.layout.left_card);
        recyclerview.setAdapter(myadapter);

    }

    public void upload_data(){
        List<BmobObject> goals = new ArrayList<BmobObject>();
        List<BmobObject> bills = new ArrayList<BmobObject>();

        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库

        Cursor cursor = monkeyDatabase.query("Goal",null,null,null,null,null,null);
        goals.clear();
        if(cursor.moveToFirst()){
            do{
                Goal_B goalItem = new Goal_B();
                goalItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
                goalItem.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
                goalItem.setGoalname(cursor.getString(cursor.getColumnIndex("goalname")));
                goalItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                goalItem.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time"))));
                goalItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
                goals.add(goalItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

        Cursor cursor2 = monkeyDatabase.query("Bill",null,null,null,null,null,null);
        bills.clear();
        if(cursor2.moveToFirst()){
            do{
                Bill_B billItem = new Bill_B();
                billItem.setId(cursor2.getInt(cursor2.getColumnIndex("id")));
                billItem.setUserId(cursor2.getString(cursor2.getColumnIndex("userid")));
                billItem.setGoalid(cursor2.getInt(cursor2.getColumnIndex("goalid")));
                billItem.setCategaryid(cursor2.getInt(cursor2.getColumnIndex("categaryid")));
                billItem.setIo(cursor2.getString(cursor2.getColumnIndex("io")));
                billItem.setMoney(cursor2.getFloat(cursor2.getColumnIndex("money")));
                billItem.setCategory(cursor2.getString(cursor2.getColumnIndex("category")));
                billItem.setNote(cursor2.getString(cursor2.getColumnIndex("note")));
                billItem.setDate(cursor2.getString(cursor2.getColumnIndex("date")));
                billItem.setDateid(cursor2.getString(cursor2.getColumnIndex("dateid")));
                bills.add(billItem);
            }while (cursor2.moveToNext());
        }
        cursor2.close();

        new BmobObject().insertBatch(this, goals, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("批量添加成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                toast("批量添加失败:"+msg);
            }
        });

        new BmobObject().insertBatch(this, bills, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("批量添加成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                toast("批量添加失败:"+msg);
            }
        });
    }
    Integer goalnum = 0;
//    public void download_data(){
//        //上传本地数据
//        upload_data();
//        //清空本地数据
//        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
//        monkeyDatabase.execSQL("delete from Bill");
//        monkeyDatabase.execSQL("delete from Goal");
//
//        //1. 查询该用户目标的数量；
//
//        BmobQuery<Goal_B> query = new BmobQuery<Goal_B>();
//        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("userid", currentuserId);
//        query.count(this, Goal_B.class, new CountListener() {
//            @Override
//            public void onSuccess(int i) {
//                goalnum = i;
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
//
//        //查询所有用户的目标
//        final List<Goal_B> allgoal = new ArrayList<>();
//        query.setLimit(goalnum);
//        query.findObjects(this, new FindListener<Goal_B>() {
//            @Override
//            public void onSuccess(List<Goal_B> list) {
//                allgoal.addAll(list);
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                toast("查询失败");
//            }
//        });
//
//        //查询用户账单记录的数量
//        BmobQuery<Bill_B> query_bill = new BmobQuery<Bill_B>();
//        query.addWhereEqualTo("userid", currentuserId);
// 
//    }
    /*退出登录方法*/
    public void logout(){
        final Dialog dialog;
        final CharSequence[] items={"退出当前账号","关闭应用"};
        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        DDUser.logOut(MainActivity.this);
                        currentUser =  DDUser.getCurrentUser(MainActivity.this, DDUser.class);
                        if (currentUser != null) {
                            //退出登录失败
                            return;
                        }
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        View v=new View(MainActivity.this);

        AlertDialog alertDialog=builder.create();

        alertDialog.show();

    }

}