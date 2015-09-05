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
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.effects.Identification;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfIdentify extends InventoryScroll {

	{
		name = Game.getVar(R.string.ScrollOfIdentify_Name);
		inventoryTitle = Game.getVar(R.string.ScrollOfIdentify_InvTitle);
		mode = WndBag.Mode.UNIDENTIFED;
	}
	
	@Override
	protected void onItemSelected( Item item ) {
		
		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
		
		item.identify();
		GLog.i(String.format(Game.getVar(R.string.ScrollOfIdentify_Info1), item));
		
		Badges.validateItemLevelAquired( item );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.ScrollOfIdentify_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
