package edu.rosehulman.rosecareerfair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	public static final String KEY_COMPANY = "KEY_COMPANY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button mapButton = (Button)findViewById(R.id.map_button);
		Button searchButton = (Button)findViewById(R.id.search_button);
		Button profileButton = (Button)findViewById(R.id.profile_button);
		
		Button companyButton = (Button)findViewById(R.id.company_button);
		
		companyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent companyIntent = new Intent(getApplicationContext(), CompanyActivity.class);
				companyIntent.putExtra(KEY_COMPANY, new Company());
				startActivity(companyIntent);
				
			}
		});
		
		Button noteButton = (Button)findViewById(R.id.note_button);
		noteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
				startActivity(noteIntent);
				
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
				startActivity(searchIntent);
				
			}
		});
	}
}