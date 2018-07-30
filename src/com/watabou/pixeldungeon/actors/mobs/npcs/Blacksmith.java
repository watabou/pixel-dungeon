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
package com.watabou.pixeldungeon.actors.mobs.npcs;

import java.util.Collection;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.DarkGold;
import com.watabou.pixeldungeon.items.quest.Pickaxe;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Room.Type;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.BlacksmithSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBlacksmith;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blacksmith extends NPC {

	private static final String TXT_GOLD_1 =
		"Hey umano! Voi esse' d'aiuto, eh? Prendi 'sto piccone e trovame un po' de _oro oscuro grezzo_, _15 pezzi_ dovrebbero basta'. " +
		"Che intendi, dovrei pagatte? Tutti avari voi altri...\n" +
		"Ok, ok, Non ho sordi pe' pagatte, ma posso forgiatte qualcosa. Considerate fortunato, " +
		"so' l'unico fabbro nei dintorni.";
	private static final String TXT_BLOOD_1 =
		"Hey umano! Voi esse' d'aiuto, eh? Prendi 'sto piccone e uccidime _un pipistrello_, me serve il suo sangue. " +
		"Che intendi, dovrei pagatte? Tutti avari voi altri...\n" +
		"Ok, ok, Non ho sordi pe' pagatte, ma posso forgiatte qualcosa. Considerate fortunato, " +
		"so' l'unico fabbro nei dintorni.";
	private static final String TXT2 =
		"Me prendi in giro? Dove hai messo il mio piccone?!";
	private static final String TXT3 =
		"Oro oscuro grezzo. 15 pezzi. Davvero, e' cosi' difficile?";
	private static final String TXT4 =
		"Ho detto che me serve sangue de pipistrello sul piccone. Datte 'na mossa!";
	private static final String TXT_COMPLETED =
		"Oh, sei tornato... meglio tardi che mai.";
	private static final String TXT_GET_LOST =
		"Sono occupato. Scio'!";
	
	private static final String TXT_LOOKS_BETTER	= "%s ha un aspetto migliore ora";
	
	{
		name = "fabbro troll";
		spriteClass = BlacksmithSprite.class;
	}
	
	@Override
	protected boolean act() {
		throwItem();		
		return super.act();
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		if (!Quest.given) {
			
			GameScene.show( new WndQuest( this, 
				Quest.alternative ? TXT_BLOOD_1 : TXT_GOLD_1 ) {
				
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					
					Quest.given = true;
					Quest.completed = false;
					
					Pickaxe pick = new Pickaxe();
					if (pick.doPickUp( Dungeon.hero )) {
						GLog.i( Hero.TXT_YOU_NOW_HAVE, pick.name() );
					} else {
						Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
					}
				};
			} );
			
			Journal.add( Journal.Feature.TROLL );
			
		} else if (!Quest.completed) {
			if (Quest.alternative) {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (!pick.bloodStained) {
					tell( TXT4 );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						pick.doUnequip( Dungeon.hero, false );
					}
					pick.detach( Dungeon.hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			} else {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				DarkGold gold = Dungeon.hero.belongings.getItem( DarkGold.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (gold == null || gold.quantity() < 15) {
					tell( TXT3 );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						pick.doUnequip( Dungeon.hero, false );
					}
					pick.detach( Dungeon.hero.belongings.backpack );
					gold.detachAll( Dungeon.hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			}
		} else if (!Quest.reforged) {
			
			GameScene.show( new WndBlacksmith( this, Dungeon.hero ) );
			
		} else {
			
			tell( TXT_GET_LOST );
			
		}
	}
	
	private void tell( String text ) {
		GameScene.show( new WndQuest( this, text ) );
	}
	
	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return "Seleziona 2 oggetti differenti, non lo stesso due volte!";
		}
		
		if (item1.getClass() != item2.getClass()) {
			return "Seleziona 2 oggetti dello stesso tipo!";
		}
		
		if (!item1.isIdentified() || !item2.isIdentified()) {
			return "Devo sapere con che sto lavorando, identificali prima!";
		}
		
		if (item1.cursed || item2.cursed) {
			return "Non lavoro con roba maledetta!";
		}
		
		if (item1.level() < 0 || item2.level() < 0) {
			return "E' uno schifo, la qualita' fa schifo!";
		}
		
		if (!item1.isUpgradable() || !item2.isUpgradable()) {
			return "Non posso riforgiarli!";
		}
		
		return null;
	}
	
	public static void upgrade( Item item1, Item item2 ) {
		
		Item first, second;
		if (item2.level() > item1.level()) {
			first = item2;
			second = item1;
		} else {
			first = item1;
			second = item2;
		}

		Sample.INSTANCE.play( Assets.SND_EVOKE );
		ScrollOfUpgrade.upgrade( Dungeon.hero );
		Item.evoke( Dungeon.hero );

		if (first.isEquipped( Dungeon.hero )) {
			((EquipableItem)first).doUnequip( Dungeon.hero, true );
		}
		first.upgrade();
		GLog.p( TXT_LOOKS_BETTER, first.name() );
		Dungeon.hero.spendAndNext( 2f );
		Badges.validateItemLevelAquired( first );

		if (second.isEquipped( Dungeon.hero )) {
			((EquipableItem)second).doUnequip( Dungeon.hero, false );
		}
		second.detachAll( Dungeon.hero.belongings.backpack );

		Quest.reforged = true;
		
		Journal.remove( Journal.Feature.TROLL );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"Questo fabbro troll assomiglia a ogni altro troll; E' alto e magro, e la sua pelle sembra pietra " +
			"sia nel colore che nella forma. Il fabbro sta inappropiatamente lavorando con piccoli strumenti .";
	}

	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static void spawn( Collection<Room> rooms ) {
			if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth ) == 0) {
				
				Room blacksmith = null;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 4 && r.height() > 4) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;
						
						spawned = true;
						alternative = Random.Int( 2 ) == 0;
						
						given = false;
						
						break;
					}
				}
			}
		}
	}
}
