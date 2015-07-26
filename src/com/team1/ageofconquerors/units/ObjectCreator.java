package com.team1.ageofconquerors.units;

import java.io.InputStream;



public class ObjectCreator{
	
	//public Villager v;
	//public ObjectCreator(String objectName, String civ, InputStream in){
		
	//}
	
	public Villager v;
	public Soldier s;
	public Knight k;
	public Trebuche t;
	public enum switchAt {
        townCenter,
        villager,
        soldier,
        knight,
        trebuche
    }
	
	//switchAt type = switchAt.villager;

public ObjectCreator(String objectName, String civ, InputStream in){
	if(objectName.equals("villager")){
		v = new Villager("Spanish", in, "unit");
		v.count_villager++;
	}
	else if(objectName.equals("soldier")){
	
		s = new Soldier("Spanish", in, "unit");
		s.count_soldier++;
	}
	else if(objectName.equals("knight")){
		
		k = new Knight("Spanish", in, "unit");
		k.count_knight++;
	}
	else if(objectName.equals("trebuche")){
		
		t = new Trebuche("Spanish", in, "unit");
		t.count_trebuche++;
	}
	else if(objectName.equals("towncenter")){
		}
	
	}
	
}
	
	

