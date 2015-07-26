package com.team1.ageofconquerors.units;

public class Images {

	// private variables
	int id;
	String name;
	String unit;

	// Empty constructor
	public Images() {

	}

	// constructor
	public Images(int keyId, String name, String unit) {
	this.id = keyId;
	this.name = name;
	this.unit = unit;

	}

	// constructor
	public Images(String name, String unit) {
	this.name = name;
	this.unit = unit;
	}

	// getting ID
	public int getID() {
	return this.id;
	}

	// setting id
	public void setID(int keyId) {
	this.id = keyId;
	}

	// getting name
	public String getName() {
	return this.name;
	}

	// setting name
	public void setName(String name) {
	this.name = name;
	}

	// getting unit type of the given name of the building
	public String getUnit() {
	return this.unit;
	}

	// setting unit type
	public void setUnit(String unit) {
	this.unit = unit;
	
	}
}


	
