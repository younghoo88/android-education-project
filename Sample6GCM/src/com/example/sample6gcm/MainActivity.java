package com.example.sample6gcm;

import java.io.IOException;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends ActionBarActivity {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String SENDER_ID = "972646024433";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	boolean isFirst = true;

	@Override
	protected void onResume() {
		super.onResume();

		if (checkPlayServices()) {
			String regId = PropertyManager.getInstance().getRegistrationId();
			if (!regId.equals("")) {
				runOnUiThread(nextAction);
			} else {
				registerInBackground();
			}
		} else {
			if (isFirst) {
				isFirst = false;
			} else {
				// Toast...
			}
		}
	}
	

	Runnable nextAction = new Runnable() {
		
		@Override
		public void run() {
			
		}
	};
	
	GoogleCloudMessaging gcm;

	private void registerInBackground() {
		new AsyncTask<Void, Integer, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(MainActivity.this);
					}
					String regid = gcm.register(SENDER_ID);

					PropertyManager.getInstance().setRegistrationId(regid);

					ServerUtilities.register(MainActivity.this, regid);
				} catch (IOException ex) {
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				runOnUiThread(nextAction);
			}
		}.execute(null, null, null);
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST);
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
				dialog.show();
			} else {
				// To Do...
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
