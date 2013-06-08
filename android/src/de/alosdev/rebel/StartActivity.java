package de.alosdev.rebel;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.alosdev.rebel.domain.DemoDetails;
import de.alosdev.rebel.view.DemoView;

public class StartActivity extends FragmentActivity implements OnItemClickListener {

	private static final DemoDetails LOADING = new DemoDetails(-1, "Loading", "");
	private CustomArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		ListView list = (ListView) findViewById(R.id.list);
		adapter = new CustomArrayAdapter(getApplicationContext(), new ArrayList<DemoDetails>());
		list.setAdapter(adapter);
		adapter.add(LOADING);
		list.setOnItemClickListener(this);
	}

	private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {

		/**
		 * @param demos
		 *          An array containing the details of the demos to be displayed.
		 */
		public CustomArrayAdapter(Context context, ArrayList<DemoDetails> demos) {
			super(context, R.layout.feature, R.id.title, demos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DemoView demoView;
			if (convertView instanceof DemoView) {
				demoView = (DemoView) convertView;
			} else {
				demoView = new DemoView(getContext());
			}

			DemoDetails demo = getItem(position);

			demoView.setTitleId(demo.title);
			demoView.setDescriptionId(demo.description);
			demoView.setDemoDetails(demo);

			return demoView;
		}
	}

	@Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DemoView demoView = (DemoView) view;
		DemoDetails details = demoView.getDemoDetails();
		if (null != details)
			if (details.demoId == -1) {
				//TODO 
				adapter.remove(details);
				adapter.addAll(new DemoDetails(1, "Demo 1", "description 1"), new DemoDetails(2, "demo 2","description 2"));
			} else {
				//TODO start map
			}
	  
  }
}
