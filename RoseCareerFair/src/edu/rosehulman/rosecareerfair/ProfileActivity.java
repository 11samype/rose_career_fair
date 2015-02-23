package edu.rosehulman.rosecareerfair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair.Company;
import com.appspot.rose_hulman_career_fair.careerfair.model.CompanyCollection;

import edu.rosehulman.rosecareerfair.MainActivity.QueryForCompaniesTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends ListActivity {
	
	private String mCompanyOrder = "name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		setTitle(R.string.favorites);
		
		updateCompanies();
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final com.appspot.rose_hulman_career_fair.careerfair.model.Company currentCompany = (com.appspot.rose_hulman_career_fair.careerfair.model.Company) getListAdapter().getItem(position);
		
		Intent companyIntent = new Intent(getApplicationContext(), CompanyActivity.class);

		companyIntent.putExtra(MainActivity.KEY_COMPANY_NAME, currentCompany.getName());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_BIO, currentCompany.getBio());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_LOGO, currentCompany.getLogo());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_TABLE, currentCompany.getTable());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_WEBSITE, currentCompany.getWebsite());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_ENTITY_KEY, currentCompany.getEntityKey());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_MAJORS, (ArrayList<String>)currentCompany.getMajors());
		companyIntent.putExtra(MainActivity.KEY_COMPANY_JOBS, (ArrayList<String>)currentCompany.getJobs());
		
		startActivity(companyIntent);
		
		super.onListItemClick(l, v, position, id);
	}
	
	private void updateCompanies() {
		(new QueryForCompaniesTask()).execute();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.profile_map:
			
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
			
		case R.id.profile_help:
			
			DialogFragment dfHelp = new DialogFragment() {
				
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.help);
					builder.setMessage(R.string.profile_help_text);
					
					return builder.create();
				}
				
			};
			
			dfHelp.show(getFragmentManager(), "help");
			
			return true;
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	class QueryForCompaniesTask extends AsyncTask<Void, Void, CompanyCollection> {

		@Override
		protected CompanyCollection doInBackground(Void... params) {
			
			CompanyCollection companies = null;
			try {
				Company.Favorite.List query = MainActivity.mService.company().favorite().list();
				query.setLimit(50L); // pages?
				query.setOrder(mCompanyOrder);
				companies = query.execute();
			} catch (IOException e) {
				Log.e(MainActivity.RCF, "Error in loading, companies is null: " + e);
			}
			
			return companies;
		}
		
		@Override
		protected void onPostExecute(CompanyCollection result) {
			super.onPostExecute(result);
			if (result == null) {
				Log.e(MainActivity.RCF, "Error in loading, companies is null");
				return;
			}
			
			if (result.getItems() == null) {
				result.setItems(new ArrayList<com.appspot.rose_hulman_career_fair.careerfair.model.Company>());
			}
			
			List<com.appspot.rose_hulman_career_fair.careerfair.model.Company> companies = result.getItems();
			
			List<com.appspot.rose_hulman_career_fair.careerfair.model.Company> companiesToRemove = new ArrayList<com.appspot.rose_hulman_career_fair.careerfair.model.Company>();
			
			// find
			for (com.appspot.rose_hulman_career_fair.careerfair.model.Company company : companies) {
				if (!company.getFavorite()) {
					companiesToRemove.add(company);
				}
			}
			
			// remove
			for (com.appspot.rose_hulman_career_fair.careerfair.model.Company company : companiesToRemove) {
				companies.remove(company);
				Log.d(MainActivity.RCF, company.getName());
			}
			
			if (companies.size() < 1) {
				TextView loadText = (TextView)findViewById(android.R.id.empty);
				loadText.setText(R.string.profile_help_text);
			}
			
			FavoriteArrayAdapter adapter = new FavoriteArrayAdapter(ProfileActivity.this,
					android.R.layout.simple_expandable_list_item_2, android.R.id.text1, companies);
			setListAdapter(adapter);
		}
		
	}
}
