package com.handheld.upsizeuhf.ui;

import android.annotation.SuppressLint;
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
import com.handheld.upsizeuhf.model.Actor;
import com.handheld.upsizeuhf.model.Costume;
import com.handheld.upsizeuhf.util.AnimationUtils;
import com.handheld.upsizeuhf.util.Constants;

import java.util.ArrayList;

public class ActSceneRVAdapter extends RecyclerView.Adapter<ActSceneRVAdapter.ActorViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private int mScreenType = Constants.SCREEN_TYPE_BY_ITEM_SET;

    private int lastPosition = -1;
    int row_index = -1;

    private ArrayList<Costume> mCostumeArrayList = new ArrayList<Costume>();


    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_listview, parent, false);
        return new ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActSceneRVAdapter.ActorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int pos = holder.getAdapterPosition();
        Costume costume = mCostumeArrayList.get(pos);
        holder.name.setText(costume.actScence);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "selected act scene name=" + costume.actScence + " position=" + pos);

                Util.play(1, 0);
                row_index=position;
                notifyDataSetChanged();

                UHFActivity uhfActivity = (UHFActivity)mActivity;
                uhfActivity.addSelectedActScene(costume.actScence);
            }
        });

        holder.list_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if (row_index==position) {
//            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrangeRed));
//            holder.list_item_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrangeRed));
            holder.list_item_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius_selected));

            Animation animate = AnimationUtils.Companion.getBounceAnimation(mContext);
            animate.setAnimationListener(new NameAnimationListener(costume));
            holder.name.startAnimation(animate);

        } else {
//            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlack));
//            holder.list_item_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlack));
            holder.list_item_layout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_border_radius));
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

    public ActSceneRVAdapter(Context context, Activity activity, ArrayList<Costume> costumes, int screenType) {
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

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "animation selected name=" + costume.actScence);
//            UHFActivity uhfActivity = (UHFActivity)mActivity;
//            uhfActivity.refreshActScene(costume.actor, mScreenType);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public class ActorViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        LinearLayout list_item_layout;

        public ActorViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.list_item);
                list_item_layout = (LinearLayout)itemView.findViewById(R.id.list_item_layout);
            }
    }
}