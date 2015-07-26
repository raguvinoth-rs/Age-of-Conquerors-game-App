package com.team1.ageofconquerors;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
 public MyService() {
 }
 @Override
 public IBinder onBind(Intent intent) {
  throw new UnsupportedOperationException("Not yet implemented");
 }
 @Override
 public void onCreate() {
  Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
 }
 private boolean running;
  @Override
 public void onStart(Intent intent, int startId) {
  // Perform your long running operations here.
	// TODO Auto-generated method stub
	    super.onStart(intent, startId);
	    Toast.makeText(this, "Service running", Toast.LENGTH_SHORT).show();

	     running=true;
	    final Handler handler = new Handler(){

	        @Override
	        public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
	            super.handleMessage(msg);
	            Toast.makeText(MyService.this, "10 secs has passed", Toast.LENGTH_SHORT).show();
	        }

	    };



	    new Thread(new Runnable(){
	        public void run() {
	        // TODO Auto-generated method stub
	        while(running)
	        {
	           try {
	            Thread.sleep(10000);
	            handler.sendEmptyMessage(0);

	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 

	        }

	                        }
	    }).start();
	}

  @Override
 public void onDestroy() {
	  running=false;
     Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
 }

}