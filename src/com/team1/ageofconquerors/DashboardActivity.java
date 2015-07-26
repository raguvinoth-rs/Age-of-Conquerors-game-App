package com.team1.ageofconquerors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team1.ageofconquerors.library.UserFunctions;

public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnLogout,btnContinue;
	TextView username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        /**
         * Dashboard Screen for the application
         * */        
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	setContentView(R.layout.dashboard);
        	
        	username = (TextView) findViewById(R.id.displayname);
            if (getIntent().getExtras() != null) {
    			username.setText(getIntent().getExtras().getString("username"));
    		}
        	btnLogout = (Button) findViewById(R.id.btnLogout);
        	btnContinue= (Button) findViewById(R.id.btnContinue);
        	
        	btnContinue.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				
    				// start the Game activity 
    	            Intent intent = new Intent(DashboardActivity.this, GameActivity.class);
    	            intent.putExtra("username", String.valueOf(username.getText()));
    	            DashboardActivity.this.startActivity(intent);
    	            // Not finishing as we are saving game state
    	            //finish();
    			}
    		});
        	
        	
        	
        	btnLogout.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				
    				AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

    			    builder.setTitle("LogOut");
    			    builder.setMessage("Are you sure to logout?");

    			    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

    			        public void onClick(DialogInterface dialog, int which) {
    			            // Do nothing but close the dialog
    			        	userFunctions.logoutUser(getApplicationContext());
    			            dialog.dismiss();
    			            Intent login = new Intent(DashboardActivity.this, LoginActivity.class);
    			            DashboardActivity.this.startActivity(login);
    			            finish();
    			        }

    			    });

    			    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

    			        @Override
    			        public void onClick(DialogInterface dialog, int which) {
    			            // Do nothing
    			            dialog.dismiss();
    			        }
    			    });

    			    AlertDialog alert = builder.create();
    			    alert.show();
    				
    			}
    		});
        	
        }else{
        	// user is not logged in show login screen
        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(login);
        	// Closing dashboard screen
        	finish();
        }
        
        
        
        
    }
}