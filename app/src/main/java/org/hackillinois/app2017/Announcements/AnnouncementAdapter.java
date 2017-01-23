package org.hackillinois.app2017.Announcements;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Notification> notifications;


    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notificationType) TextView typeTextView;
        @BindView(R.id.notificationMessage) TextView messageTextView;
        @BindView(R.id.notificationTime) TextView timeTextView;

        public AnnouncementViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notificationType) TextView typeTextView;
        @BindView(R.id.notificationMessage) TextView messageTextView;
        @BindView(R.id.notificationTime) TextView timeTextView;
        @BindView(R.id.notificationLocation) TextView locationTextView;

        public ReminderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public AnnouncementAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public int getItemViewType(int position) {
        if (notifications.get(position) instanceof Reminder) {
            return 1;
        }

        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_layout, parent, false);
            return new AnnouncementViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_layout, parent, false);
        return new ReminderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 0 = Announcement
        // 1 = Reminder
        Notification notification = notifications.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                AnnouncementViewHolder announcementHolder = (AnnouncementViewHolder)holder;
                announcementHolder.typeTextView.setText("Announcement");
                announcementHolder.messageTextView.setText(notification.getMessage());
                announcementHolder.timeTextView.setText(notification.getTime());
                break;
            case 1:
                ReminderViewHolder reminderHolder = (ReminderViewHolder)holder;
                Reminder myReminder = (Reminder)notification;
                reminderHolder.typeTextView.setText("Reminder");
                reminderHolder.messageTextView.setText(myReminder.getMessage());
                reminderHolder.timeTextView.setText(myReminder.getTime());
                reminderHolder.locationTextView.setText(myReminder.getLocation());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
