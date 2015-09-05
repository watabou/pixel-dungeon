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
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Levitation;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfLevitation extends Potion {

	{
		name = Game.getVar(R.string.PotionOfLevitation_Name);
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Levitation.class, Levitation.DURATION );
		GLog.i(Game.getVar(R.string.PotionOfLevitation_Apply));
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.PotionOfLevitation_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
