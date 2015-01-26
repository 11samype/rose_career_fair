package edu.rosehulman.rosecareerfair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class NoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		final Button interviewButton = (Button) findViewById(R.id.interview_button);
		interviewButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment df = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle(R.string.interview);
						
						LayoutInflater inflator = LayoutInflater.from(getApplicationContext());
						View interviewView = inflator.inflate(R.layout.dialog_interview, null);
						
						builder.setView(interviewView);
						builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Add Interview
								
								DatePicker datePicker = (DatePicker) ((AlertDialog) dialog).findViewById(R.id.datePicker1);
								int year = datePicker.getYear();
								int month = datePicker.getMonth();
								int dayOfMonth = datePicker.getDayOfMonth();
								
								TimePicker timePicker = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.timePicker1);
								int hour = timePicker.getCurrentHour();
								int minute = timePicker.getCurrentMinute();
								
								interviewButton.setText("" + month + "/"+ dayOfMonth + "/" + year + " " + hour + ":" + minute);
								
							}
						});
						
						builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Remove interview
								interviewButton.setText(R.string.schedule_interview);
								
							}
						});
						
						return builder.create();
					}
				};
				df.show(getFragmentManager(), "interview");
				
			}
		});
	}
}
