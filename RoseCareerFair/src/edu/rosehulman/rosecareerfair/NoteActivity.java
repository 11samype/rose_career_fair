package edu.rosehulman.rosecareerfair;

import java.io.IOException;
import java.util.Calendar;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair;
import com.appspot.rose_hulman_career_fair.careerfair.model.Company;
import com.appspot.rose_hulman_career_fair.careerfair.model.Note;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NoteActivity extends Activity {

	boolean scheduled;
	private Company mCompany;
	private Note mNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		Intent intent = getIntent();
		
		Bundle extras = intent.getExtras();
		mCompany = new Company();
		mCompany.setName(extras.getString(MainActivity.KEY_COMPANY_NAME));
		mCompany.setBio(extras.getString(MainActivity.KEY_COMPANY_BIO));
		mCompany.setLogo(extras.getString(MainActivity.KEY_COMPANY_LOGO));
		mCompany.setEntityKey(extras.getString(MainActivity.KEY_COMPANY_ENTITY_KEY));
		mCompany.setMajors(extras.getStringArrayList(MainActivity.KEY_COMPANY_MAJORS));
		mCompany.setJobs(extras.getStringArrayList(MainActivity.KEY_COMPANY_JOBS));
		
		setTitle(mCompany.getName() + " " + getString(R.string.notes));
		
		mNote = new Note();
		requestNote();
		
		Button saveButton = (Button)findViewById(R.id.button_save);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText noteEditText = (EditText)findViewById(R.id.notes);
				
				mNote.setNote(noteEditText.getText().toString());
				
				(new InsertNoteTask()).execute(mNote);
				
				finish();
				
			}
		});
	}
	
	private void requestNote() {
		(new QueryForNoteTask()).execute();
		
	}

	class QueryForNoteTask extends AsyncTask<String, Void, Note> {

		@Override
		protected Note doInBackground(String... params) {
			Note returnedValue = null;
			
			try {
				returnedValue = MainActivity.mService.note().company(mCompany.getEntityKey()).execute();
			} catch (IOException e) {
				Log.e(MainActivity.RCF, "Error in loading, note is null: " + e);
			}
			
			Log.d(MainActivity.RCF, "note: " + returnedValue.getEntityKey());
			
			return returnedValue;
		}
		
		@Override
		protected void onPostExecute(Note result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (result == null) {
				Log.e(MainActivity.RCF, "Error in loading, note is null");
			}
			
			mNote = result;
			
			EditText noteEditText = (EditText)findViewById(R.id.notes);
			
			if (mNote != null) {
				noteEditText.setText(mNote.getNote());
			}
		}
		
	}
	
	class InsertNoteTask extends AsyncTask<Note, Void, Note> {

		@Override
		protected Note doInBackground(Note... notes) {
			Note returnNote = null;
			
			try {
				returnNote = MainActivity.mService.note().insert(notes[0]).execute();
			} catch (IOException e) {
				Log.e(MainActivity.RCF, "Error inserting note");
			}
			
			return returnNote;
		}
		
		@Override
		protected void onPostExecute(Note result) {
			if (result == null) {
				Log.e(MainActivity.RCF, "Error in insert, returned value is null");
			}
			super.onPostExecute(result);
		}
		
	}
}
