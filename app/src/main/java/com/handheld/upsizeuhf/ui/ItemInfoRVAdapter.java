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

import java.util.ArrayList;

public class ItemInfoRVAdapter extends RecyclerView.Adapter<ItemInfoRVAdapter.ItemInfoViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;

    private int lastPosition = -1;
    int row_index = -1;

    private ArrayList<Costume> mCostumeArrayList = new ArrayList<Costume>();


    @Override
    public ItemInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iteminfo_listview, parent, false);
        return new ItemInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemInfoViewHolder holder, int position) {
        Costume costume = mCostumeArrayList.get(position);
        holder.actor_textview.setText(costume.actor);
        holder.actscene_textview.setText(costume.actScence);
        holder.epc_header_textview.setText(costume.epcHeader);
        holder.epc_run_textview.setText(costume.epcRun);

        holder.actor_textview.setOnClickListener(new ItemInfoRVAdapter.ItemInfoOnClickListener(costume, position));
        holder.actscene_textview.setOnClickListener(new ItemInfoRVAdapter.ItemInfoOnClickListener(costume, position));
        holder.epc_header_textview.setOnClickListener(new ItemInfoRVAdapter.ItemInfoOnClickListener(costume, position));
        holder.epc_run_textview.setOnClickListener(new ItemInfoRVAdapter.ItemInfoOnClickListener(costume, position));

        if(costume.isFound) {
            holder.item_info_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_found));
        } else {
            if (row_index == position) {
                holder.item_info_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_selected));
            } else {
                holder.item_info_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCostumeArrayList.size();
    }

    private class ItemInfoOnClickListener implements View.OnClickListener {
        int position = -1;
        Costume costume;
        public ItemInfoOnClickListener(Costume costume, int position) {
            this.position = position;
            this.costume = costume;
        }

        @Override
        public void onClick(View view) {
            Util.play(1, 0);
            row_index=position;
            notifyDataSetChanged();

            UHFActivity uhfActivity = (UHFActivity)mActivity;
            uhfActivity.addSelectedItemInfo(costume);
        }
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

    public ItemInfoRVAdapter(Context context, Activity activity, ArrayList<Costume> costumes) {
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

    public class ItemInfoViewHolder extends RecyclerView.ViewHolder{

        TextView actor_textview;
        TextView actscene_textview;
        TextView epc_header_textview;
        TextView epc_run_textview;

        LinearLayout item_info_layout;

        public ItemInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            actor_textview = (TextView)itemView.findViewById(R.id.actor_textview);
            actscene_textview = (TextView)itemView.findViewById(R.id.actscene_textview);
            epc_header_textview = (TextView)itemView.findViewById(R.id.epc_header_textview);
            epc_run_textview = (TextView)itemView.findViewById(R.id.epc_run_textview);
            item_info_layout = (LinearLayout)itemView.findViewById(R.id.box_layout);
        }
    }
}