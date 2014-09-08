/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfWeaponUpgrade extends InventoryScroll {

	private static final String TXT_LOOKS_BETTER = Game.getVar(R.string.ScrollOfWeaponUpgrade_LooksBetter);
	
	{
		name = Game.getVar(R.string.ScrollOfWeaponUpgrade_Name);
		inventoryTitle = Game.getVar(R.string.ScrollOfWeaponUpgrade_InvTitle);
		mode = WndBag.Mode.WEAPON;
	}
	
	@Override
	protected void onItemSelected( Item item ) {
		
		Weapon weapon = (Weapon)item;
		
		ScrollOfRemoveCurse.uncurse( Dungeon.hero, weapon );
		weapon.upgrade( true );
		
		GLog.p( TXT_LOOKS_BETTER, weapon.name() );
		
		Badges.validateItemLevelAquired( weapon );
		
		curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.ScrollOfWeaponUpgrade_Info);
	}
}
