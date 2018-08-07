/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE( null, null ),
	
	GLADIATOR( "gladiatore", 
		"Un attacco riuscito con un'arma da corpo a corpo permette al _Gladiatore_ di iniziare una combo, " +
		"nella quale ogni attacco successivo infligge danno maggiore." ),
	BERSERKER( "berserker", 
		"Quando gravemente ferito, il _Berserker_ entra in uno stato di furia selvaggia " +
		"aumentando di molto l'attacco." ),
	
	WARLOCK( "stregone", 
		"Dopo aver ucciso un nemico lo _Stregone_ consuma la sua anima. " +
		"Curando le sue ferite e soddisfacendo la sua fame." ),
	BATTLEMAGE( "magoguerriero", 
		"Quando combatte con una bacchetta in mano, il _Magguerriero_ infligge altri danni addizionali che dipedendono " +
		"dal numero corrente di cariche. Ogni attacco mandato a segno restituisce 1 carica alla bacchetta." ),
	
	ASSASSIN( "assassino", 
		"Quando effettua un attacco a sorpresa,  l'_Assassino_ infligge danni addizionali al bersaglio." ),
	FREERUNNER( "freerunner", 
		"Il _Freerunner_ puo' muoversi almeno il doppio piu' veloce, che la maggiorparte dei mostri. Quando " +
		"corre, il Freerunner e' piu' difficile da colpire. Per questo non dovrebbe essere appesantito e affamato." ),
		
	SNIPER( "cecchino", 
		"I _Cecchini_ riescono a trovare i punti deboli nelle armature nemiche, " +
		"annullandole quando utilizzano armi da lancio." ),
	WARDEN( "guardiano", 
		"Avere un forte legame con la natura da' al _Guardiano_ un abilita' di conservare gocce di rugiada " +
		"e semi dalle piante. Inoltre fermarsi su dell'erba alta garantisce loro un bonus di armatura temporanea." );
	
	private String title;
	private String desc;
	
	private HeroSubClass( String title, String desc ) {
		this.title = title;
		this.desc = desc;
	}
	
	public String title() {
		return title;
	}
	
	public String desc() {
		return desc;
	}
	
	private static final String SUBCLASS	= "subClass";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( SUBCLASS, toString() );
	}
	
	public static HeroSubClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( SUBCLASS );
		try {
			return valueOf( value );
		} catch (Exception e) {
			return NONE;
		}
	}
	
}
