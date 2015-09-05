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

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.R;
import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE( null, null ),
	GLADIATOR( Game.getVar(R.string.HeroSubClass_NameGlad), Game.getVar(R.string.HeroSubClass_DescGlad)),
	BERSERKER( Game.getVar(R.string.HeroSubClass_NameBers), Game.getVar(R.string.HeroSubClass_DescBers)),
	WARLOCK(   Game.getVar(R.string.HeroSubClass_NameWarL), Game.getVar(R.string.HeroSubClass_DescWarL)),
	BATTLEMAGE(Game.getVar(R.string.HeroSubClass_NameBatM), Game.getVar(R.string.HeroSubClass_DescBatM)),
	ASSASSIN(  Game.getVar(R.string.HeroSubClass_NameAssa), Game.getVar(R.string.HeroSubClass_DescAssa)),
	FREERUNNER(Game.getVar(R.string.HeroSubClass_NameFreR), Game.getVar(R.string.HeroSubClass_DescFreR)),
	SNIPER(    Game.getVar(R.string.HeroSubClass_NameSnip), Game.getVar(R.string.HeroSubClass_DescSnip)),
	WARDEN(    Game.getVar(R.string.HeroSubClass_NameWard), Game.getVar(R.string.HeroSubClass_DescWard));
	
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
	
	private static final String SUBCLASS = "subClass";
	
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
