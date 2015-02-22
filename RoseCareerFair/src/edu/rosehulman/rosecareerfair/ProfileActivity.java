package edu.rosehulman.rosecareerfair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		setTitle(R.string.profile);
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.profile, menu);
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		
//		switch (id) {
//		case R.id.profile_map:
//			
//			DialogFragment df = new DialogFragment() {
//				
//				@Override
//				public Dialog onCreateDialog(Bundle savedInstanceState) {
//					
//					AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
//					LayoutInflater inflator = getActivity().getLayoutInflater();
//					builder.setView(inflator.inflate(R.layout.dialog_map, null));
//					builder.setTitle(getString(R.string.map));
//					
//					return builder.create();
//				}
//				
//			};
//			
//			df.show(getFragmentManager(), "map");
//			
//			return true;
//		}
//		
//		return super.onOptionsItemSelected(item);
//	}
}
