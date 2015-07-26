package com.team1.ageofconquerors.units;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class Retriever {
	Map<String,String> XMLfetch = new HashMap<String,String>(20);
	
	boolean inPassedTag = false;
	boolean PassedTag = false;
	String civilization = "";
	boolean inCiv = false;
	int civId=0, id=0;
	public String nodeType;
	public String tagName;
	public String bname;
	String tcTag;
	public int cost_food = 0;
	public int cost_wood = 0;
	public int cost_stone = 0;
	public int cost_gold = 0;
	
	public Retriever(String civName, InputStream in, String tagName, String nodeType, String building) {
		bname = building;
		InputStream is = in;
		this.nodeType = nodeType;
		this.tagName = tagName;
		Log.i("team1", "Just inside Retrieve "+nodeType);
		try {
			Log.i(this.nodeType, "Inside Try ");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		
		DefaultHandler handler = new DefaultHandler() {
		public void startElement(String uri, String localName,String qName, 
	                Attributes attributes) throws SAXException {
			//og.i(localLogTag, "Inside startElement "+qName);
		
			if(qName.equalsIgnoreCase("unit"))
			{
				String civName = attributes.getValue("name");
				if(civName.equals("villager") && bname.equals("tc")){
					
				//Log.i(localLogTag, "inside EqualsIgnoreCase(buildG) "+qName);
				inPassedTag = true;
				PassedTag = true;
				//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
				id = Integer.parseInt(attributes.getValue("id"));
				if(id==1)
				{
					//Log.i(localLogTag, "Inside if(id==5) ");
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
				if((civName.equals("soldier") || civName.equals("knight") ) && bname.equals("barrack")){
					
					//Log.i(localLogTag, "inside EqualsIgnoreCase(buildG) "+qName);
					inPassedTag = true;
					PassedTag = true;
					//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
					id = Integer.parseInt(attributes.getValue("id"));
					if(id==1)
					{
						//Log.i(localLogTag, "Inside if(id==5) ");
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
				
				if(civName.equals("trebuche") && bname.equals("workshop")){
					
				//Log.i(localLogTag, "inside EqualsIgnoreCase(buildG) "+qName);
				inPassedTag = true;
				PassedTag = true;
				//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
				id = Integer.parseInt(attributes.getValue("id"));
				if(id==1)
				{
					//Log.i(localLogTag, "Inside if(id==5) ");
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
				}	
			
			
			
			//Log.i(localLogTag, "before going to cost node ");
			if(inPassedTag && id==1){
				//Log.i(localLogTag, "before going to cost node");
				if(qName.equalsIgnoreCase("cost")){

					//Log.i(localLogTag, "Inside COST node "+tcTag);
					//Log.i(localLogTag, "Wood "+attributes.getValue("wood"));
					//Log.i(localLogTag, "Stone "+attributes.getValue("stone"));
					//Log.i(localLogTag, "Food "+ attributes.getValue("food"));
					//Log.i(localLogTag, "Gold "+attributes.getValue("gold"));
					XMLfetch.put("costWood", attributes.getValue("wood"));
					XMLfetch.put("costStone", attributes.getValue("stone"));
					XMLfetch.put("costFood", attributes.getValue("food"));
					XMLfetch.put("costGold", attributes.getValue("gold"));
				}				
			}
		}
		
		// END ELEMENT here		
				@Override
			     public void endElement(String uri, String localName, String qName) throws SAXException {

					if(inPassedTag)
					{
						if(qName.equalsIgnoreCase("PassedTag")) {
							  PassedTag=false;
							  inPassedTag=false;
							  }
						if(qName.equalsIgnoreCase("cost")){
							inPassedTag=false;
						}
					}
					
			     }
				 
				// READ DATA here
				 @Override
				  public void characters(char[] ch, int start, int length) throws SAXException {
				 	}
		
	     };
	     
	    // Log.i(localLogTag, "Now, going to parse the doc");
	     //InputStream xmlFile = context.getApplicationContext().getAssets().open("civilizations.xml");
	     
	     saxParser.parse(is, handler);
	     //Log.i(localLogTag, "after parsing");
	     } catch (Exception e) {
	       e.printStackTrace();
	       //Log.i(localLogTag, e.toString());
	     }	 
	   }

}
