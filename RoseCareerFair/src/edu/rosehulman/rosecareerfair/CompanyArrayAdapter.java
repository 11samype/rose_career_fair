package edu.rosehulman.rosecareerfair;

import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.model.Company;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CompanyArrayAdapter extends ArrayAdapter<Company>{
	
	private Context mContext;
	
	public CompanyArrayAdapter(Context context, int resource, int textViewResourceId, List<Company> companies) {
		super(context, resource, textViewResourceId, companies);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView companyTextView = (TextView) view.findViewById(android.R.id.text1);
		companyTextView.setText(getItem(position).getName());
		
		TextView tableTextView = (TextView) view.findViewById(android.R.id.text2);
		tableTextView.setText(mContext.getString(R.string.table) + " " + getItem(position).getTable());
		if (getItem(position).getFavorite()) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.alice_blue));
		}

		return view;
	}
}
