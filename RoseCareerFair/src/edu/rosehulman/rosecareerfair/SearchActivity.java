package edu.rosehulman.rosecareerfair;

import java.io.IOException;
import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.Careerfair;
import com.appspot.rose_hulman_career_fair.careerfair.Careerfair.Company;
import com.appspot.rose_hulman_career_fair.careerfair.model.CompanyCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class SearchActivity extends ListActivity {

	public static final String KEY_COMPANY = "KEY_COMPANY";
	public static final String KEY_COMPANY_ENTITY_KEY = "KEY_COMPANY_ENTITY_KEY";
	public static final String KEY_COMPANY_NAME = "KEY_COMPANY_NAME";
	public static final String KEY_COMPANY_BIO = "KEY_COMPANY_BIO";
	public static final String KEY_COMPANY_LOGO = "KEY_COMPANY_LOGO";
	public Careerfair mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		Careerfair.Builder builder = new Careerfair.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), null);
		mService = builder.build();
		
		updateCompanies();
	}
	
	private void updateCompanies() {
		(new QueryForCompaniesTask()).execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.company_search, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final com.appspot.rose_hulman_career_fair.careerfair.model.Company currentCompany = (com.appspot.rose_hulman_career_fair.careerfair.model.Company) getListAdapter().getItem(position);
		
		Intent companyIntent = new Intent(getApplicationContext(), CompanyActivity.class);
//		companyIntent.putExtra(KEY_COMPANY, new Company1());
		companyIntent.putExtra(KEY_COMPANY_NAME, currentCompany.getName());
		companyIntent.putExtra(KEY_COMPANY_BIO, currentCompany.getBio());
		companyIntent.putExtra(KEY_COMPANY_LOGO, currentCompany.getLogo());
		companyIntent.putExtra(KEY_COMPANY_ENTITY_KEY, currentCompany.getEntityKey());
		
		startActivity(companyIntent);
		
		super.onListItemClick(l, v, position, id);
	}
	
	class QueryForCompaniesTask extends AsyncTask<Void, Void, CompanyCollection> {

		@Override
		protected CompanyCollection doInBackground(Void... params) {
			
			CompanyCollection companies = null;
			try {
				Company.List query = mService.company().list();
				query.setLimit(50L); // pages?
				query.setOrder("name");
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
			}
			List<com.appspot.rose_hulman_career_fair.careerfair.model.Company> companies = result.getItems();
			CompanyArrayAdapter adapter = new CompanyArrayAdapter(SearchActivity.this,
					android.R.layout.simple_expandable_list_item_1, android.R.id.text1, companies);
			setListAdapter(adapter);
		}
		
	}
}
