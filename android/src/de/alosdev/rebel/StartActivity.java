package de.alosdev.rebel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.JsonReader;
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
	private static final DemoDetails LOADING_DETAILS = new DemoDetails(-1, "Loading", null, null, null);
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
			JsonReader reader = null;
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL("http://rebel.polizei-news.com/test.php")
				    .openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				// Starts the query
				conn.connect();
				reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				return readDemoDetailsArray(reader);
			} catch (MalformedURLException e) {
				Log.e(TAG, "cannot parse document", e);
			} catch (IOException e) {
				Log.e(TAG, "cannot parse document", e);
			} finally {
				if (null != reader)
					try {
						reader.close();
					} catch (IOException e) {
					}
			}

			return new ArrayList<DemoDetails>();
		}

		@Override
		protected void onPostExecute(ArrayList<DemoDetails> result) {
			adapter.clear();
			adapter.addAll(result);
			loading = false;
			invalidateOptionsMenu();
		}
	}

	public ArrayList<DemoDetails> readDemoDetailsArray(JsonReader reader) throws IOException {
		ArrayList<DemoDetails> messages = new ArrayList<DemoDetails>();

		reader.beginArray();
		while (reader.hasNext()) {
			messages.add(readDemoDetail(reader));
		}
		reader.endArray();
		return messages;
	}

	private DemoDetails readDemoDetail(JsonReader reader) throws IOException {
		reader.beginObject();
		int id = -1;
		String title = null;
		String desc = null;
		String hashTag = null;
		ArrayList<Location> route = new ArrayList<Location>();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				id = reader.nextInt();
			} else if (name.equals("title")) {
				title = reader.nextString();
			} else if (name.equals("desc")) {
				desc = reader.nextString();
			} else if (name.equals("hashtag")) {
				hashTag = reader.nextString();
			} else if (name.equals("route")) {
				reader.beginArray();
				while (reader.hasNext()) {
					route.add(readLocation(reader));
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return new DemoDetails(id, title, desc, hashTag, route);
	}

	private Location readLocation(JsonReader reader) throws IOException {
		reader.beginObject();
		double latitude = .0, longitude = .0;
		String label = null;
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("latitude")) {
				latitude = reader.nextDouble();
			} else if (name.equals("longitude")) {
				longitude = reader.nextDouble();
			} else if (name.equals("label")) {
				label = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		Location loc = new Location(label);
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		return loc;
	}
}
