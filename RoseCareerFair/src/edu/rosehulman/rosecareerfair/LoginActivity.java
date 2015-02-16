package edu.rosehulman.rosecareerfair;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText mUsernameText;
	private EditText mPasswordText;
	private Button mLoginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUsernameText = (EditText)findViewById(R.id.user_name);
		mPasswordText = (EditText)findViewById(R.id.password);
		mLoginButton = (Button)findViewById(R.id.sign_in_button);
		
		
	}
	private class LoginListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			
		}
		
	}
}
