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
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	
	public MeleeWeapon( int tier, float acu, float dly ) {
		super();
		
		this.tier = tier;
		
		ACU = acu;
		DLY = dly;
		
		STR = typicalSTR();
	}
	
	protected int min0() {
		return tier;
	}
	
	protected int max0() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}
	
	@Override
	public int min() {
		return isBroken() ? min0() : min0() + level(); 
	}
	
	@Override
	public int max() {
		return isBroken() ? max0() : max0() + level() * tier;
	}
	
	@Override
	final public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		STR--;		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}
	
	@Override
	public Item degrade() {		
		STR++;
		return super.degrade();
	}
	
	public int typicalSTR() {
		return 8 + tier * 2;
	}
	
	@Override
	public String info() {
		
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );
		
		int lvl = visiblyUpgraded();
		String quality = lvl != 0 ? 
			(lvl > 0 ? 
				(isBroken() ? "rotta" : "migliorata") : 
				"indebolita") : 
			"";
		info.append( p );
		info.append( "Questa e' una" + name + quality + " e ");
		info.append( "un'arma da corpo a corpo. " );
		
		if (levelKnown) {
			int min = min();
			int max = max();
			info.append( "Il suo danno medio e' di " + (min + (max - min) / 2) + " punti d'attacco. " );
		} else {
			int min = min0();
			int max = max0();
			info.append( 
				"Il suo danno medio e' di " + (min + (max - min) / 2) + " punti d'attacco " +
				"e di solito richiede " + typicalSTR() + " punti di forza. " );
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append( "Probabilmente quest'arma e' troppo pesante per te." );
			}
		}
		
		if (DLY != 1f) {
			info.append( "Questa e' un'arma piuttosto " + (DLY < 1f ? "veloce" : "lenta") );
			if (ACU != 1f) {
				if ((ACU > 1f) == (DLY < 1f)) {
					info.append( " e ");
				} else {
					info.append( " ma ");
				}
				info.append( ACU > 1f ? "precisa." : "imprecisa." );
			}
		} else if (ACU != 1f) {
			info.append( "Questa e' un'arma piuttosto " + (ACU > 1f ? "precisa." : "imprecisa."));
		}
		switch (imbue) {
		case SPEED:
			info.append( "E' stata bilanciata per essere piu' veloce. " );
			break;
		case ACCURACY:
			info.append( "E' stata bilanciata per essere piu' precisa. " );
			break;
		case NONE:
		}
		
		if (enchantment != null) {
			info.append( "E' incantata." );
		}
		
		if (levelKnown && Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append( p );
				info.append( 
					"A causa della tua forza inadeguata la precisione e la velocita' " +
					"dei tuoi attacchi sono diminuite." );
			}
			if (STR < Dungeon.hero.STR()) {
				info.append( p );
				info.append( 
					"A causa della tua forza eccessiva " +
					"della tua forza la precisione e la velocita' sono aumentate." );
			}
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append( p );
			info.append( "Stai tenendo " + name + " al momento" + 
				(cursed ? ", e a causa della maledizione, non puoi lasciarla." : ".") ); 
		} else {
			if (cursedKnown && cursed) {
				info.append( p );
				info.append( "Puoi percepire una parvenza di magia oscura scorrere in " + name +"." );
			}
		}
		
		return info.toString();
	}
	
	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (enchantment != null) {
			price *= 1.5;
		}
		return considerState( price );
	}
	
	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level() ) == 0) {
			enchant();
		}
		
		return this;
	}
}
