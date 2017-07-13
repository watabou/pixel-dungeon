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

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Heap.Type;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;

public class WndInfoItem extends Window {
	
	private static final String TXT_CHEST			= "Forziere";
	private static final String TXT_LOCKED_CHEST	= "Forziere chiuso";
	private static final String TXT_CRYSTAL_CHEST	= "Forziere di cristallo";
	private static final String TXT_TOMB			= "Tomba";
	private static final String TXT_SKELETON		= "Resti d'ossa";
	private static final String TXT_WONT_KNOW		= "Non saprai cosa c'e' dentro finche' non lo aprirai!";
	private static final String TXT_NEED_KEY		= TXT_WONT_KNOW + " Ma hai bisogno della chiave.";
	private static final String TXT_INSIDE			= "Puoi scorgere %s all'interno, ma hai bisogno della chiave per aprire il forziere.";
	private static final String TXT_OWNER	= 
		"Questa vecchia tomba potrebbe contenere qualcosa di utile, " +
		"ma il suo proprietario obiettera' sicuramente se lo cerchi.";
	private static final String TXT_REMAINS	= 
		"Questo e' quello che rimane di un tuo precedessore. " +
		"Forse vale la pena dare un'occhiata.";
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.levelKnown) {
				if (item.level() < 0) {
					color = ItemSlot.DEGRADED;				
				} else if (item.level() > 0) {
					color = item.isBroken() ? ItemSlot.WARNING : ItemSlot.UPGRADED;				
				}
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.MIMIC) {
				title = TXT_CHEST;
				info = TXT_WONT_KNOW;
			} else if (heap.type == Type.TOMB) {
				title = TXT_TOMB;
				info = TXT_OWNER;
			} else if (heap.type == Type.SKELETON) {
				title = TXT_SKELETON;
				info = TXT_REMAINS;
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = TXT_CRYSTAL_CHEST;
				info = Utils.format( TXT_INSIDE, Utils.indefinite( heap.peek().name() ) );
			} else {
				title = TXT_LOCKED_CHEST;
				info = TXT_NEED_KEY;
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.levelKnown) {
			if (item.level() < 0 || item.isBroken()) {
				color = ItemSlot.DEGRADED;				
			} else if (item.level() > 0) {
				color = ItemSlot.UPGRADED;				
			}
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( image, glowing ) );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
		txtInfo.maxWidth = WIDTH;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );
		
		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
	}
}
