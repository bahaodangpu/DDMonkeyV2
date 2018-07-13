package com.hit.ddmonkey.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GoalRecycleViewAdapter extends RecyclerView.Adapter<GoalRecycleViewAdapter.ViewHolder> {

    private List<Goal> goalList;
    Context context;

    public interface OnItemClickerListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickerListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickerListener listener){
        this.mOnItemClickListener = listener;
    }

    public GoalRecycleViewAdapter(Context context,List<Goal> goalList)
    {
        this.context = context;
        this.goalList = goalList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_card,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Goal item = goalList.get(position);
        Log.e("ddddd","dyl");
        holder.MoneyText.setText(String.valueOf(item.getMoney()));
        holder.NameText.setText(item.getName());
        holder.DayText.setText(item.getTime()+"");

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView MoneyText;
        public TextView NameText;
        public TextView DayText;
        public ViewHolder(View itemView){
            super(itemView);
            MoneyText = (TextView)itemView.findViewById(R.id.moneyNum);
            NameText = (TextView)itemView.findViewById(R.id.goalName);
            DayText = (TextView)itemView.findViewById(R.id.period);
        }
    }

    public void refresh(List<Goal> goalList){
        this.goalList = goalList;
        notifyDataSetChanged();
    }
}
