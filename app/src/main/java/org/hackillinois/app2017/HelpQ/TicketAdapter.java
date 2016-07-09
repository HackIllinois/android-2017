package org.hackillinois.app2017.HelpQ;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{

    private ArrayList<Ticket> tickets;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ticketIssue) TextView issueTextView;
        @BindView(R.id.ticketName) TextView userNameTextView;
        private clickListener listener;

        public ViewHolder(View v, clickListener listener) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            listener.onItemClick(position);
        }

        public interface clickListener{
            void onItemClick(int position);
        }
    }

    public TicketAdapter(ArrayList<Ticket> tickets){
        this.tickets = tickets;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ticket_layout, parent, false);
        ViewHolder vh = new ViewHolder(view, new ViewHolder.clickListener() {
            @Override
            public void onItemClick(int position) {
                Ticket ticket = tickets.get(position);
                Toast.makeText(parent.getContext(), ticket.getIssue(), Toast.LENGTH_SHORT).show();
            }
        });

        return vh;
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
