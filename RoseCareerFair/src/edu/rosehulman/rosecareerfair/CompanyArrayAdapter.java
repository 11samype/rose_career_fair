package edu.rosehulman.rosecareerfair;

import java.util.List;

import com.appspot.rose_hulman_career_fair.careerfair.model.Company;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CompanyArrayAdapter extends ArrayAdapter<Company>{
	
	private ToggleButton mFavoriteButton;
	private Context mContext;
	
	public CompanyArrayAdapter(Context context, int resource, int textViewResourceId, List<Company> companies) {
		super(context, resource, textViewResourceId, companies);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView companyTextView = (TextView) view.findViewById(android.R.id.text1);
//		companyTextView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
		companyTextView.setText(getItem(position).getName());
//		mFavoriteButton = (ToggleButton) mView.findViewById(android.R.id.button1);
//		mFavoriteButton.setBackgroundColor(mView.getResources().getColor(R.color.gray));
//		mFavoriteButton.setChecked(false);
//		mFavoriteButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(mFavoriteButton.isChecked()){
//					mFavoriteButton.setBackgroundColor(mView.getResources().getColor(R.color.gray));
//					mFavoriteButton.setChecked(false);
//				}
//				else{
//					mFavoriteButton.setBackgroundColor(mView.getResources().getColor(R.color.red));
//					mFavoriteButton.setChecked(true);
//				}
//				
//			}
//		});
		return view;
	}
}
