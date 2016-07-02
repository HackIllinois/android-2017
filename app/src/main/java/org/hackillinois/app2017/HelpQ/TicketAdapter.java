package org.hackillinois.app2017.HelpQ;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{

    ArrayList<Ticket> tickets;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ticketIssue) TextView issueTextView;
        @BindView(R.id.ticketName) TextView userNameTextView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
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
