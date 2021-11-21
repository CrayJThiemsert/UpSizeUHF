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
import com.handheld.upsizeuhf.util.Constants;
import com.handheld.upsizeuhf.util.UhfUtils;

import java.util.ArrayList;

public class ViewItemCodeRVAdapter extends RecyclerView.Adapter<ViewItemCodeRVAdapter.ItemCodeViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;

    private int lastPosition = -1;
    int row_index = -1;

    private ArrayList<Costume> mCostumeArrayList = new ArrayList<Costume>();
    private int mScreenType = Constants.SCREEN_TYPE_BY_ITEM_SET;

    @Override
    public ItemCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_itemcode_listview, parent, false);
        return new ItemCodeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCodeViewHolder holder, int position) {
        Costume costume = mCostumeArrayList.get(position);
        holder.runningno_textview.setText(costume.runningNo);
        holder.code_textview.setText(costume.code);
        holder.type_textview.setText(costume.type);
        holder.size_textview.setText(costume.size);
        holder.number_textview.setText(costume.codeNo);
        String epc = costume.epcHeader + costume.epcRun;
//        epc = UhfUtils.Companion.separateEPCString(epc, " ", 4, 16);
        String epcTop = UhfUtils.Companion.separateEPCTopString(epc, " ", 4, 16);
        String epcBottom = UhfUtils.Companion.separateEPCBottomString(epc, " ", 4, 16);

        holder.epc_header_textview.setText(epcTop);
        holder.epc_run_textview.setText(epcBottom);

        String currentBox = getCurrentBoxString(costume);
        holder.current_box_textview.setText(currentBox);

        holder.epc_header_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String epc = costume.epcHeader + costume.epcRun;
                epc = UhfUtils.Companion.separateEPCString(epc, " ", 4, 16);
                Log.d(TAG, "selected epc=" + epc + " position=" + position);

                Util.play(1, 0);
                row_index=position;
                notifyDataSetChanged();

                UHFActivity uhfActivity = (UHFActivity)mActivity;
                uhfActivity.addSelectedCostumeToWriteTag(costume, mScreenType);
            }
        });

        holder.epc_run_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String epc = costume.epcHeader + costume.epcRun;
                epc = UhfUtils.Companion.separateEPCString(epc, " ", 4, 16);
                Log.d(TAG, "selected epc=" + epc + " position=" + position);

                Util.play(1, 0);
                row_index=position;
                notifyDataSetChanged();

                UHFActivity uhfActivity = (UHFActivity)mActivity;
                uhfActivity.addSelectedCostumeToWriteTag(costume, mScreenType);
            }
        });

        holder.current_box_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        holder.item_code_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

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

    private String getCurrentBoxString(Costume costume) {
        String result = "";
        if(!costume.shipBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.ship_box) + ": " + costume.shipBox;
        } else if(!costume.storageBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.storage_box) + ": " + costume.storageBox;
        } else if(!costume.playBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.play_box) + ": " + costume.playBox;
        }
        return result;
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

    public ViewItemCodeRVAdapter(Context context, Activity activity, ArrayList<Costume> costumes, int screenType) {
        mContext = context;
        mActivity = activity;
        mCostumeArrayList = costumes;
        mScreenType = screenType;
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

    public class ItemCodeViewHolder extends RecyclerView.ViewHolder{

        TextView runningno_textview;
        TextView code_textview;
        TextView type_textview;
        TextView size_textview;
        TextView number_textview;
        TextView epc_header_textview;
        TextView epc_run_textview;
        TextView current_box_textview;
        LinearLayout item_code_layout;

        public ItemCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            runningno_textview = (TextView)itemView.findViewById(R.id.runningno_textview);
            code_textview = (TextView)itemView.findViewById(R.id.code_textview);
            type_textview = (TextView)itemView.findViewById(R.id.type_textview);
            size_textview = (TextView)itemView.findViewById(R.id.size_textview);
            number_textview = (TextView)itemView.findViewById(R.id.number_textview);
            epc_header_textview = (TextView)itemView.findViewById(R.id.epc_header_textview);
            epc_run_textview = (TextView)itemView.findViewById(R.id.epc_run_textview);
            current_box_textview = (TextView)itemView.findViewById(R.id.current_box_textview);
            item_code_layout = (LinearLayout)itemView.findViewById(R.id.item_code_layout);

            current_box_textview.setText("");
        }
    }
}