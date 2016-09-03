package org.hackillinois.app2017.Announcement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> implements Filterable {

    private ArrayList<Announcement> announcements;
    private ArrayList<Announcement> filteredAnnouncements;
    private HashMap<String, Integer> filterMap;
    private ItemFilter mFilter = new ItemFilter();

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.announcementTitle) TextView titleTextView;
        @BindView(R.id.announcementMessage) TextView messageTextView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public AnnouncementAdapter(ArrayList<Announcement> announcements){
        this.announcements = announcements;
        this.filteredAnnouncements = announcements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_announcement_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement curAnnouncement = filteredAnnouncements.get(position);
        holder.titleTextView.setText(curAnnouncement.getTitle());
        holder.messageTextView.setText(curAnnouncement.getMessage());
    }

    @Override
    public int getItemCount() {
        return filteredAnnouncements.size();
    }


    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toUpperCase();
            int filterCategory = Constants.filterMap.get(filterString);
            FilterResults results =  new FilterResults();
            final ArrayList<Announcement> list = announcements;
            int count = list.size();
            final ArrayList<Announcement> newList =  new ArrayList<>(count);

            for(int i = 0; i<count; i++){
                Announcement cur = list.get(i);
                if(cur.getCategory() == filterCategory || filterCategory == Constants.ALL_CATEGORY){
                    newList.add(cur);
                }
            }
            results.values = newList;
            results.count = newList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredAnnouncements = (ArrayList<Announcement>) results.values;
            notifyDataSetChanged();
        }
    }

}
