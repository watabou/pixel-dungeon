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
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;

public class RingOfHaggler extends Ring {

	{
		name = "Anello della Negoziazione";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Haggling();
	}
	
	@Override
	public Item random() {
		level( +1 );
		return this;
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		identify();
		Badges.validateRingOfHaggler();
		Badges.validateItemLevelAquired( this );
		return super.doPickUp(hero);
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public void use() {
		// Do nothing (it can't degrade)
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"In  realta' questo anello non ha nessun potere magico, ma mostra ai venditori " +
			"che il proprietario e' un membro della Gilda dei Ladri. " +
			"Spesso sono felici di fare degli sconti in cambio " +
			"di una immunita' temporanea. Migliorare quest'anello non ti dara' nessun bonus aggiuntivo." :
			super.desc();
	}
	
	public class Haggling extends RingBuff {	
	}
}
