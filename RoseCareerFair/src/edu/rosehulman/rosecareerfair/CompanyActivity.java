package edu.rosehulman.rosecareerfair;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair;
import com.appspot.rose_hulman_career_fair.careerfair.model.Company;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyActivity extends Activity {
	
	private Company company;
	private Careerfair mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		Intent intent = getIntent();
		
		Bundle extras = intent.getExtras();
		company = new Company();
		company.setName(extras.getString(SearchActivity.KEY_COMPANY_NAME));
		company.setBio(extras.getString(SearchActivity.KEY_COMPANY_BIO));
		company.setLogo(extras.getString(SearchActivity.KEY_COMPANY_LOGO));
		
		Log.d(MainActivity.RCF, company.getLogo());
		
		Careerfair.Builder builder = new Careerfair.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), null);
		mService = builder.build();
		
		ImageView companyLogo = (ImageView) findViewById(R.id.company_logo);
		new ImageLoadTask(company.getLogo(), companyLogo).execute();
		
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
}
