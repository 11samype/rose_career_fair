package edu.rosehulman.rosecareerfair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair;
import com.appspot.rose_hulman_career_fair.careerfair.Careerfair.Linelength;
import com.appspot.rose_hulman_career_fair.careerfair.model.Company;
import com.appspot.rose_hulman_career_fair.careerfair.model.LineLength;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CompanyActivity extends Activity {
	
	private Company mCompany;
	private LineLength mLinelength;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		Intent intent = getIntent();
		
		Bundle extras = intent.getExtras();
		mCompany = new Company();
		mCompany.setName(extras.getString(MainActivity.KEY_COMPANY_NAME));
		mCompany.setBio(extras.getString(MainActivity.KEY_COMPANY_BIO));
		mCompany.setLogo(extras.getString(MainActivity.KEY_COMPANY_LOGO));
		mCompany.setEntityKey(extras.getString(MainActivity.KEY_COMPANY_ENTITY_KEY));
		mCompany.setTable(extras.getLong(MainActivity.KEY_COMPANY_TABLE));
		mCompany.setWebsite(extras.getString(MainActivity.KEY_COMPANY_WEBSITE));
		mCompany.setMajors(extras.getStringArrayList(MainActivity.KEY_COMPANY_MAJORS));
		mCompany.setJobs(extras.getStringArrayList(MainActivity.KEY_COMPANY_JOBS));
		
		String titleString = mCompany.getName() + ": " + getString(R.string.table) + " ";
		
		if (mCompany.getTable() == null) {
			titleString += getString(R.string.na);
		} else {
			titleString += mCompany.getTable().intValue();
		}
		
		setTitle(mCompany.getName() + " " + getString(R.string.table) + " " + mCompany.getTable().intValue());
		
		Log.d(MainActivity.RCF, mCompany.getLogo());
		
		ImageView companyLogo = (ImageView) findViewById(R.id.company_logo);
		new ImageLoadTask(mCompany.getLogo(), companyLogo).execute();
		
//		company = intent.getParcelableExtra(MainActivity.KEY_COMPANY);
		
//		TextView workTypeText = (TextView)findViewById(R.id.company_work_type_text);
//		
//		boolean[] workType = company.getWorkType();
//		String workTypeString = new String();
//		
//		if (workType[0] && workType[1]) {
//			workTypeString = "Interns & Full Time";
//		} else if (workType[0]) {
//			workTypeString = "Interns";
//		} else {
//			workTypeString = "Full Time";
//		}
//		
//		workTypeText.setText(workTypeString);
//		
//		TextView majorListText = (TextView)findViewById(R.id.company_major_list_text);
//		
//		String majorText = new String();
//		majorText = "";
//		String[] majors = company.getMajors();
//		majorText += majors[0];
//		
//		for (int i = 1; i < majors.length; i++) {
//			majorText += (", " + majors[i]);
//		}
//		
//		majorListText.setText(majorText);
		
		updateLineStatus();
		
//		Log.d(MainActivity.RCF, mLinelength.getLength().toString());
		
		Button waitTimeButton = (Button)findViewById(R.id.button_wait_time);
		waitTimeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment df = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle(mCompany.getName() + " " + getResources().getString(R.string.wait_time));
						
						builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// cancel
								
							}
						});
						
						builder.setItems(R.array.radio_button_time_array, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								int length = which + 1;
								
								Log.d(MainActivity.RCF, "line length to send: " + length);
								
								LineLength newLineLength = new LineLength();

								newLineLength.setCompanyEntityKey(mCompany.getEntityKey());
								newLineLength.setLength((long) length);

								(new InsertLineLengthTask()).execute(newLineLength);
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
				noteIntent.putExtra(MainActivity.KEY_COMPANY_NAME, mCompany.getName());
				noteIntent.putExtra(MainActivity.KEY_COMPANY_BIO, mCompany.getBio());
				noteIntent.putExtra(MainActivity.KEY_COMPANY_LOGO, mCompany.getLogo());
				noteIntent.putExtra(MainActivity.KEY_COMPANY_ENTITY_KEY, mCompany.getEntityKey());
				noteIntent.putExtra(MainActivity.KEY_COMPANY_MAJORS, (ArrayList<String>)mCompany.getMajors());
				noteIntent.putExtra(MainActivity.KEY_COMPANY_JOBS, (ArrayList<String>)mCompany.getJobs());
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
						builder.setTitle(getResources().getString(R.string.about) + " " + mCompany.getName());
						builder.setMessage(mCompany.getBio());
						
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
		
		ImageButton internetButton = (ImageButton)findViewById(R.id.button_internet);
		internetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent internetIntent = new Intent(Intent.ACTION_VIEW);
				internetIntent.setData(Uri.parse(mCompany.getWebsite()));
				startActivity(internetIntent);
				
			}
		});
		
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
						builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								DatePicker datePicker = (DatePicker) ((AlertDialog) dialog).findViewById(R.id.datePicker1);
								int year = datePicker.getYear();
								int month = datePicker.getMonth();
								int dayOfMonth = datePicker.getDayOfMonth();
								
								TimePicker timePicker = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.timePicker1);
								int hour = timePicker.getCurrentHour();
								int minute = timePicker.getCurrentMinute();
								
//								String leadingZero = "";
								
//								if (minute < 10) {
//									leadingZero = "0";
//								}
								Calendar beginTime = Calendar.getInstance();
						    	beginTime.set(year, month, dayOfMonth, hour, minute);
						        Intent intent = new Intent(Intent.ACTION_INSERT);
						        intent.setType("vnd.android.cursor.item/event");
						        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
						        intent.putExtra(Events.TITLE, mCompany.getName() + " Interview");
						        intent.putExtra(Events.DESCRIPTION, "Interview");

						        // Use the Calendar app to view the time.
						        startActivity(intent);
//						        Toast.makeText(getApplicationContext(), "Interview made", Toast.LENGTH_LONG).show();

							}
						});
						
						
						builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
						
						return builder.create();
					}
				};
				df.show(getFragmentManager(), "interview");
				
			}
		});
		
		TextView workTypeTextView = (TextView)findViewById(R.id.company_work_type_text);
		
		workTypeTextView.setText(getWorkTypeText());
		
		TextView majorTextView = (TextView)findViewById(R.id.company_major_list_text);
		
		majorTextView.setText(getMajorText());
		
		
	}
	
	private String getMajorText() {
		
		List<String> majors = mCompany.getMajors();
		
		String majorString = "";
		
		if (majors == null) {
			majorString = getString(R.string.na) + " ";
		} else {
			
			for (String major : majors) {
				Log.d(MainActivity.RCF, major);
				majorString += (" " + major + ",");
				
			}

		}
		
		return majorString.substring(0, majorString.length() - 1);
	}

	private String getWorkTypeText() {
		List<String> jobs = mCompany.getJobs();
		
		String jobString = "";
		
		if (jobs == null) {
			jobString = getString(R.string.na) + " ";
			
		} else {
			for (String job : jobs) {
				Log.d(MainActivity.RCF, job);
				jobString += (job + " \n");
				
			}
		}
		
		return jobString.substring(0, jobString.length() - 1);
	}

	private void updateLineStatus() {
		(new QueryForLineLengthTask()).execute();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.company, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.company_map:
			
			DialogFragment df = new DialogFragment() {
				
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					
					AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
					LayoutInflater inflator = getActivity().getLayoutInflater();
					builder.setView(inflator.inflate(R.layout.dialog_map, null));
					builder.setTitle(getString(R.string.map) + ": " + mCompany.getName() + 
							": " + getString(R.string.table) + " " + mCompany.getTable());
					
					return builder.create();
				}
				
			};
			
			df.show(getFragmentManager(), "map");
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
		
		private String url;
		private ImageView imageView;
		
		public ImageLoadTask(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}
		
	}
	
	class QueryForLineLengthTask extends AsyncTask<String, Void, LineLength> {

		@Override
		protected LineLength doInBackground(String... params) {
			LineLength returnedValue = null;
			
			Log.d(MainActivity.RCF, "executed: " + mCompany.getEntityKey());
			
			try {
				returnedValue = MainActivity.mService.linelength().status(mCompany.getEntityKey()).execute();
			} catch (IOException e) {
				Log.e(MainActivity.RCF, "Error in loading, linelength is null: " + e);
			}
			
			Log.d(MainActivity.RCF, "linelength: " + returnedValue.getLength());
			
			return returnedValue;
		}
		
		@Override
		protected void onPostExecute(LineLength result) {
			super.onPostExecute(result);
			
			Log.d(MainActivity.RCF, "executed");
			
			if (result == null) {
				Log.e(MainActivity.RCF, "Error in loading, linelength is null");
			}
			
			mLinelength = result;
			
			Log.d(MainActivity.RCF, mCompany.getEntityKey());
			
			Log.d(MainActivity.RCF, "wait time: " + mLinelength.getLength());
			
			Button lineLengthButton = (Button)findViewById(R.id.button_wait_time_status);
			
			int length = mLinelength.getLength().intValue();
			Log.d(MainActivity.RCF, "length: " + length);
			
			int linelengthString = 0;
			int color = 0;
			
			switch (length) {
			case 0:
				linelengthString = R.string.unknown;
				color = R.color.gray;
				
				break;
			
			case 1:
				linelengthString = R.string.short_line;
				color = R.color.green;
				
				break;
				
			case 2:
				linelengthString = R.string.medium_line;
				color = R.color.yellow;
				
				break;
				
			case 3:
				linelengthString = R.string.long_line;
				color = R.color.red;
				
				break;

			default:
				break;
			}
			
			Log.d(MainActivity.RCF, "line length string: " + linelengthString + 
					" color: " + color);
			
			if ((color != 0) && (linelengthString != 0)) {
				lineLengthButton.setBackgroundColor(getResources().getColor(color));
				lineLengthButton.setText(linelengthString);
			}
		}
	}
	
	class InsertLineLengthTask extends AsyncTask<LineLength, Void, LineLength> {

		@Override
		protected LineLength doInBackground(LineLength... params) {
			LineLength returnLineLength = null;
			try {
				returnLineLength = MainActivity.mService.linelength().insert(params[0]).execute();
			} catch (IOException e) {
				Log.e(MainActivity.RCF, "Error inserting line length");
			}
			return returnLineLength;
		}
		
		@Override
		protected void onPostExecute(LineLength result) {
			if (result == null) {
				Log.e(MainActivity.RCF, "Error in insert, returned value is null");
			}
			
			updateLineStatus();
		}
		
	}
}
