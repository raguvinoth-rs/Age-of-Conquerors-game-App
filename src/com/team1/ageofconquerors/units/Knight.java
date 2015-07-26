package com.team1.ageofconquerors.units;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.team1.ageofconquerors.units.Retriever;
import android.util.Log;

public class Knight extends Unit {
	
	Map<String,String> XMLfetch = new HashMap<String,String>(20);
	int id=0;
	public int cost_food = 0;
	public int cost_wood = 0;
	public int cost_stone = 0;
	public int cost_gold = 0;
	public int count_knight=0;
	
	public Knight(String civName, InputStream in, String tag) {
		// TODO Auto-generated constructor stub
		Retriever retrieveIt = new Retriever("Spanish", in, "unit", "knight","barrack");
		XMLfetch = retrieveIt.XMLfetch;		
		name = XMLfetch.get("name");
		Log.i("null var", (XMLfetch.get("id"))+" ");
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
			
	}
}

