package com.handheld.upsizeuhf.ui;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.handheld.upsizeuhf.R;
import com.handheld.upsizeuhf.UHFActivity;
import com.handheld.upsizeuhf.Util;
import com.handheld.upsizeuhf.model.Actor;
import com.handheld.upsizeuhf.util.AnimationUtils;

import java.util.ArrayList;

public class ActorRVAdapter extends RecyclerView.Adapter<ActorRVAdapter.ActorViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;

    private int lastPosition = -1;
    int row_index = -1;



    private int mCurrentItem=0;
    private boolean isClick = false;
    private ArrayList<Actor> mActorArrayList = new ArrayList<Actor>();


    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_listview, parent, false);
        return new ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorRVAdapter.ActorViewHolder holder, int position) {
        Actor actor = mActorArrayList.get(position);
        holder.name.setText(actor.name);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Actor actorSelected = (Actor)((TextView) view).getTag();

                Log.d(TAG, "selected actor name=" + actor.name + " position=" + position);



                Util.play(1, 0);
                row_index=position;
                notifyDataSetChanged();





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
            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrangeRed));
            holder.list_item_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrangeRed));

            Animation animate = AnimationUtils.Companion.getBounceAnimation(mContext);
            animate.setAnimationListener(new NameAnimationListener(actor));
            holder.name.startAnimation(animate);

        } else {
            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlack));
            holder.list_item_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlack));
        }


//        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mActorArrayList.size();
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


//    // View lookup cache
//    private static class ViewHolder {
//        TextView name;
//        LinearLayout list_item_layout;
//    }

    public ActorRVAdapter(Context context, Activity activity, ArrayList<Actor> actors) {
//        super(context, R.layout.name_listview, actors);
        mContext = context;
        mActivity = activity;
        mActorArrayList = actors;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Actor actor = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//        if (convertView == null) {
//            // If there's no view to re-use, inflate a brand new view for row
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.name_listview, parent, false);
//            viewHolder.name = (TextView) convertView.findViewById(R.id.list_item);
//            viewHolder.list_item_layout = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
//
//            viewHolder.name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Actor actorSelected = (Actor)((TextView) view).getTag();
//
//                    Log.d(TAG, "selected actor name=" + actorSelected.name + " id=" + getPosition(actor) + " position=" + position);
//
//                    Util.play(1, 0);
//                    Animation animate = AnimationUtils.Companion.getBounceAnimation(mContext);
//                    animate.setAnimationListener(new NameAnimationListener(actorSelected));
//                    ((TextView) view).startAnimation(animate);
//
//                }
//            });
//
//            // Cache the viewHolder object inside the fresh view
//            convertView.setTag(viewHolder);
//        } else {
//            // View is being recycled, retrieve the viewHolder object from tag
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        // Populate the data from the data object via the viewHolder object
//        // into the template view.
//        viewHolder.name.setText(actor.name);
//        viewHolder.name.setTag(actor);
//
//        Log.d(TAG, "currentItem position=" + position + " : mCurrentItem=" + mCurrentItem);
//
//        // Return the completed view to render on screen
//        return convertView;
//    }
//
//    public void setCurrentItem(int currentItem) {
//        Log.d(TAG, "currentItem position=" + currentItem);
//        this.mCurrentItem = currentItem;
//    }
//
//    public void setClick(boolean click) {
//        this.isClick = click;
//    }


    private class NameAnimationListener implements Animation.AnimationListener   {
        Actor actor = new Actor();
        public NameAnimationListener(Actor actor) {
            this.actor = actor;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "animation selected name=" + actor.name);
            UHFActivity uhfActivity = (UHFActivity)mActivity;
            uhfActivity.refreshActScene(actor.name);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public class ActorViewHolder extends RecyclerView.ViewHolder{

//            TextView movieTitle, movieType, movieYear;
//            LinearLayout linearLayout;
        TextView name;
        LinearLayout list_item_layout;

        public ActorViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.list_item);


                list_item_layout = (LinearLayout)itemView.findViewById(R.id.list_item_layout);
            }
    }
}