package com.handheld.upsizeuhf.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.handheld.upsizeuhf.R;
import com.handheld.upsizeuhf.UHFActivity;
import com.handheld.upsizeuhf.Util;
import com.handheld.upsizeuhf.model.Actor;
import com.handheld.upsizeuhf.util.AnimationUtils;

import java.util.ArrayList;

public class ActorAdapter extends ArrayAdapter<Actor> {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Activity mActivity;
    // View lookup cache
    private static class ViewHolder {
        TextView name;
    }

    public ActorAdapter(Context context, Activity activity, ArrayList<Actor> actors) {
        super(context, R.layout.name_listview, actors);
        mContext = context;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Actor actor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.name_listview, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.list_item);

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String name = ((TextView) view).getText().toString();
                    Actor actorSelected = (Actor)((TextView) view).getTag();

                    Log.d(TAG, "selected actor name=" + actorSelected.name);

                    Util.play(1, 0);
                    Animation animate = AnimationUtils.Companion.getBounceAnimation(mContext);
                    animate.setAnimationListener(new NameAnimationListener(actorSelected));
                    ((TextView) view).startAnimation(animate);
                }
            });

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(actor.name);
        viewHolder.name.setTag(actor);

        // Return the completed view to render on screen
        return convertView;
    }

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
}