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

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.R;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	
	public MeleeWeapon( int tier, float acu, float dly ) {
		super();
		
		this.tier = tier;
		
		ACU = acu;
		DLY = dly;
		
		STR = typicalSTR();
		
		MIN = min();
		MAX = max();
	}
	
	private int min() {
		return tier;
	}
	
	private int max() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}
	
	@Override
	final public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		STR--;		
		MIN++;
		MAX += tier;
		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}
	
	@Override
	public Item degrade() {		
		STR++;
		MIN--;
		MAX -= tier;
		return super.degrade();
	}
	
	public int typicalSTR() {
		return 8 + tier * 2;
	}
	
	@Override
	public String info() {
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );

		String typical  = Game.getVar(R.string.MeleeWeapon_Info1b);
		String upgraded = Game.getVar(R.string.MeleeWeapon_Info1c);
		String degraded = Game.getVar(R.string.MeleeWeapon_Info1d);
		String quality = levelKnown && level != 0 ? (level > 0 ? upgraded : degraded) : typical;
		info.append(p);
		info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info1a), name, quality, tier));
		info.append(" ");
		
		if (levelKnown) {
			info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info2a), (MIN + (MAX - MIN) / 2)));
		} else {
			info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info2b), (min() + (max() - min()) / 2), typicalSTR()));
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append(" "+Game.getVar(R.string.MeleeWeapon_Info2c));
			}
		}

		quality = "";
		info.append(" ");
		if (DLY != 1f) {
			quality += (DLY < 1f ? Game.getVar(R.string.MeleeWeapon_Info3b) : Game.getVar(R.string.MeleeWeapon_Info3c));
			if (ACU != 1f) {
				quality += " ";
				if ((ACU > 1f) == (DLY < 1f)) {
					quality += Game.getVar(R.string.MeleeWeapon_Info3d);
				} else {
					quality += Game.getVar(R.string.MeleeWeapon_Info3e);
				}
				quality += " ";
				quality += ACU > 1f ? Game.getVar(R.string.MeleeWeapon_Info3f) : Game.getVar(R.string.MeleeWeapon_Info3g);
			}
			info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info3a), quality));
		} else if (ACU != 1f) {
			quality += ACU > 1f ? Game.getVar(R.string.MeleeWeapon_Info3f) : Game.getVar(R.string.MeleeWeapon_Info3g);
			info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info3a), quality));
		}

		info.append(" ");
		switch (imbue) {
		case SPEED:
			info.append(Game.getVar(R.string.MeleeWeapon_Info4a));
			break;
		case ACCURACY:
			info.append(Game.getVar(R.string.MeleeWeapon_Info4b));
			break;
		case NONE:
		}

		info.append(" ");
		if (enchantment != null) {
			info.append(Game.getVar(R.string.MeleeWeapon_Info5));
		}

		if (levelKnown && Dungeon.hero.belongings.backpack.items.contains( this )) {
			info.append(p);
			if (STR > Dungeon.hero.STR()) {
				info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info6a), name));
			}
			if (STR < Dungeon.hero.STR()) {
				info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info6b), name));
			}
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append(p);
			info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info7a), name, (cursed ? Game.getVar(R.string.MeleeWeapon_Info7b) : "")) ); 
		} else {
			if (cursedKnown && cursed) {
				info.append(p);
				info.append(String.format(Game.getVar(R.string.MeleeWeapon_Info7c), name));
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
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
	
	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level ) == 0) {
			enchant();
		}
		
		return this;
	}
}
