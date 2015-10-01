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

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Challenges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.CursePersonification;
import com.watabou.pixeldungeon.actors.mobs.FetidRat;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.ClothArmor;
import com.watabou.pixeldungeon.items.quest.DriedRose;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.SewerLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GhostSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.pixeldungeon.windows.WndSadGhost;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost extends NPC {

	{
		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
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
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		Quest.type.handler.interact( this );
	}
	
	@Override
	public String description() {
		return 
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static void replace( final Mob a, final Mob b ) {
		final float FADE_TIME = 0.5f;
		
		a.destroy();
		a.sprite.parent.add( new AlphaTweener( a.sprite, 0, FADE_TIME ) {
			protected void onComplete() {
				a.sprite.killAndErase();
				parent.erase( this );
			};
		} );
		
		b.pos = a.pos;
		GameScene.add( b );
		
		b.sprite.flipHorizontal = a.sprite.flipHorizontal;
		b.sprite.alpha( 0 );
		b.sprite.parent.add( new AlphaTweener( b.sprite, 1, FADE_TIME ) );
	}

	public static class Quest {

		enum Type {
			ILLEGAL( null ), ROSE( roseQuest ), RAT( ratQuest ), CURSE( curseQuest );
			
			public QuestHandler handler;
			private Type( QuestHandler handler ) {
				this.handler = handler;
			}
		}
		
		private static Type type;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean processed;
		
		private static int depth;
		
		private static int left2kill;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false; 
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE		= "type";
		private static final String ALTERNATIVE	= "alternative";
		private static final String LEFT2KILL	= "left2kill";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type.toString() );
				if (type == Type.ROSE) {
					node.put( LEFT2KILL, left2kill );
				}
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed );
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				type = node.getEnum( TYPE, Type.class );
				if (type == Type.ILLEGAL) {
					type = node.getBoolean( ALTERNATIVE ) ? Type.RAT : Type.ROSE;
				}
				if (type == Type.ROSE) {
					left2kill = node.getInt( LEFT2KILL );
				}
				
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
				processed	= node.getBoolean( PROCESSED );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
				switch (Random.Int( 3 )) {
				case 0:
					type = Type.ROSE;
					left2kill = 8;
					break;
				case 1:
					type = Type.RAT;
					break;
				case 2:
					type = Type.CURSE;
					break;
				}
				
				given = false;
				processed = false;
				depth = Dungeon.depth;
				
				for (int i=0; i < 4; i++) {
					Item another;
					do {
						another = (Weapon)Generator.random( Generator.Category.WEAPON );
					} while (another instanceof MissileWeapon);
					
					if (weapon == null || another.level() > weapon.level()) {
						weapon = (Weapon)another;
					}
				}
				
				if (Dungeon.isChallenged( Challenges.NO_ARMOR )) {
					armor = (Armor)new ClothArmor().degrade();
				} else {
					armor = (Armor)Generator.random( Generator.Category.ARMOR );
					for (int i=0; i < 3; i++) {
						Item another = Generator.random( Generator.Category.ARMOR );
						if (another.level() > armor.level()) {
							armor = (Armor)another;
						}
					}
				}
				
				weapon.identify();
				armor.identify();
			}
		}

		public static void processSewersKill( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				switch (type) {
				case ROSE:
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					break;
				case RAT:
					FetidRat rat = new FetidRat();
					rat.pos = Dungeon.level.randomRespawnCell();
					if (rat.pos != -1) {
						GameScene.add( rat );
						processed = true;
					}
					break;
				default:
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}
	}

	abstract public static class QuestHandler {
		
		abstract public void interact(  Ghost ghost ); 
		
		protected void relocate( Ghost ghost ) {
			int newPos = -1;
			for (int i=0; i < 10; i++) {
				newPos = Dungeon.level.randomRespawnCell();
				if (newPos != -1) {
					break;
				}
			}
			if (newPos != -1) {
				
				Actor.freeCell( ghost.pos );
				
				CellEmitter.get( ghost.pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
				ghost.pos = newPos;
				ghost.sprite.place( ghost.pos );
				ghost.sprite.visible = Dungeon.visible[ghost.pos];
			}
		}
	}
	
	private static final QuestHandler roseQuest = new QuestHandler() {
		private static final String TXT_ROSE1	=
			"Hello adventurer... Once I was like you - strong and confident... " +
			"And now I'm dead... But I can't leave this place... Not until I have my _dried rose_... " +
			"It's very important to me... Some monster stole it from my body...";
		
		private static final String TXT_ROSE2	=
			"Please... Help me... _Find the rose_...";
		
		private static final String TXT_ROSE3	= 
			"Yes! Yes!!! This is it! Please give it to me! " +
			"And you can take one of these items, maybe they " +
			"will be useful to you in your journey...";

		public void interact( Ghost ghost ) {
			if (Quest.given) {

				Item item = Dungeon.hero.belongings.getItem( DriedRose.class );	
				if (item != null) {
					GameScene.show( new WndSadGhost( ghost, item, TXT_ROSE3 ) );
				} else {
					GameScene.show( new WndQuest( ghost, TXT_ROSE2 ) );
					relocate( ghost );
				}
				
			} else {
				GameScene.show( new WndQuest( ghost, TXT_ROSE1 ) );
				Quest.given = true;
				
				Journal.add( Journal.Feature.GHOST );
			}
		}
	};
	
	private static final QuestHandler ratQuest = new QuestHandler() {
		private static final String TXT_RAT1	=
			"Hello adventurer... Once I was like you - strong and confident... " +
			"And now I'm dead... But I can't leave this place... Not until I have my revenge... " +
			"Slay the _fetid rat_, that has taken my life...";
			
		private static final String TXT_RAT2	=
			"Please... Help me... _Slay the abomination_...";
		
		private static final String TXT_RAT3	= 
			"Yes! The ugly creature is slain and I can finally rest... " +
			"Please take one of these items, maybe they " +
			"will be useful to you in your journey...";
		
		public void interact( Ghost ghost ) {
			if (Quest.given) {

				Item item = Dungeon.hero.belongings.getItem( RatSkull.class );	
				if (item != null) {
					GameScene.show( new WndSadGhost( ghost, item, TXT_RAT3 ) );
				} else {
					GameScene.show( new WndQuest( ghost, TXT_RAT2 ) );
					relocate( ghost );
				}
				
			} else {
				GameScene.show( new WndQuest( ghost, TXT_RAT1 ) );
				Quest.given = true;
				
				Journal.add( Journal.Feature.GHOST );
			}
		}
	};
	
	private static final QuestHandler curseQuest = new QuestHandler() {
		private static final String TXT_CURSE1 =
			"Hello adventurer... Once I was like you - strong and confident... " +
			"And now I'm dead... But I can't leave this place, as I am bound by a horrid curse... " +
			"Please... Help me... _Destroy the curse_...";
		private static final String TXT_CURSE2 =
			"Thank you, %s! The curse is broken and I can finally rest... " +
			"Please take one of these items, maybe they " +
			"will be useful to you in your journey...";
		
		private static final String TXT_YES	= "Yes, I will do it for you";
		private static final String TXT_NO	= "No, I can't help you";
		
		public void interact( final Ghost ghost ) {
			if (Quest.given) {

				GameScene.show( new WndSadGhost( ghost, null, Utils.format( TXT_CURSE2, Dungeon.hero.className() ) ) );
				
			} else {
				GameScene.show( new WndQuest( ghost, TXT_CURSE1, TXT_YES, TXT_NO ) {
					protected void onSelect( int index ) {
						if (index == 0) {
							Quest.given = true;
							
							CursePersonification d = new CursePersonification();
							Ghost.replace( ghost, d );
							
							d.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
							Sample.INSTANCE.play( Assets.SND_GHOST );
							
							Dungeon.hero.next();
						} else {
							relocate( ghost );
						}
					};
				} );
				
				Journal.add( Journal.Feature.GHOST );
			}
		}
	};
}
