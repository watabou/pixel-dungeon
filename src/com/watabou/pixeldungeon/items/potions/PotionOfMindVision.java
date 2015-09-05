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

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.MindVision;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		name = Game.getVar(R.string.PotionOfMindVision_Name);
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, MindVision.class, MindVision.DURATION );
		Dungeon.observe();
		
		if (Dungeon.level.mobs.size() > 0) {
			GLog.i(Game.getVar(R.string.PotionOfMindVision_Apply1));
		} else {
			GLog.i(Game.getVar(R.string.PotionOfMindVision_Apply2));
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.PotionOfMindVision_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
