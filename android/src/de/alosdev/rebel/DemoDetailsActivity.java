/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.alosdev.rebel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.JsonWriter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import de.alosdev.rebel.dialog.ReportDialog;
import de.alosdev.rebel.dialog.ReportDialog.ReportDialogListener;
import de.alosdev.rebel.domain.DemoDetails;

/**
 * This demo shows how GMS Location can be used to check for changes to the
 * users location. The "My Location" button uses GMS Location to set the blue
 * dot representing the users location. To track changes to the users location
 * on the map, we request updates from the {@link LocationClient}.
 */
public class DemoDetailsActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener,
    LocationListener, ReportDialogListener {

	private static final String TAG = DemoDetailsActivity.class.getSimpleName();
	private static final String BUNDLE_DETAIL = TAG + ".demoDetails";
	// These settings are the same as the settings for the map. They will in fact
	// give you updates at
	// the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000) // 5
	                                                                                          // seconds
	    .setFastestInterval(16) // 16ms = 60fps
	    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public static void start(Activity activity, DemoDetails details) {
		Intent startIntent = new Intent(activity, DemoDetailsActivity.class);
		startIntent.putExtra(BUNDLE_DETAIL, details);
		activity.startActivity(startIntent);
	}

	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private DemoDetails details;
	private ListView list;
	private Location lastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_details);
		details = getIntent().getParcelableExtra(BUNDLE_DETAIL);
		getActionBar().setTitle(details.title);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		list = (ListView) findViewById(R.id.list);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			onBackPressed();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
		UiSettings uiSettings = mMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(false);
		uiSettings.setCompassEnabled(true);
		mMap.clear();
		for (Location loc : details.route)
			mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())));
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
			    this); // OnConnectionFailedListener
		}
	}

	public void createReport(View view) {
		if (lastLocation == null) {
			Toast.makeText(getApplicationContext(), "No location available", Toast.LENGTH_SHORT).show();
		} else {
			FragmentManager fm = getFragmentManager();
			ReportDialog dialog = new ReportDialog();
			dialog.show(fm, "fragment_report");
		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (null == lastLocation) {
			// final float distance = lastLocation.distanceTo(location);
			// Log.d(TAG, "distance : " + distance + "m");
			// if (distance > 50f) {
			// createBounds(location);
			// }
			createBounds(location);
		}
		lastLocation = location;
	}

	private void createBounds(Location location) {
		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
		if (null != lastLocation)
			boundsBuilder.include(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
		boundsBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));
		for (Location loc : details.route)
			boundsBuilder.include(new LatLng(loc.getLatitude(), loc.getLongitude()));

		mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 130));
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public void onFinishReportDialog(final String name, final String message) {
		if (TextUtils.isEmpty(message))
			Toast.makeText(getApplicationContext(), "you must set a message!", Toast.LENGTH_SHORT).show();
		else {

			new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					try {
						URL url = new URL("http://rebel.polizei-news.com/json.php");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();

						conn.setDoOutput(true);
						conn.setRequestMethod("POST");
						conn.setChunkedStreamingMode(0);
						conn.setUseCaches(false);

						OutputStream out = conn.getOutputStream();
						writeBody(out, name, message);
						out.close();

						return  "reported name: " + name + "; message:" + message;
					} catch (MalformedURLException e) {
						return "cannot send report";
					} catch (IOException e) {
						return "cannot send report";
					}
				}
				
				@Override
				protected void onPostExecute(String result) {
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				}

			}.execute();

		}
	}

	private void writeBody(OutputStream out, String name, String message) throws UnsupportedEncodingException,
	    IOException {
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));

		writer.beginObject();

		if (TextUtils.isEmpty(name))
			writer.name("user").value(message);
		writer.name("location").beginObject().name("longitude").value(lastLocation.getLongitude()).name("latitude")
		    .value(lastLocation.getLatitude()).endObject();
		writer.name("report").value(message);

		writer.endObject();
		writer.close();
	}
}
