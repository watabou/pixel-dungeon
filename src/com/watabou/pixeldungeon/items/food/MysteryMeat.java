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
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat extends Food {

	{
		name = "carne misteriosa";
		image = ItemSpriteSheet.MEAT;
		energy = Hunger.STARVING - Hunger.HUNGRY;
		message = "Sapeva di... non so.";
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			
			switch (Random.Int( 5 )) {
			case 0:
				GLog.w( "Oh e' bollente!" );
				Buff.affect( hero, Burning.class ).reignite( hero );
				break;
			case 1:
				GLog.w( "Non senti piu' le gambe!" );
				Buff.prolong( hero, Roots.class, Paralysis.duration( hero ) );
				break;
			case 2:
				GLog.w( "Non ti senti bene." );
				Buff.affect( hero, Poison.class ).set( Poison.durationFactor( hero ) * hero.HT / 5 );
				break;
			case 3:
				GLog.w( "Ti senti gonfio e lento." );
				Buff.prolong( hero, Slow.class, Slow.duration( hero ) );
				break;
			}
		}
	}
	
	@Override
	public String info() {
		return "Mangiala a tuo rischio e pericolo!";
	}
	
	public int price() {
		return 5 * quantity;
	};
}
