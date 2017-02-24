package org.hackillinois.app2017.Map;


import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> directions;

    public static class DirectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.map_bottomsheet_direction_image)
        ImageView image;
        @BindView(R.id.map_bottomsheet_direction_summary)
        TextView summary;
        @BindView(R.id.map_bottomsheet_direction_distance)
        TextView distance;

        public DirectionViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");

            summary.setTypeface(brandon_reg);
            distance.setTypeface(brandon_reg);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.map_bottomsheet_header_summary) TextView summary;
        @BindView(R.id.map_bottomsheet_header_image) ImageView image;

        public HeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");

            summary.setTypeface(brandon_reg);
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

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_direction_viewholder, parent, false);
        return new DirectionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (directions.get(position) instanceof DirectionObject) {
            DirectionViewHolder h = (DirectionViewHolder) holder;
            DirectionObject direction = (DirectionObject)directions.get(position);
            h.distance.setText(direction.getDistance());
            h.summary.setText(direction.getDescription());

            if (direction.getDescription().contains("Head")) {
                h.image.setImageDrawable(ContextCompat.getDrawable(h.image.getContext(), R.drawable.head_towards));
            } else if (direction.getDescription().contains("left")) {
                h.image.setImageDrawable(ContextCompat.getDrawable(h.image.getContext(), R.drawable.turn_left));
            } else if (direction.getDescription().contains("right")) {
                h.image.setImageDrawable(ContextCompat.getDrawable(h.image.getContext(), R.drawable.turn_right));
            }
        }

        if (position == directions.size() - 1) {
            HeaderViewHolder h = (HeaderViewHolder) holder;
            h.image.setImageDrawable(ContextCompat.getDrawable(h.image.getContext(), R.drawable.ic_pin_drop));
        }
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }
}
