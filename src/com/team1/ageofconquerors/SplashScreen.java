package com.team1.ageofconquerors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
/**
 * Splash screen for AgeOfConquerors Project
 */

public class SplashScreen extends Activity 
{
 
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 4000; //4 seconds
    private Handler myhandler;
 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
               
        setContentView(R.layout.splash_screen);
     // Make sure the progress bar is visible
        setProgressBarVisibility(true);
        myhandler = new Handler();
 
        // run a thread to start the home screen
        myhandler.postDelayed(new Runnable() {
 
            @Override
            public void run() 
            {
 
               finish();
                
               if (!mIsBackButtonPressed)
               {
                    // start the Login activity 
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    SplashScreen.this.startActivity(intent);
               }
                 
            }
 
        }, SPLASH_DURATION); 
    }
    
   
    //handle back button press
    @Override
    public void onBackPressed() 
    {
        mIsBackButtonPressed = true;
        super.onBackPressed();
    }
   
}

