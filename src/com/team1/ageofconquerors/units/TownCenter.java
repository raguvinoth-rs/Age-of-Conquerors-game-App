package com.team1.ageofconquerors.units;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class TownCenter extends Unit {	
	Map<String,String> XMLfetch = new HashMap<String,String>(20);
	boolean inBuilding = false;
	boolean building = false;
	String civilization = "";
	boolean inCiv = false;
	int civId=0, id=0;
	
	String tcTag;
	public int cost_food;
	public int cost_wood;
	public int cost_stone;
	public int cost_gold;
	//To count units
	public Map<String,String> TC = new HashMap<String,String>(20);
	
	public TownCenter(String civName, InputStream in, String tag) {
		// TODO Auto-generated constructor stub
		tcTag=tag;
		retrieve(civName, in, tag);
		
		name = XMLfetch.get("name");
		Log.i("null var", (XMLfetch.get("id"))+" "+tcTag);
		id = Integer.parseInt(XMLfetch.get("id"));
		hitpoints = Integer.parseInt(XMLfetch.get("hp"));
		attack = Integer.parseInt(XMLfetch.get("attack"));
		defenseMelee = Integer.parseInt(XMLfetch.get("armor"));
		defensePiercing = Integer.parseInt(XMLfetch.get("pierceArmor"));
		rangeMax = Integer.parseInt(XMLfetch.get("range"));
		rangeMin = Integer.parseInt(XMLfetch.get("rangeMin"));
		cost_food = Integer.parseInt(XMLfetch.get("costFood"));
		cost_wood = Integer.parseInt(XMLfetch.get("costWood"));
		cost_stone = Integer.parseInt(XMLfetch.get("costStone"));
		cost_gold = Integer.parseInt(XMLfetch.get("costGold"));
		XMLfetch.clear();
		/*
		hitpoints=Integer.parseInt(ArrayList[0]);
		attack = Integer.parseInt(ArrayList[1]);
		defenseMelee = Integer.parseInt(ArrayList[2]);
		defensePiercing = Integer.parseInt(ArrayList[3]);
		rangeMin = 0;
		rangeMax = Integer.parseInt(ArrayList[4]);*/
			
	}


	public void retrieve(String civName, InputStream in, String tag) {
		InputStream is = in;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		
			DefaultHandler handler = new DefaultHandler() {
			public void startElement(String uri, String localName,String qName, 
	                Attributes attributes) throws SAXException {
			Log.i("team1", "Inside startElement "+tcTag);
		
			if(qName.equalsIgnoreCase("building"))
			{
				Log.i("team1", "inside EqualsIgnoreCase(buildG) " +tcTag);
				inBuilding = true;
				building = true;
				//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
				id = Integer.parseInt(attributes.getValue("id"));
				if(id==5)
				{
					Log.i("team1", "Inside if(id==5) " +tcTag);
					XMLfetch.put("id", attributes.getValue("id"));
					XMLfetch.put("name", attributes.getValue("name"));
					XMLfetch.put("hp", attributes.getValue("hp"));
					XMLfetch.put("attack", attributes.getValue("attack"));
					XMLfetch.put("armor", attributes.getValue("armor"));
					XMLfetch.put("pierceArmor", attributes.getValue("pierceArmor"));
					XMLfetch.put("range", attributes.getValue("range"));
					XMLfetch.put("rangeMin", attributes.getValue("rangeMin"));
					XMLfetch.put("garrison", attributes.getValue("garrison"));
				}				
			}
			Log.i("team1", "before going to cost node "+tcTag);
			if(inBuilding && id==5){
				Log.i("team1", "before going to cost node");
				if(qName.equalsIgnoreCase("cost")){

					Log.i("team1", "Inside COST node "+tcTag);
					Log.i("resources", "Wood "+attributes.getValue("wood")+tcTag);
					Log.i("resources", "Stone "+attributes.getValue("stone")+tcTag);
					Log.i("resources", "Food "+ attributes.getValue("food")+tcTag);
					Log.i("resources", "Gold "+attributes.getValue("gold")+tcTag);
					XMLfetch.put("costWood", attributes.getValue("wood"));
					XMLfetch.put("costStone", attributes.getValue("stone"));
					XMLfetch.put("costFood", attributes.getValue("food"));
					XMLfetch.put("costGold", attributes.getValue("gold"));
				}
				Log.i("team1", "before going to Unit node");
				if(qName.equalsIgnoreCase("unit")){
					Log.i("team1", "Inside Unit Node");
					TC.put(attributes.getValue("name"),"0");
					
				}
			}
		}
		
		// END ELEMENT here		
				@Override
			     public void endElement(String uri, String localName, String qName) throws SAXException {

					if(inBuilding)
					{
						if(qName.equalsIgnoreCase("building")) {
							  building=false;
							  inBuilding=false;
							  }
						if(qName.equalsIgnoreCase("cost")){
							inBuilding=false;
						}
					}
					
			     }
				 
				// READ DATA here
				 @Override
				  public void characters(char[] ch, int start, int length) throws SAXException {
				 	}
		
	     };
	     
	     Log.i("team1", "Now, going to parse the doc");
	     //InputStream xmlFile = context.getApplicationContext().getAssets().open("civilizations.xml");
	     
	     saxParser.parse(is, handler);
	     Log.i("team1", "after parsing");
	     } catch (Exception e) {
	       e.printStackTrace();
	       Log.i("team1", e.toString());
	     }
		
	 
	   }
	
    
}
