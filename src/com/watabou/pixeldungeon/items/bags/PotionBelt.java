package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class PotionBelt extends Bag {

	{
		name = "potion belt";
		image = ItemSpriteSheet.BELT;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Potion;
	}
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"This storng lether belt allows you to store all your potions";
	}
	
}
