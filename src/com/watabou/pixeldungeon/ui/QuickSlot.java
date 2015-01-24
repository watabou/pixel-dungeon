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
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Bundle;

public class QuickSlot extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";
	
	private static QuickSlot primary;
	private static QuickSlot secondary;
	
	private Item itemInSlot;
	private ItemSlot slot;
	
	private Image crossB;
	private Image crossM;
	
	private boolean targeting = false;
	private Item lastItem = null;
	private Char lastTarget= null;

	public static Object primaryValue;
	public static Object secondaryValue;
	
	public void primary() {
		primary = this;
		item( select() );
	}
	
	public void secondary() {
		secondary = this;
		item( select() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (this == primary) {
			primary = null;
		} else {
			secondary = null;
		}
		
		lastItem = null;
		lastTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					GameScene.handleCell( lastTarget.pos );
				} else {
					Item item = select();
					if (item == lastItem) {
						useTargeting();
					} else {
						lastItem = item;
					}
					item.execute( Dungeon.hero );
				}
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlot.this.onLongClick();
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );
		
		crossM = new Image();
		crossM.copy( crossB );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill( this );
		
		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
	}
	
	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
	}
	
	@Override
	protected boolean onLongClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private Item select() {
		
		Object content = (this == primary ? primaryValue : secondaryValue);
		if (content instanceof Item) {
			
			return (Item)content;
			
		} else if (content != null) {
			
			Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)content );			
			return item != null ? item : Item.virtual( (Class<? extends Item>)content );
			
		} else {
			
			return null;
			
		}
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			if (this == primary) {
				primaryValue = (item.stackable ? item.getClass() : item);
			} else {
				secondaryValue = (item.stackable ? item.getClass() : item);
			}
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
		itemInSlot = item;
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable( 
			itemInSlot != null && 
			itemInSlot.quantity() > 0 && 
			(Dungeon.hero.belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.hero )));
	}
	
	private void useTargeting() {
		
		targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.visible[lastTarget.pos];
		
		if (targeting) {
			if (Actor.all().contains( lastTarget )) {
				lastTarget.sprite.parent.add( crossM );
				crossM.point( DungeonTilemap.tileToWorld( lastTarget.pos ) );
				crossB.visible = true;
			} else {
				lastTarget = null;
			}
		}
	}
	
	public static void refresh() {
		if (primary != null) {
			primary.item( primary.select() );
		}
		if (secondary != null) {
			secondary.item( secondary.select() );
		}
	}
	
	public static void target( Item item, Char target ) {
		if (target != Dungeon.hero) {
			if (item == primary.lastItem) {
				
				primary.lastTarget = target;
				HealthIndicator.instance.target( target );
				
			} else if (item == secondary.lastItem) {
				
				secondary.lastTarget = target;
				HealthIndicator.instance.target( target );
				
			}
		}
	}
	
	public static void cancel() {
		if (primary != null && primary.targeting) {
			primary.crossB.visible = false;
			primary.crossM.remove();
			primary.targeting = false;
		}
		if (secondary != null && secondary.targeting) {
			secondary.crossB.visible = false;
			secondary.crossM.remove();
			secondary.targeting = false;
		}
	}
	
	private static final String QUICKSLOT1	= "quickslot";
	private static final String QUICKSLOT2	= "quickslot2";
	
	@SuppressWarnings("unchecked")
	public static void save( Bundle bundle ) {
		Belongings stuff = Dungeon.hero.belongings;
		
		if (primaryValue instanceof Class && 
			stuff.getItem( (Class<? extends Item>)primaryValue ) != null) {
				
			bundle.put( QUICKSLOT1, ((Class<?>)primaryValue).getName() );
		}
		if (QuickSlot.secondaryValue instanceof Class &&
			stuff.getItem( (Class<? extends Item>)secondaryValue ) != null &&
			Toolbar.secondQuickslot()) {
					
			bundle.put( QUICKSLOT2, ((Class<?>)secondaryValue).getName() );
		}
	}
	
	public static void save( Bundle bundle, Item item ) {
		if (item == primaryValue) {
			bundle.put( QuickSlot.QUICKSLOT1, true );
		}
		if (item == secondaryValue && Toolbar.secondQuickslot()) {
			bundle.put( QuickSlot.QUICKSLOT2, true );
		}
	}
	
	public static void restore( Bundle bundle ) {
		primaryValue = null;
		secondaryValue = null;
		
		String qsClass = bundle.getString( QUICKSLOT1 );
		if (qsClass != null) {
			try {
				primaryValue = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		}
		
		qsClass = bundle.getString( QUICKSLOT2 );
		if (qsClass != null) {
			try {
				secondaryValue = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		}
	}
	
	public static void restore( Bundle bundle, Item item ) {
		if (bundle.getBoolean( QUICKSLOT1 )) {
			primaryValue = item;
		}
		if (bundle.getBoolean( QUICKSLOT2 )) {
			secondaryValue = item;
		}
	}
	
	public static void compress() {
		if ((primaryValue == null && secondaryValue != null) ||
			(primaryValue == secondaryValue)) {
				
			primaryValue = secondaryValue;
			secondaryValue = null;
		}
	}
}
