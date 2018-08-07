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
package com.watabou.pixeldungeon.items.rings;

public class RingOfShadows extends Ring {

	{
		name = "Anello delle Tenebre";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Shadows();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"I nemici ti individueranno meno probabilmente se indossi questo anello. Anelli delle tenebre indeboliti " +
			"allerteranno nemici che invece non avrebbero notato la tua presenza." :
			super.desc();
	}
	
	public class Shadows extends RingBuff {
	}
}
