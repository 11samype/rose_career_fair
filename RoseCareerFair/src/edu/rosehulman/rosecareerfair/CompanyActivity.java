package edu.rosehulman.rosecareerfair;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CompanyActivity extends Activity {
	
	private Company company;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		Intent intent = getIntent();
		company = intent.getParcelableExtra(MainActivity.KEY_COMPANY);
		
		TextView workTypeText = (TextView)findViewById(R.id.company_work_type_text);
		
		boolean[] workType = company.getWorkType();
		String workTypeString = new String();
		
		if (workType[0] && workType[1]) {
			workTypeString = "Interns & Full Time";
		} else if (workType[0]) {
			workTypeString = "Interns";
		} else {
			workTypeString = "Full Time";
		}
		
		workTypeText.setText(workTypeString);
		
		TextView majorListText = (TextView)findViewById(R.id.company_major_list_text);
		
		String majorText = new String();
		majorText = "";
		String[] majors = company.getMajors();
		majorText += majors[0];
		
		for (int i = 1; i < majors.length; i++) {
			majorText += (", " + majors[i]);
		}
		
		majorListText.setText(majorText);
		
		Button waitTimeButton = (Button)findViewById(R.id.button_wait_time);
		waitTimeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment df = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle(company.getName() + " " + getResources().getString(R.string.wait_time));
						
						builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// cancel
								
							}
						});
						
						builder.setItems(R.array.radio_button_time_array, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 1:
									// send short line
									break;

								case 2:
									// send medium line
									break;
									
								case 3:
									// send long line
									break;
								}
								
							}
						});
						
						return builder.create();
					}
				};
				
				df.show(getFragmentManager(), "waittime");
				
			}
		});
		
		Button noteButton = (Button)findViewById(R.id.button_notes);
		noteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
				startActivity(noteIntent);
				
			}
		});
		
		Button aboutButton = (Button)findViewById(R.id.button_about);
		aboutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DialogFragment df = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle(getResources().getString(R.string.about) + " " + company.getName());
						builder.setMessage(company.getBio());
						
						builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// nothing
								
							}
						});
						
						return builder.create();
					}
				};
				
				df.show(getFragmentManager(), "about");
				
			}
		});
		
	}
}
