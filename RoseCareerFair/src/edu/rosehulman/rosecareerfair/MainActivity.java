package edu.rosehulman.rosecareerfair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair;
import com.appspot.rose_hulman_career_fair.careerfair.Careerfair.Company;
import com.appspot.rose_hulman_career_fair.careerfair.model.CompanyCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	static final int REQUEST_ACCOUNT_PICKER = 1;
	public static final String RCF = "RCF";
	public static final String KEY_COMPANY = "KEY_COMPANY";
	public static final String KEY_COMPANY_ENTITY_KEY = "KEY_COMPANY_ENTITY_KEY";
	public static final String KEY_COMPANY_NAME = "KEY_COMPANY_NAME";
	public static final String KEY_COMPANY_BIO = "KEY_COMPANY_BIO";
	public static final String KEY_COMPANY_LOGO = "KEY_COMPANY_LOGO";
	public static final String KEY_COMPANY_MAJORS = "KEY_COMPANY_MAJORS";
	public static final String KEY_COMPANY_JOBS = "KEY_COMPANY_JOBS";
	public static final String KEY_COMPANY_TABLE = "KEY_COMPANY_TABLE";
	public static final String KEY_COMPANY_WEBSITE = "KEY_COMPANY_WEBSITE";
	public static Careerfair mService;
	
	public static final String SHARED_PREFERENCES_NAME = "RoseCareerFair";
	public static final String PREF_ACCOUNT_NAME = "PREF_ACCOUNT_NAME";
	
	private String mCompanyOrder;
	
	GoogleAccountCredential mCredential;
	
	SharedPreferences mSettings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCompanyOrder = "name";
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				com.appspot.rose_hulman_career_fair.careerfair.model.Company currentCompany = (com.appspot.rose_hulman_career_fair.careerfair.model.Company) getListAdapter().getItem(position);
				
				// TODO
				
//				view.setBackgroundColor(getResources().getColor(R.color.light_sky_blue));
				
				if (currentCompany.getFavorite() == true) {
					currentCompany.setFavorite(false);
				} else {
					currentCompany.setFavorite(true);
				}
				
				(new InsertCompanyFavoriteTask()).execute(currentCompany);
				
				return true;
			}
		});
		
		mCredential = GoogleAccountCredential.usingAudience(this, 
				"server:client_id:226229190503-97pgt09qc8tr0g368pa6fpf0kjsof3pm.apps.googleusercontent.com");
		
		mSettings = getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
		setAccountName(mSettings.getString(PREF_ACCOUNT_NAME, null));
		
		Careerfair.Builder builder = new Careerfair.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), mCredential);
		mService = builder.build();
		
		if (mCredential.getSelectedAccountName() == null) {
			// Not signed in, show login window or request an existing account.
			chooseAccount();
		}
		
		updateCompanies();
		
	}
	
	private void updateCompanies() {
		(new QueryForCompaniesTask()).execute();
		
	}
	
	void chooseAccount() {
		// This picker is built in to the Android framework.
		startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
	}
	
	/**
	 * Save the account name in preferences and the credentials
	 * 
	 * @param accountName
	 */
	private void setAccountName(String accountName) {
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString(PREF_ACCOUNT_NAME, accountName);
		editor.commit();
		mCredential.setSelectedAccountName(accountName);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (data != null && data.getExtras() != null) {
				String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					setAccountName(accountName); // User is authorized.
					updateCompanies();
				}
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final com.appspot.rose_hulman_career_fair.careerfair.model.Company currentCompany = (com.appspot.rose_hulman_career_fair.careerfair.model.Company) getListAdapter().getItem(position);
		
		Intent companyIntent = new Intent(getApplicationContext(), CompanyActivity.class);

		companyIntent.putExtra(KEY_COMPANY_NAME, currentCompany.getName());
		companyIntent.putExtra(KEY_COMPANY_BIO, currentCompany.getBio());
		companyIntent.putExtra(KEY_COMPANY_LOGO, currentCompany.getLogo());
		companyIntent.putExtra(KEY_COMPANY_TABLE, currentCompany.getTable());
		companyIntent.putExtra(KEY_COMPANY_WEBSITE, currentCompany.getWebsite());
		companyIntent.putExtra(KEY_COMPANY_ENTITY_KEY, currentCompany.getEntityKey());
		companyIntent.putExtra(KEY_COMPANY_MAJORS, (ArrayList<String>)currentCompany.getMajors());
		companyIntent.putExtra(KEY_COMPANY_JOBS, (ArrayList<String>)currentCompany.getJobs());
		
		startActivity(companyIntent);
		
		super.onListItemClick(l, v, position, id);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.main_map:
			
			DialogFragment df = new DialogFragment() {
				
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					
					AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
					LayoutInflater inflator = getActivity().getLayoutInflater();
					builder.setView(inflator.inflate(R.layout.dialog_map, null));
					builder.setTitle(getString(R.string.map));
					
					return builder.create();
				}
				
			};
			
			df.show(getFragmentManager(), "map");
			
			return true;
			
		case R.id.main_profile:
			
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			startActivity(profileIntent);
			
			return true;
			
		case R.id.main_account:
			
			chooseAccount();
			
			return true;
			
		case R.id.az:

			mCompanyOrder = "name";

			updateCompanies();
			
			return true;
			
		case R.id.za:
			
			mCompanyOrder = "-name";

			updateCompanies();
			
			return true;

		}
		
		return super.onOptionsItemSelected(item);
	}
	

	class QueryForCompaniesTask extends AsyncTask<Void, Void, CompanyCollection> {

		@Override
		protected CompanyCollection doInBackground(Void... params) {
			
			CompanyCollection companies = null;
			try {
				Log.d(RCF, "Using account name = " + mCredential.getSelectedAccountName());
				Log.d(RCF, "Order: " + mCompanyOrder);
				Company.Favorite.List query = mService.company().favorite().list();
				query.setLimit(50L); // pages?
				query.setOrder(mCompanyOrder);
				companies = query.execute();
			} catch (IOException e) {
				Log.e(RCF, "Error in loading, companies is null: " + e);
			}
			
			return companies;
		}
		
		@Override
		protected void onPostExecute(CompanyCollection result) {
			super.onPostExecute(result);
			if (result == null) {
				Log.e(RCF, "Error in loading, companies is null");
				return;
			}
			
			if (result.getItems() == null) {
				result.setItems(new ArrayList<com.appspot.rose_hulman_career_fair.careerfair.model.Company>());
			}
			
			List<com.appspot.rose_hulman_career_fair.careerfair.model.Company> companies = result.getItems();
			
			CompanyArrayAdapter adapter = new CompanyArrayAdapter(MainActivity.this,
					android.R.layout.simple_expandable_list_item_2, android.R.id.text1, companies);
			setListAdapter(adapter);
		}
		
	}
	
	class InsertCompanyFavoriteTask extends AsyncTask<com.appspot.rose_hulman_career_fair.careerfair.model.Company, Void, com.appspot.rose_hulman_career_fair.careerfair.model.Company> {

		@Override
		protected com.appspot.rose_hulman_career_fair.careerfair.model.Company doInBackground(com.appspot.rose_hulman_career_fair.careerfair.model.Company... items) {
			com.appspot.rose_hulman_career_fair.careerfair.model.Company returnCompany = null;
			try {
				returnCompany = mService.company().favorite().insert(items[0]).execute();
				return returnCompany;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return returnCompany;
		}
		
		@Override
		protected void onPostExecute(
				com.appspot.rose_hulman_career_fair.careerfair.model.Company result) {
			super.onPostExecute(result);
			if (result == null) {
				Log.d(MainActivity.RCF, "Error inserting assignment, result is null");
				return;
			}
			updateCompanies();
		}
		
	}

}
