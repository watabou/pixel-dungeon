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
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfEnchantment extends InventoryScroll {

	private static final String TXT_GLOWS	= Game.getVar(R.string.ScrollOfEnchantment_Glows);
	
	{
		name = Game.getVar(R.string.ScrollOfEnchantment_Name);
		inventoryTitle = Game.getVar(R.string.ScrollOfEnchantment_InvTitle);
		mode = WndBag.Mode.ENCHANTABLE;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfRemoveCurse.uncurse( Dungeon.hero, item );
		
		if (item instanceof Weapon) {
			
			((Weapon)item).enchant();
			
		} else {

			((Armor)item).inscribe();
		
		}
		
		item.fix();
		
		curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
		GLog.w( TXT_GLOWS, item.name() );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.ScrollOfEnchantment_Info);
	}
}
