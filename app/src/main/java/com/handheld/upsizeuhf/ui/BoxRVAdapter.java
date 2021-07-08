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
import com.handheld.upsizeuhf.model.Box;
import com.handheld.upsizeuhf.ui.dialog.CheckTypeDialogFragment;

import java.util.ArrayList;

public class BoxRVAdapter extends RecyclerView.Adapter<BoxRVAdapter.BoxViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private CheckTypeDialogFragment mFragment;

    private int lastPosition = -1;
    int row_index = -1;

    private ArrayList<Box> mBoxArrayList = new ArrayList<Box>();


    @Override
    public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_listview, parent, false);
        return new BoxViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
        Box box = mBoxArrayList.get(position);
        holder.name_textview.setText(box.name);
        holder.epc_header_textview.setText(box.epcHeader);
        holder.epc_run_textview.setText(box.epcRun);

        holder.name_textview.setOnClickListener(new BoxRVAdapter.ItemInfoOnClickListener(box, position));
        holder.epc_header_textview.setOnClickListener(new BoxRVAdapter.ItemInfoOnClickListener(box, position));
        holder.epc_run_textview.setOnClickListener(new BoxRVAdapter.ItemInfoOnClickListener(box, position));

        if(box.selected) {
            holder.box_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_found));
        } else if (row_index == position) {
            holder.box_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_selected));
        } else {
            holder.box_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius));
        }
    }

    @Override
    public int getItemCount() {
        return mBoxArrayList.size();
    }

    private class ItemInfoOnClickListener implements View.OnClickListener {
        int position = -1;
        Box box;
        public ItemInfoOnClickListener(Box box, int position) {
            this.position = position;
            this.box = box;
        }

        @Override
        public void onClick(View view) {
            Util.play(1, 0);
//            mFragment.clearBoxList();

            row_index=position;
            notifyDataSetChanged();

            UHFActivity uhfActivity = (UHFActivity)mActivity;
            uhfActivity.setSelectedCheckedBox(box);
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

    public BoxRVAdapter(Context context, Activity activity, ArrayList<Box> boxs, CheckTypeDialogFragment fragment) {
        mContext = context;
        mActivity = activity;
        mBoxArrayList = boxs;
        mFragment = fragment;
    }

    private class NameAnimationListener implements Animation.AnimationListener   {
        Box box = new Box();
        public NameAnimationListener(Box box) {
            this.box = box;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "animation selected name=" + box.name);
//            UHFActivity uhfActivity = (UHFActivity)mActivity;
//            uhfActivity.refreshActScene(box.name);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public class BoxViewHolder extends RecyclerView.ViewHolder{

        TextView name_textview;
        TextView epc_header_textview;
        TextView epc_run_textview;

        LinearLayout box_layout;

        public BoxViewHolder(@NonNull View itemView) {
            super(itemView);
            name_textview = (TextView)itemView.findViewById(R.id.name_textview);
            epc_header_textview = (TextView)itemView.findViewById(R.id.epc_header_textview);
            epc_run_textview = (TextView)itemView.findViewById(R.id.epc_run_textview);
            box_layout = (LinearLayout)itemView.findViewById(R.id.box_layout);
        }
    }
}