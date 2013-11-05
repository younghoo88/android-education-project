package com.example.hellocontentprovider;

import java.net.URLEncoder;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
	String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
	String selection = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) and ("+ContactsContract.Contacts.DISPLAY_NAME + " != ''))";
	String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
	
	ListView listView;
	SimpleCursorAdapter mAdapter;
	Cursor mCursor = null;
	
	EditText keywordView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		keywordView = (EditText)findViewById(R.id.editText1);
		listView = (ListView)findViewById(R.id.listView1);
		String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
		int[] to = {android.R.id.text1};
		mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
		listView.setAdapter(mAdapter);
		
		keywordView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String keyword = "" + s;
				searchPhoneBook(keyword);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String keyword = keywordView.getText().toString();
				searchPhoneBook(keyword);
			}
		});
		
	}

	private void searchPhoneBook(String keyword) {
		Uri uri;
		if (keyword != null && !keyword.equals("")) {
			uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(keyword));
		} else {
			uri = contactUri;
		}
		Cursor c = getContentResolver().query(uri, projection, selection, null, sortOrder);
		mCursor = c;
		mAdapter.swapCursor(c);
	}
	@Override
	protected void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
