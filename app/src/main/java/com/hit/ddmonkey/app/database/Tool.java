package com.hit.ddmonkey.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hit.ddmonkey.app.BillRecord;
import com.hit.ddmonkey.app.Goal;

import java.util.ArrayList;
import java.util.List;

//修改Goal和Bill的数据库结构，增加UserId字段，并同步在Bmob上。
public class Tool {
    MonkeyDatabaseHelper monkeyDatabaseHelper;
    List<Goal_B> goals = new ArrayList<Goal_B>();

    public void initGoal(){
        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库

        Cursor cursor = monkeyDatabase.query("Goal",null,null,null,null,null,null);
        goals.clear();
        if(cursor.moveToFirst()){
            do{
                Goal_B goalItem = new Goal_B();
                goalItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
                goalItem.setGoalname(cursor.getString(cursor.getColumnIndex("goalname")));
                goalItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                goalItem.setTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time"))));
                goals.add(goalItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }

//    public void initItems(){
//
//        SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
//
//        Cursor cursor = monkeyDatabase.query("Bill",null,null,null,null,null,null);
//        items.clear();
//        if(cursor.moveToFirst()){
//            do{
//                BillRecord BillRecordItem = new BillRecord();
//                BillRecordItem.setIO(cursor.getString(cursor.getColumnIndex("io")));
//                BillRecordItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
//                BillRecordItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
//                BillRecordItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
//                BillRecordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
//                BillRecordItem.setDateId(cursor.getString(cursor.getColumnIndex("dateid")));
//                items.add(BillRecordItem);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//
//    }
}