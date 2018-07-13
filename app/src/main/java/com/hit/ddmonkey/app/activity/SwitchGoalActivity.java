package com.hit.ddmonkey.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hit.ddmonkey.app.Goal;
import com.hit.ddmonkey.app.GoalRecycleViewAdapter;
import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SwitchGoalActivity extends Activity{
    RecyclerView recyclerView;
    GoalRecycleViewAdapter adapter;
    List<Goal> goalList;
    MonkeyDatabaseHelper monkeyDatabaseHelper = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);
    String currentuserId = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goallist);
        currentuserId = getIntent().getStringExtra("currentuserId");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fa);
        goalList = new ArrayList<>();
        initGoal();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoalRecycleViewAdapter(this,goalList);
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new GoalRecycleViewAdapter.OnItemClickerListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("goal",position);
                intent.setClass(SwitchGoalActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    public void initGoal(){
        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库

        Cursor cursor = monkeyDatabase.query("Goal",null,"userid=?",new String[]{currentuserId.toString()},null,null,null);
        goalList.clear();
        if(cursor.moveToFirst()){
            do{
                Goal goalItem = new Goal();
                goalItem.setName(cursor.getString(cursor.getColumnIndex("goalname")));
                goalItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                goalItem.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time"))));
                goalList.add(goalItem);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}
