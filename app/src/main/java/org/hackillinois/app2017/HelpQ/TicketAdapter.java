package org.hackillinois.app2017.HelpQ;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{

    ArrayList<Ticket> tickets;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView issueTextView;
        public TextView userNameTextView;
        public ViewHolder(View v) {
            super(v);
            issueTextView = (TextView) v.findViewById(R.id.ticketIssue);
            userNameTextView = (TextView) v.findViewById(R.id.ticketName);
        }
    }

    public TicketAdapter(ArrayList<Ticket> tickets){
        this.tickets = tickets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ticket_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.issueTextView.setText(ticket.getIssue());
        holder.userNameTextView.setText(ticket.getUserName());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
