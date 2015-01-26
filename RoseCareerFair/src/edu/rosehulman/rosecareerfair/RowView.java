package edu.rosehulman.rosecareerfair;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RowView extends LinearLayout{
	private TextView mCompanyNameView;
	private Button mInfoButton;
	private Button mNotesButton;
	public RowView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.row_view, this);
		mCompanyNameView = (TextView)findViewById(R.id.company_name_view);
		mInfoButton = (Button)findViewById(R.id.info_button);
		mNotesButton = (Button)findViewById(R.id.notes_button);

		}

		public void setLeftText(String string) {
			mCompanyNameView.setText(string);
		}
		

}
