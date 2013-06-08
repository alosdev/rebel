package de.alosdev.rebel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends FragmentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Button btn = (Button) findViewById(R.id.Button1);
		btn.setOnClickListener(this);
	}

	@Override
  public void onClick(View v) {
		if (v.getId() == R.id.Button1)
			startActivity(new Intent(this, MyLocationDemoActivity.class));
  }

}
