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
package com.watabou.pixeldungeon.items;

import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class Ankh extends Item {

	{
		stackable = true;
		name = "Miky";
		image = ItemSpriteSheet.ANKH;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		return 
			"L'antico simbolo dell'immortalita' garantisce l'abilita' di tornare in vita dopo la morte. " +
			"Risorgendo perderai tutti gli oggetti non equipaggiati.";
	}
	
	@Override
	public int price() {
		return 50 * quantity;
	}
}
