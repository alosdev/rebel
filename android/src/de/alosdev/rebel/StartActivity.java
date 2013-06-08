package de.alosdev.rebel;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.alosdev.rebel.domain.DemoDetails;
import de.alosdev.rebel.view.DemoView;

public class StartActivity extends FragmentActivity implements OnItemClickListener {

	private static final String TAG = StartActivity.class.getSimpleName();
	private static final DemoDetails LOADING_DETAILS = new DemoDetails(-1, "Loading", null, null);
	private static final String BUNDLE_DEMODETAILS = TAG + ".demos";

	private CustomArrayAdapter adapter;
	boolean loading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		ListView list = (ListView) findViewById(R.id.list);

		final ArrayList<DemoDetails> items;
		if (null == savedInstanceState) {
			items = new ArrayList<DemoDetails>();
		} else
			items = savedInstanceState.getParcelableArrayList(BUNDLE_DEMODETAILS);
		adapter = new CustomArrayAdapter(getApplicationContext(), items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		if (null == savedInstanceState)
			refresh();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.refresh) {
			refresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.refresh);
		if (null != item)
			item.setEnabled(!loading);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		final int count = adapter.getCount();
		final ArrayList<DemoDetails> list = new ArrayList<DemoDetails>(count);
		for (int i = 0; i < count; i++)
			list.add(adapter.getItem(i));

		outState.putParcelableArrayList(BUNDLE_DEMODETAILS, list);
		super.onSaveInstanceState(outState);
	}

	private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
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
		if (null != details && details.demoId != -1) {
			DemoDetailsActivity.start(this, details);
		}
	}

	void refresh() {
		loading = true;
		adapter.clear();
		adapter.add(LOADING_DETAILS);
		new RefreshAsyncTask().execute();
		invalidateOptionsMenu();
	}

	class RefreshAsyncTask extends AsyncTask<Integer, Void, ArrayList<DemoDetails>> {

		@Override
		protected ArrayList<DemoDetails> doInBackground(Integer... params) {
			// TODO Api Anbindung
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error during sleep", e);
			}
			ArrayList<DemoDetails> list = new ArrayList<DemoDetails>();
			list.add(new DemoDetails(1, "Demo 1", "description 1", null));
			list.add(new DemoDetails(2, "demo 2", "description 2", null));
			return list;
		}

		@Override
		protected void onPostExecute(ArrayList<DemoDetails> result) {
			adapter.clear();
			adapter.addAll(result);
			loading = false;
			invalidateOptionsMenu();
		}
	}
}
