package com.team1.ageofconquerors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.polidea.view.ZoomView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team1.ageofconquerors.MyService;
import com.team1.ageofconquerors.db.ImageDataBaseHelper;
import com.team1.ageofconquerors.units.Barrack;
import com.team1.ageofconquerors.units.Castle;
import com.team1.ageofconquerors.units.Workshop;
import com.team1.ageofconquerors.units.Images;
import com.team1.ageofconquerors.units.ObjectCreator;
import com.team1.ageofconquerors.units.TownCenter;
import com.team1.ageofconquerors.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
@SuppressLint("NewApi")
public class GameActivity extends Activity {
	// do something here
	private ZoomView zoomView;
	Button unitMenuCloseButton, createVillager, KnightButton, SwordButton, TrebucheButton;
	public ImageView iv;
	TextView username;
	GridView gridView;
	public TownCenter tc;
	public Castle castle;
	public Barrack barracks;
	public Workshop workshop;
	View viewTc, viewCastle, viewBarracks, viewWorkshop,viewGold;
//	View viewCastle;
	Map<String, Integer> liveUnits = new HashMap<String, Integer>();
	Map<String, Integer> resources = new HashMap<String, Integer>();
	FrameLayout villagerFrameLayout,swordsmanFrameLayout, TrebucheFrameLayout, knightFrameLayout;
	public RelativeLayout mapView;
	public GameActivity self = this;
	public ImageView buttonBuild;
	public RelativeLayout buildPopUp, unitMenu;
	public ImageButton crossBuildPopup;
	public Button crossUnitMenu;
	public FrameLayout townCenter, castleFrame, barracksFrame, workshopFrame;
	TextView viewTcTextView, viewCastleTextView, viewBarracksTextView, viewWorkshopTextView, unitName, attack, range, villager_icon_text, villagerText, SwordText, KnightText, TrebucheText;
	int count_tc = 0, count_castle = 0, count_barracks = 0, count_workshops = 0, villager_count;

	ImageView iconUnitMenu;
	static int goldm,stonem,woodm,foodm;

	public boolean toggleBuildPopup = false, toggleUnitMenu = false;
	/**
	 * Hold a reference to the current animator, so that it can be canceled
	 * mid-way.
	 */
	private Animator mCurrentAnimator;

	/**
	 * The system "short" animation time duration, in milliseconds. This
	 * duration is ideal for subtle animations or animations that occur very
	 * frequently.
	 */
	private int mShortAnimationDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// this is the main activity
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		startService();
		
		username = (TextView) findViewById(R.id.username);
		if (getIntent().getExtras() != null) {
			username.setText(getIntent().getExtras().getString("username"));
		}

		buttonBuild = (ImageView) findViewById(R.id.buttonBuild);

		// buttonBuild.setOnClickListener(buttonBuildClickListener);

		// townCenter = (FrameLayout) findViewById(R.id.tc);
		// townCenter.setOnClickListener(townCenterClickListener);

		gridView = (GridView) findViewById(R.id.gridView1);
		initializeResources();
		
		buttonBuild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ImageDataBaseHelper db = new ImageDataBaseHelper(
						GameActivity.this);
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(GameActivity.this);
				if (!prefs.getBoolean("firstTime", false)) {
					// run your one time code
					// insert images
					Log.d("Insert: ", "Inserting ..");
					db.addimages(new Images(1, "castle", "B"));
					db.addimages(new Images(2, "tc", "B"));
					db.addimages(new Images(3, "barracks", "B"));
					db.addimages(new Images(4, "workshop", "B"));
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("firstTime", true);
					editor.commit();
				}

				gridView = (GridView) findViewById(R.id.gridView1);

				if (gridView.getVisibility() == View.VISIBLE) {
					gridView.setVisibility(View.INVISIBLE);
				} else {
					gridView.setVisibility(View.VISIBLE);
					// Read images based on unit type
					ArrayList<Images> imageArry = new ArrayList<Images>();
					List<Images> images = db.getImagesByUnit("B");
					int i = 0;
					int size = images.size();
					String[] MOBILE_OS = new String[size];
					for (Images cn : images) {
						String temp = cn.getName().toLowerCase();
						System.out.println("" + temp);
						MOBILE_OS[i] = temp;
						i++;
					}

					gridView.setAdapter(new ImageAdapter(GameActivity.this,
							MOBILE_OS));

					gridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
						/*	Toast.makeText(
									getApplicationContext(),
									((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT)
									.show();
						*/	String text = ((TextView) v
									.findViewById(R.id.grid_item_label))
									.getText().toString();
						
							if (text.equalsIgnoreCase("castle")) {
								Toast.makeText(
										getApplicationContext(),
									     "castle will be created in 6 sec", Toast.LENGTH_SHORT)
										.show();
								// Create the town center
								final Handler handlerb = new Handler();
								handlerb.postDelayed(new Runnable() {
								
								    @Override	    
								    public void run() {	
								// Create the castle
								createCastle();
								// Initialize the resources
								// Set Resources for the first time
								resources.put("wood", resources.get("wood") - castle.cost_wood);
								resources.put("food", resources.get("food") - castle.cost_food);
								resources.put("gold", resources.get("gold") - castle.cost_gold);
								resources.put("stone", resources.get("stone") - castle.cost_stone);
								updateResources(
									resources.get("wood"),
									resources.get("food"),
									resources.get("gold"),
									resources.get("stone")
								);

								Toast.makeText(
										getApplicationContext(),
									     "Castle created", Toast.LENGTH_SHORT)
										.show();
							// The onClick listener for the Castle, we open unit
							// menu when clicked on the Castle
							if(viewCastle != null){
								viewCastle.setOnClickListener(new OnClickListener() {
	
									@Override
									public void onClick(View v) {
										zoomImageFromThumb(viewCastle,
												R.drawable.unit_menu);
										unitMenu = (RelativeLayout) findViewById(R.id.unitMenu); // finding the respective layouts/views of the unit menu
										iconUnitMenu = (ImageView) findViewById(R.id.iconUnitMenu);
										unitName = (TextView) findViewById(R.id.unitName);
										attack = (TextView) findViewById(R.id.attack);
										range = (TextView) findViewById(R.id.range);
										if(KnightText != null)KnightText.setVisibility(View.INVISIBLE);
										if(KnightButton != null)KnightButton.setVisibility(View.INVISIBLE);
										if(createVillager != null)createVillager.setVisibility(View.INVISIBLE);
										if(villagerText != null)villagerText.setVisibility(View.INVISIBLE);
										if(SwordText != null)SwordText.setVisibility(View.INVISIBLE);
										if(SwordButton != null)SwordButton.setVisibility(View.INVISIBLE);
										if(TrebucheText != null)TrebucheText.setVisibility(View.INVISIBLE);
										if(TrebucheButton != null)TrebucheButton.setVisibility(View.INVISIBLE);
										iconUnitMenu.setBackgroundResource(R.drawable.icon_unit_menu_castle);
										unitName.setText("Castle");
										attack.setText(Integer.toString(castle.attack));
										range.setText(Integer.toString(castle.rangeMax));																	
	
									} // end of onClick() of
										// viewCastle.setOnClickListener
	
								}); // end of viewCastle.onCLickListener
							
						}
				    }
				}, 6000);
				
			}
							
							if (text.equalsIgnoreCase("barracks")) {
								Toast.makeText(
										getApplicationContext(),
									     "barracks will be created in 6 sec", Toast.LENGTH_SHORT)
										.show();
								// Create the town center
								final Handler handlerb = new Handler();
								handlerb.postDelayed(new Runnable() {
								
								    @Override	    
								    public void run() {
								// Create the castle
								createBarracks();
								// Initialize the resources
								// Set Resources for the first time
								resources.put("wood", resources.get("wood") - barracks.cost_wood);
								resources.put("food", resources.get("food") - barracks.cost_food);
								resources.put("gold", resources.get("gold") - barracks.cost_gold);
								resources.put("stone", resources.get("stone") - barracks.cost_stone);
								updateResources(
									resources.get("wood"),
									resources.get("food"),
									resources.get("gold"),
									resources.get("stone")
								);
								Toast.makeText(
										getApplicationContext(),
									     "barracks created", Toast.LENGTH_SHORT)
										.show();

							
							if(viewBarracks != null){
								viewBarracks.setOnClickListener(new OnClickListener() {
	
									@Override
									public void onClick(View v) {
										zoomImageFromThumb(viewBarracks,
												R.drawable.unit_menu);
										unitMenu = (RelativeLayout) findViewById(R.id.unitMenu); // finding the respective layouts/views of the unit menu
										iconUnitMenu = (ImageView) findViewById(R.id.iconUnitMenu);
										unitName = (TextView) findViewById(R.id.unitName);
										attack = (TextView) findViewById(R.id.attack);
										range = (TextView) findViewById(R.id.range);
										
										SwordText = (TextView) findViewById(R.id.SwordText);
										SwordButton = (Button) findViewById(R.id.SwordButton);
										SwordText.setVisibility(View.VISIBLE);
										SwordButton.setVisibility(View.VISIBLE);
										//createVillager.setLayoutParams (new LayoutParams(40, LayoutParams.WRAP_CONTENT));
										SwordButton.setBackgroundResource(R.drawable.icon_swords_sb);
										SwordText.setText("Swordsmen");
										
										KnightText = (TextView) findViewById(R.id.KnightText);
										KnightText.setVisibility(View.VISIBLE);
										KnightButton = (Button) findViewById(R.id.KnightButton);
										KnightButton.setVisibility(View.VISIBLE);
										KnightButton.setBackgroundResource(R.drawable.icon_knight_sb);
										KnightText.setText("Knights");
										iconUnitMenu.setBackgroundResource(R.drawable.icon_unit_menu_barracks);
										unitName.setText("Barracks");
										attack.setText(Integer.toString(barracks.attack));
										range.setText(Integer.toString(barracks.rangeMax));	
										
										// Now, inside the unit menu, we have a
										// swordsman icon. onClick listener for that:
										SwordButton.setOnClickListener(new OnClickListener() {
	
													@Override
													public void onClick(View v) {
														// We find the Villager icon's FramyLayout View first
														SwordButton = (Button) findViewById(R.id.SwordButton); 
														// This got initiated twice
														 swordsmanFrameLayout = (FrameLayout)findViewById(R.id.layout_swords1);
														int swordsmanCount = searchFor("soldier"); // Declare an var to fetch current count of villagers from the HM
														Log.i("VC",	Integer.toString(swordsmanCount));
														// Create an inputStream with XML doc in it and pass it on to the Object Creator. 
														// This is the first time when I created the ObjectCreator class.
														// We will use this class hence forth when we create any object for this game.
														AssetManager assetManager = getAssets();
														InputStream in = null;
														try {
															in = assetManager
																	.open("civilizations.xml");
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// TODO Auto-generated method stub
														ObjectCreator oc = new ObjectCreator(
																"soldier",
																"Spanish", in);
														resources.put("wood", resources.get("wood") - oc.s.cost_wood);
														resources.put("food", resources.get("food") - oc.s.cost_food);
														resources.put("gold", resources.get("gold") - oc.s.cost_gold);
														resources.put("stone", resources.get("stone") - oc.s.cost_stone);
														updateResources(
															resources.get("wood"),
															resources.get("food"),
															resources.get("gold"),
															resources.get("stone")
														);
														// Now close the input stream so it could be opened again from start.
														try {
															in.close();
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// If the searchFor() function returns 0 that means no object called "villager" was NOT
														// created before, thus, we create the first entry:
														if (swordsmanCount == 0) {
															liveUnits.put(
																	"soldier", 1);
														} else { 
															// If it does return something, then we simply increment the count.
															swordsmanCount++;
															liveUnits.put(
																	"soldier",
																	swordsmanCount);
														}
														// Find and set the count icon of villager in the map to the total number of villagers.
														 TextView swordsman_icon_text = (TextView) findViewById(R.id.swords_icon_text);
														// We again call the searchFor() function to get the most updated value.
														swordsman_icon_text.setText(Integer.toString(searchFor("soldier")));
														Log.i("VFL", "just before this statement");
														swordsmanFrameLayout.setVisibility(View.VISIBLE);// Making the villager icon visible the following is a hashMap created 
																										// in the TownCenter.java class, not sure if I would use it.
														barracks.BarrackMap.put("soldier", Integer.toString((oc.s.count_soldier)));
	
													} // end of onClick() of
														// createSwordsman.onCLickListener
	
												}); // end of Swordsman
													// onCLickListener		
	
										// Now, inside the unit menu, we have a
										// Knight icon. onClick listener for that:
										KnightButton.setOnClickListener(new OnClickListener() {
	
													@Override
													public void onClick(View v) {
														// We find the Knight icon's FramyLayout View first
														KnightButton = (Button) findViewById(R.id.KnightButton); 
														// This got initiated twice
														 knightFrameLayout = (FrameLayout)findViewById(R.id.layout_knight1);
														int knightCount = searchFor("knight"); // Declare an var to fetch current count of villagers from the HM
														Log.i("VC",	Integer.toString(knightCount));
														// Create an inputStream with XML doc in it and pass it on to the Object Creator. 
														// This is the first time when I created the ObjectCreator class.
														// We will use this class hence forth when we create any object for this game.
														AssetManager assetManager = getAssets();
														InputStream in = null;
														try {
															in = assetManager
																	.open("civilizations.xml");
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// TODO Auto-generated method stub
														ObjectCreator oc = new ObjectCreator(
																"knight",
																"Spanish", in);
														resources.put("wood", resources.get("wood") - oc.k.cost_wood);
														resources.put("food", resources.get("food") - oc.k.cost_food);
														resources.put("gold", resources.get("gold") - oc.k.cost_gold);
														resources.put("stone", resources.get("stone") - oc.k.cost_stone);
														updateResources(
															resources.get("wood"),
															resources.get("food"),
															resources.get("gold"),
															resources.get("stone")
														);
														// Now close the input stream so it could be opened again from start.
														try {
															in.close();
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// If the searchFor() function returns 0 that means no object called "villager" was NOT
														// created before, thus, we create the first entry:
														if (knightCount == 0) {
															liveUnits.put(
																	"knight", 1);
														} else { 
															// If it does return something, then we simply increment the count.
															knightCount++;
															liveUnits.put(
																	"knight",
																	knightCount);
														}
														// Find and set the count icon of villager in the map to the total number of villagers.
														 TextView knight_icon_text = (TextView) findViewById(R.id.knight_icon_text);
														// We again call the searchFor() function to get the most updated value.
														knight_icon_text.setText(Integer.toString(searchFor("knight")));
														Log.i("VFL", "just before this statement");
														knightFrameLayout.setVisibility(View.VISIBLE);// Making the villager icon visible the following is a hashMap created 
																										// in the TownCenter.java class, not sure if I would use it.
														barracks.BarrackMap.put("knight", Integer.toString((oc.k.count_knight)));
	
													} // end of onClick() of
														// createSwordsman.onCLickListener
	
												}); // end of Swordsman
													// onCLickListener		
	
										
										
									} // end of onClick() of
										// viewBarracks.setOnClickListener
	
								}); // end of viewBarracks.onCLickListener
							}
								    }
								}, 6000);
								
							}	
							
							if (text.equalsIgnoreCase("workshop")) {
								Toast.makeText(
										getApplicationContext(),
									     "Workshop will be created in 6 sec", Toast.LENGTH_SHORT)
										.show();
								// Create the town center
								final Handler handlerb = new Handler();
								handlerb.postDelayed(new Runnable() {
								
								    @Override	    
								    public void run() {
								// Create the castle
								createWorkshop();
								// Initialize the resources
								// Set Resources for the first time
								resources.put("wood", resources.get("wood") - workshop.cost_wood);
								resources.put("food", resources.get("food") - workshop.cost_food);
								resources.put("gold", resources.get("gold") - workshop.cost_gold);
								resources.put("stone", resources.get("stone") - workshop.cost_stone);
								updateResources(
									resources.get("wood"),
									resources.get("food"),
									resources.get("gold"),
									resources.get("stone")
								);
								Toast.makeText(
										getApplicationContext(),
									     "Workshop created", Toast.LENGTH_SHORT)
										.show();

							
							if(viewWorkshop != null){
								viewWorkshop.setOnClickListener(new OnClickListener() {
	
									@Override
									public void onClick(View v) {
										zoomImageFromThumb(viewWorkshop,
												R.drawable.unit_menu);
										unitMenu = (RelativeLayout) findViewById(R.id.unitMenu); // finding the respective layouts/views of the unit menu
										iconUnitMenu = (ImageView) findViewById(R.id.iconUnitMenu);
										unitName = (TextView) findViewById(R.id.unitName);
										attack = (TextView) findViewById(R.id.attack);
										range = (TextView) findViewById(R.id.range);
										if(KnightText != null)KnightText.setVisibility(View.INVISIBLE);
										if(KnightButton != null)KnightButton.setVisibility(View.INVISIBLE);
										if(createVillager != null)createVillager.setVisibility(View.INVISIBLE);
										if(villagerText != null)villagerText.setVisibility(View.INVISIBLE);
										if(SwordText != null)SwordText.setVisibility(View.INVISIBLE);
										if(SwordButton != null)SwordButton.setVisibility(View.INVISIBLE);
										TrebucheText = (TextView) findViewById(R.id.TrebucheText);
										TrebucheButton = (Button) findViewById(R.id.TrebucheButton);
										TrebucheText.setVisibility(View.VISIBLE);
										TrebucheButton.setVisibility(View.VISIBLE);
										TrebucheButton.setBackgroundResource(R.drawable.icon_trebuche_sb);
										TrebucheText.setText("Trebuche");
										iconUnitMenu.setBackgroundResource(R.drawable.workshop);
										unitName.setText("Workshops");
										attack.setText(Integer.toString(workshop.attack));
										range.setText(Integer.toString(workshop.rangeMax));			
										
										TrebucheButton.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// We find the Villager icon's FramyLayout View first
												TrebucheButton = (Button) findViewById(R.id.TrebucheButton); 
												// This got initiated twice
												TrebucheFrameLayout = (FrameLayout)findViewById(R.id.layout_trebuche1);
												int trebucheCount = searchFor("trebuche"); // Declare an var to fetch current count of villagers from the HM
												Log.i("TrC",	Integer.toString(trebucheCount));
												// Create an inputStream with XML doc in it and pass it on to the Object Creator. 
												// This is the first time when I created the ObjectCreator class.
												// We will use this class hence forth when we create any object for this game.
												AssetManager assetManager = getAssets();
												InputStream in = null;
												try {
													in = assetManager
															.open("civilizations.xml");
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												// TODO Auto-generated method stub
												ObjectCreator oc = new ObjectCreator(
														"trebuche",
														"Spanish", in);
												resources.put("wood", resources.get("wood") - oc.t.cost_wood);
												resources.put("food", resources.get("food") - oc.t.cost_food);
												resources.put("gold", resources.get("gold") - oc.t.cost_gold);
												resources.put("stone", resources.get("stone") - oc.t.cost_stone);
												updateResources(
													resources.get("wood"),
													resources.get("food"),
													resources.get("gold"),
													resources.get("stone")
												);
												// Now close the input stream so it could be opened again from start.
												try {
													in.close();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												// If the searchFor() function returns 0 that means no object called "villager" was NOT
												// created before, thus, we create the first entry:
												if (trebucheCount == 0) {
													liveUnits.put(
															"trebuche", 1);
												} else { 
													// If it does return something, then we simply increment the count.
													trebucheCount++;
													liveUnits.put(
															"trebuche",
															trebucheCount);
												}
												// Find and set the count icon of villager in the map to the total number of villagers.
												 TextView trebuche_icon_text = (TextView) findViewById(R.id.trebuche_icon_text);
												// We again call the searchFor() function to get the most updated value.
												trebuche_icon_text.setText(Integer.toString(searchFor("trebuche")));
												Log.i("TFL", "just before this statement");
												TrebucheFrameLayout.setVisibility(View.VISIBLE);// Making the villager icon visible the following is a hashMap created 
																								// in the TownCenter.java class, not sure if I would use it.
												workshop.WorkshopMap.put("trebuche", Integer.toString((oc.t.count_trebuche)));

											} // end of onClick() of
												// createVillager.onCLickListener

										}); // end of createVillager
											// onCLickListener
	
									} // end of onClick() of
										// viewBarracks.setOnClickListener
	
								}); // end of viewBarracks.onCLickListener
							}
								    }
								}, 6000);
								
							}	



							
							if (text.equalsIgnoreCase("tc")) {
								Toast.makeText(
										getApplicationContext(),
									     "TC will be created in 6 sec", Toast.LENGTH_SHORT)
										.show();
								// Create the town center
								final Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
								
								    @Override	    
								    public void run() {
								        // Do something after 30s = 10000ms
								    	createTownCenter();
										// Create the first 3 villagers
										// Initialize the resources
										//initializeResources();
										// Set Resources for the first time
										resources.put("wood", resources.get("wood") - tc.cost_wood);
										resources.put("food", resources.get("food") - tc.cost_food);
										resources.put("gold", resources.get("gold") - tc.cost_gold);
										resources.put("stone", resources.get("stone") - tc.cost_stone);
										updateResources(
											resources.get("wood"),
											resources.get("food"),
											resources.get("gold"),
											resources.get("stone")
										);
										Toast.makeText(
												getApplicationContext(),
											     "TC created", Toast.LENGTH_SHORT)
												.show();
							
							// The onClick listener for the TC, we open unit
							// menu when clicked on the TC
							if(viewTc != null){
								viewTc.setOnClickListener(new OnClickListener() {
	
									@Override
									public void onClick(View v) {
										zoomImageFromThumb(viewTc,
												R.drawable.unit_menu);
										unitMenu = (RelativeLayout) findViewById(R.id.unitMenu); // finding the respective layouts/views of the unit menu
										iconUnitMenu = (ImageView) findViewById(R.id.iconUnitMenu);
										unitName = (TextView) findViewById(R.id.unitName);
										attack = (TextView) findViewById(R.id.attack);
										range = (TextView) findViewById(R.id.range);
										if(KnightText != null)KnightText.setVisibility(View.INVISIBLE);
										if(KnightButton != null)KnightButton.setVisibility(View.INVISIBLE);
										if(SwordText != null)SwordText.setVisibility(View.INVISIBLE);
										if(SwordButton != null)SwordButton.setVisibility(View.INVISIBLE);
										if(TrebucheText != null)TrebucheText.setVisibility(View.INVISIBLE);
										if(TrebucheButton != null)TrebucheButton.setVisibility(View.INVISIBLE);
										villagerText = (TextView) findViewById(R.id.villagerText);
										createVillager = (Button) findViewById(R.id.createVillager);
										createVillager.setVisibility(View.VISIBLE);
										villagerText.setVisibility(View.VISIBLE);
										iconUnitMenu.setBackgroundResource(R.drawable.icon_unit_menu_tc);
										createVillager.setBackgroundResource(R.drawable.icon_villager_menu);
										villagerText.setText("Town Center");
										unitName.setText("Town Center");
										attack.setText(Integer.toString(tc.attack));
										range.setText(Integer.toString(tc.rangeMax));
	
										// Now, inside the unit menu, we have a
										// villager icon. onClick listener for that:
										createVillager.setOnClickListener(new OnClickListener() {
	
													@Override
													public void onClick(View v) {
														// We find the Villager icon's FramyLayout View first
														createVillager = (Button) findViewById(R.id.createVillager); 
														// This got initiated twice
														 villagerFrameLayout = (FrameLayout)findViewById(R.id.layout_villager1);
														int villagerCount = searchFor("villager"); // Declare an var to fetch current count of villagers from the HM
														Log.i("VC",	Integer.toString(villagerCount));
														// Create an inputStream with XML doc in it and pass it on to the Object Creator. 
														// This is the first time when I created the ObjectCreator class.
														// We will use this class hence forth when we create any object for this game.
														AssetManager assetManager = getAssets();
														InputStream in = null;
														try {
															in = assetManager
																	.open("civilizations.xml");
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// TODO Auto-generated method stub
														ObjectCreator oc = new ObjectCreator(
																"villager",
																"Spanish", in);
														resources.put("wood", resources.get("wood") - oc.v.cost_wood);
														resources.put("food", resources.get("food") - oc.v.cost_food);
														resources.put("gold", resources.get("gold") - oc.v.cost_gold);
														resources.put("stone", resources.get("stone") - oc.v.cost_stone);
														updateResources(
															resources.get("wood"),
															resources.get("food"),
															resources.get("gold"),
															resources.get("stone")
														);
														// Now close the input stream so it could be opened again from start.
														try {
															in.close();
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														// If the searchFor() function returns 0 that means no object called "villager" was NOT
														// created before, thus, we create the first entry:
														if (villagerCount == 0) {
															liveUnits.put(
																	"villager", 1);
														} else { 
															// If it does return something, then we simply increment the count.
															villagerCount++;
															liveUnits.put(
																	"villager",
																	villagerCount);
														}
														
														if (villagerCount == 2){
													     viewGold = findViewById(R.id.goldmine);
													     
													    	 viewGold.setVisibility(View.VISIBLE);
													         goldm = 100;
														}
														// Find and set the count icon of villager in the map to the total number of villagers.
														 TextView villager_icon_text = (TextView) findViewById(R.id.villager_icon_text);
														// We again call the searchFor() function to get the most updated value.
														villager_icon_text.setText(Integer.toString(searchFor("villager")));
														Log.i("VFL", "just before this statement");
														villagerFrameLayout.setVisibility(View.VISIBLE);// Making the villager icon visible the following is a hashMap created 
																										// in the TownCenter.java class, not sure if I would use it.
														tc.TC.put("villager", Integer.toString((oc.v.count_villager)));
	
													} // end of onClick() of
														// createVillager.onCLickListener
	
												}); // end of createVillager
													// onCLickListener
	
									} // end of onClick() of
										// viewTc.setOnClickListener
	
								}); // end of viewTc.onCLickListener
							}
								    }
								}, 6000);
							
							}	
							

							
	

						} // end of onClick() gridView.setOnItemClickListener

					}); // end of gridView.setOnItemClickListener

				} // if gridView is not present

			} // end of onClick() of buttonBuild.setOnClickListener

		}); // end of buttonBuild.setOnClickListener

	} // end of onCreate()

	
	

	//Service in Background
	 // Start the service
	 public void startService() {
	  startService(new Intent(this, MyService.class));
	 }

	  // Stop the service
	 public void stopService() {
	  stopService(new Intent(this, MyService.class));
	 }
	
	//End Service
		
	
	// This function is called everytime game resources are used, basically,
	// everytime one needs to update resources.
	public void updateResources(int wood, int food, int gold, int stone) {
		TextView w, f, g, s;
		w = (TextView) findViewById(R.id.wood);
		f = (TextView) findViewById(R.id.food);
		g = (TextView) findViewById(R.id.gold);
		s = (TextView) findViewById(R.id.stone);
		w.setText(Integer.toString(wood));
		f.setText(Integer.toString(food));
		g.setText(Integer.toString(gold));
		s.setText(Integer.toString(stone));
	}

	// This function is called once when the game starts, however, we may call
	// it again if the user wishes to build one more.
	public void createTownCenter() {

		AssetManager assetManager = getAssets();
		InputStream in = null;
		try {
			in = assetManager.open("civilizations.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tc = new TownCenter("Spanish", in, "");
		if (tc != null) {// If TC is created, make the view containing it
							// visible, make the count icon text = 1
			viewTc = findViewById(R.id.tc);
			TextView viewTcTextView = (TextView) findViewById(R.id.tc_icon_text);
			viewTc.setVisibility(View.VISIBLE);
			count_tc++;
			viewTcTextView.setText(Integer.toString(count_tc));
		}
		// Closing the input stream
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createCastle() {

		AssetManager assetManager = getAssets();
		InputStream in = null;
		try {
			in = assetManager.open("civilizations.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		castle = new Castle("Spanish", in, "");
		if (castle != null) {// If TC is created, make the view containing it
							// visible, make the count icon text = 1
			viewCastle = findViewById(R.id.castle);
			TextView viewCastleTextView = (TextView) findViewById(R.id.castle_icon_text);
			viewCastle.setVisibility(View.VISIBLE);
			count_castle++;
			viewCastleTextView.setText(Integer.toString(count_castle));
		}
		// Closing the input stream
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createBarracks() {

		AssetManager assetManager = getAssets();
		InputStream in = null;
		try {
			in = assetManager.open("civilizations.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		barracks = new Barrack("Spanish", in, "");
		if (barracks != null) {// If TC is created, make the view containing it
							// visible, make the count icon text = 1
			viewBarracks = findViewById(R.id.barracks);
			TextView viewBarracksTextView = (TextView) findViewById(R.id.barracks_icon_text);
			viewBarracks.setVisibility(View.VISIBLE);
			count_barracks++;
			viewBarracksTextView.setText(Integer.toString(count_barracks));
		}
		// Closing the input stream
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createWorkshop() {

		AssetManager assetManager = getAssets();
		InputStream in = null;
		try {
			in = assetManager.open("civilizations.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		workshop = new Workshop("Spanish", in, "");
		if (workshop != null) {// If TC is created, make the view containing it
							// visible, make the count icon text = 1
			viewWorkshop = findViewById(R.id.workshop);
			TextView viewWorkshopTextView = (TextView) findViewById(R.id.workshop_icon_text);
			viewWorkshop.setVisibility(View.VISIBLE);
			count_workshops++;
			viewWorkshopTextView.setText(Integer.toString(count_workshops));
		}
		// Closing the input stream
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	// This method will basically search: IF a particular unit created exists or
	// not?
	public int searchFor(String attributeName) {
		int count = 0;
		if (liveUnits.get(attributeName) != null) {
			count = liveUnits.get(attributeName);
		}
		Log.i("SFC", Integer.toString(count));
		return count;
	}

	// This function is called once when the game starts. It basically allocates
	// basic resources. These could change later in the game settings.
	public void initializeResources() {
		resources.put("wood", 900);
		resources.put("food", 900);
		resources.put("gold", 900);
		resources.put("stone", 900);
		updateResources(
			resources.get("wood"),
			resources.get("food"),
			resources.get("gold"),
			resources.get("stone")
		);
	}


	
	
	
	// source: http://developer.android.com/training/animation/zoom.html
	// zooming an image once it is clicked on
	private void zoomImageFromThumb(final View thumbView, int imageResId) {
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		final RelativeLayout expandedImageView = (RelativeLayout) findViewById(R.id.unitMenu);
		expandedImageView.setBackgroundResource(imageResId);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		findViewById(R.id.maincontainer).getGlobalVisibleRect(finalBounds,
				globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their
				// original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								startBounds.top))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}

	public OnClickListener buttonBuildClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mapView = (RelativeLayout) findViewById(R.id.map);
		}
	};

	public OnClickListener crossBuildPopupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (toggleBuildPopup == true) {
				buildPopUp.setVisibility(View.INVISIBLE);
				toggleBuildPopup = false;
			}

		}
	};

	public OnClickListener crossUnitMenuClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (toggleUnitMenu == true) {
				unitMenu.setVisibility(View.INVISIBLE);
				toggleUnitMenu = false;
			}

		}
	};

	
	  public void onBackPressed() {
          Log.d("Exit", "I exit the game"); 
          
          
			AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

		    builder.setTitle("Quit");
		    builder.setMessage("Are you sure to Quit Game?");

		    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing but close the dialog
		        	stopService();
		            dialog.dismiss();
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
          
          return;
      } 
}