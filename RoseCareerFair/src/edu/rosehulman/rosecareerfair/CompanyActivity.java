package edu.rosehulman.rosecareerfair;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CompanyActivity extends Activity {
	
	private Company company;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		Intent intent = getIntent();
		company = intent.getParcelableExtra(MainActivity.KEY_COMPANY);
		
		TextView bioText = (TextView)findViewById(R.id.company_bio_text);
		bioText.setText(company.getBio());
		
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
		
	}
}
