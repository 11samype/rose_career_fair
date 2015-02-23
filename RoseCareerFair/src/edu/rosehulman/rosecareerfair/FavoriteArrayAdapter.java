package edu.rosehulman.rosecareerfair;

import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.model.Company;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FavoriteArrayAdapter extends ArrayAdapter<Company>{
	
	private Context mContext;
	
	public FavoriteArrayAdapter(Context context, int resource, int textViewResourceId, List<Company> companies) {
		super(context, resource, textViewResourceId, companies);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView companyTextView = (TextView) view.findViewById(android.R.id.text1);
		companyTextView.setText(getItem(position).getName());

		return view;
	}
}
