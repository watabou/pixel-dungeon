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
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class WndWandmaker extends WndQuest {
	
	private static final String TXT_MESSAGE	= 
		"Oh, I see you have succeeded! I do hope it hasn't troubled you too much. " +
		"As I promised, you can choose one of my high quality wands.";
	private static final String TXT_BATTLE		= "Battle wand";
	private static final String TXT_NON_BATTLE	= "Non-battle wand";
	
	private static final String TXT_FARAWELL	= "Good luck in your quest, %s!";
	
	private Wandmaker wandmaker;
	private Item questItem;
	
	
	public WndWandmaker( final Wandmaker wandmaker, final Item item ) {
		
		super( wandmaker, TXT_MESSAGE, TXT_BATTLE, TXT_NON_BATTLE );
		
		this.wandmaker = wandmaker;
		questItem = item;
	}
	
	@Override
	protected void onSelect( int index ) {

		questItem.detach( Dungeon.hero.belongings.backpack );
		
		Item reward = index == 0 ? Wandmaker.Quest.wand1 : Wandmaker.Quest.wand2;
		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, wandmaker.pos ).sprite.drop();
		}
		
		wandmaker.yell( Utils.format( TXT_FARAWELL, Dungeon.hero.className() ) );
		wandmaker.destroy();
		
		wandmaker.sprite.die();
		
		Wandmaker.Quest.complete();
	}
}
