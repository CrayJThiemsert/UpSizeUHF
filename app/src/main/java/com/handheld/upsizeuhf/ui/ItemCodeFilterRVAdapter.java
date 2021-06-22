package com.handheld.upsizeuhf.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.handheld.upsizeuhf.R;
import com.handheld.upsizeuhf.UHFActivity;
import com.handheld.upsizeuhf.Util;
import com.handheld.upsizeuhf.model.Costume;
import com.handheld.upsizeuhf.util.UhfUtils;

import java.util.ArrayList;

public class ItemCodeFilterRVAdapter extends RecyclerView.Adapter<ItemCodeFilterRVAdapter.ItemCodeFilterViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;

    private int lastPosition = -1;
    int row_index = -1;

    private ArrayList<Costume> mCostumeArrayList = new ArrayList<Costume>();

    @Override
    public ItemCodeFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcode_filter_listview, parent, false);
        return new ItemCodeFilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCodeFilterViewHolder holder, int position) {
        Costume costume = mCostumeArrayList.get(position);
        holder.code_textview.setText(costume.code);
        holder.type_textview.setText(costume.type);
        holder.size_textview.setText(costume.size);
        holder.number_textview.setText(costume.codeNo);

        holder.code_textview.setOnClickListener(new ItemCodeFilterOnClickListener(costume, position));
        holder.type_textview.setOnClickListener(new ItemCodeFilterOnClickListener(costume, position));
        holder.size_textview.setOnClickListener(new ItemCodeFilterOnClickListener(costume, position));
        holder.number_textview.setOnClickListener(new ItemCodeFilterOnClickListener(costume, position));
        holder.item_code_layout.setOnClickListener(new ItemCodeFilterOnClickListener(costume, position));

        if(costume.isFound) {
            holder.item_code_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_found));
        } else {
            if (row_index == position) {
                holder.item_code_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_selected));
            } else {
                holder.item_code_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius));
            }
        }

//        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mCostumeArrayList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;

        }
    }

    public ItemCodeFilterRVAdapter(Context context, Activity activity, ArrayList<Costume> costumes) {
        mContext = context;
        mActivity = activity;
        mCostumeArrayList = costumes;
    }

    private class NameAnimationListener implements Animation.AnimationListener   {
        Costume costume = new Costume();
        public NameAnimationListener(Costume costume) {
            this.costume = costume;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "animation selected name=" + costume.actScence);
//            UHFActivity uhfActivity = (UHFActivity)mActivity;
//            uhfActivity.refreshActScene(costume.name);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ItemCodeFilterOnClickListener implements View.OnClickListener {
        int position = -1;
        Costume costume;
        public ItemCodeFilterOnClickListener(Costume costume, int position) {
            this.position = position;
            this.costume = costume;
        }

        @Override
        public void onClick(View view) {
            Util.play(1, 0);
            row_index=position;
            notifyDataSetChanged();

            UHFActivity uhfActivity = (UHFActivity)mActivity;
            uhfActivity.refreshItemCodeInfo(costume);
        }
    }

    public class ItemCodeFilterViewHolder extends RecyclerView.ViewHolder{

        TextView code_textview;
        TextView type_textview;
        TextView size_textview;
        TextView number_textview;

        LinearLayout item_code_layout;

        public ItemCodeFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            code_textview = (TextView)itemView.findViewById(R.id.code_textview);
            type_textview = (TextView)itemView.findViewById(R.id.type_textview);
            size_textview = (TextView)itemView.findViewById(R.id.size_textview);
            number_textview = (TextView)itemView.findViewById(R.id.number_textview);
            item_code_layout = (LinearLayout)itemView.findViewById(R.id.item_code_layout);
        }
    }
}