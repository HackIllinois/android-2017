package org.hackillinois.android.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends DrawerActivity {
	@BindView(R.id.active_events) RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);

		//create the ItemAdapter holding your Items
		ItemAdapter itemAdapter = new ItemAdapter();
		//create the managing FastAdapter, by passing in the itemAdapter
		FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

		//set our adapters to the RecyclerView
		recyclerView.setAdapter(fastAdapter);

		//set the items to your ItemAdapter
		//itemAdapter.add(ITEMS);
	}
}
