package com.team1.ageofconquerors.units;

public class Unit {
	// Unit basic parameters
	public String name; // Villager, etc.
	public int id; // Unit ID;
	public String civilization; // Spanish, etc.
	public String owner; // Player username
	public int hitpoints; // Health
	public String unitType; // Infantry, Cavalry, archer, building, etc. - To
							// calculate attack bonuses

	// Advanced common parameters(Common between military and non-military)
	public int movementSpeed; // Movement speed of a unit
	public int buildingSpeed; // For villagers - the building speed
	public int creationSpeed; // Creation of villagers, military units, etc.

	// Unit military parameters
	public boolean isMilitary; // Bool value to determine if unit is military
								// unit
	public int attack; // Attack power
	public int attackDelay; // Time between each attack
	public boolean attackAoE; // true of damage is in an 'area of effect' (AoE)
	public int rangeMin; // Minimum range of attack (0 for no minimum range,
							// e.g. Throwing Axeman)
	public int rangeMax; // Maximum range of attack
	public int defenseMelee; // Normal Defense
	public int defensePiercing; // Piercing Defense
	public boolean isMounted; // Riding on something or not
	public boolean isStatic; // Moves?
	public boolean isEnemy; // Determine enemy Vs non-enemy units
	public boolean canGarrison; // Garrisonable or not

	// Unit current status
	public boolean isIdle = true; // You know why
	public boolean isGarrisoned = false; // Is the current unit garrisoned or
											// not
	public boolean isUnderAttack = false; // To determine if unit is currently
											// under attack
	public boolean isAttacking = false; // To determine if unit is currently
										// attacking

}
