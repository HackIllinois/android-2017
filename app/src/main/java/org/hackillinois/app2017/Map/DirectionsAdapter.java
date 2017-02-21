package org.hackillinois.app2017.Map;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> directions;

    public static class DirectionViewHolder extends RecyclerView.ViewHolder {

        public DirectionViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.map_bottomsheet_distance) TextView distance;
        @BindView(R.id.map_bottomsheet_name) TextView name;
        @BindView(R.id.map_bottomsheet_time) TextView time;
        @BindView(R.id.map_bottomsheet_indoormap) TextView indoorMap;

        public HeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");

            name.setTypeface(brandon_reg);
            distance.setTypeface(brandon_reg);
            time.setTypeface(brandon_reg);
            indoorMap.setTypeface(brandon_med);
        }
    }

    public DirectionsAdapter(ArrayList<Object> directions) {
        this.directions = directions;
    }

    @Override
    public int getItemViewType(int position) {
        if (directions.get(position) instanceof DirectionObject) {
            return 1;
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_header_viewholder, parent, false);
            return new HeaderViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_layout, parent, false);
        return new DirectionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 0 = Header
        // 1 = Directions
        if (holder.getItemViewType() == 0) {
            HeaderViewHolder mHolder = (HeaderViewHolder)holder;
            Header mHeader = (Header)directions.get(position);

            mHolder.name.setText(mHeader.getName());
            mHolder.distance.setText(mHeader.getDistance());
            mHolder.time.setText(mHeader.getTime());
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }
}
