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
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.MindVision;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		name = "Pozione della Visione Maggica";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, MindVision.class, MindVision.DURATION );
		Dungeon.observe();
		
		if (Dungeon.level.mobs.size() > 0) {
			GLog.i( "Puoi in qualche modo sentire la presenza delle altre creature!" );
		} else {
			GLog.i( "Puoi in qualche modo sentire che sei da solo su questo livello al momento." );
		}
	}
	
	@Override
	public String desc() {
		return
			"Dopo avere bevuto questa pozione, la tua mente sara' capace di percepire presenze vitali attraverso " +
			"i muri. Questa pozione ti permette anche di vedere attraverso muri e porte vicine.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
