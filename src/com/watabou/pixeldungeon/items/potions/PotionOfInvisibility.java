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
package com.watabou.pixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfInvisibility extends Potion {

	private static final float ALPHA	= 0.4f;
	
	{
		name = "Pozione dell'Invisibilita'";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
		GLog.i( "Vedi le tue mani diventare invisibili!" );
		Sample.INSTANCE.play( Assets.SND_MELD );
	}
	
	@Override
	public String desc() {
		return
			"Bere questa pozione ti rendera' temporaneamente invisibile. Mentre sei invisibile, " +
			"i nemici non ti vedranno. Attaccare un nemico, anche con una bacchetta o con una pergamena, " +
			"davanti i suoi occhi, fara' sparire l'effetto.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
	
	public static void melt( Char ch ) {
		if (ch.sprite.parent != null) {
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, ALPHA, 0.4f ) );
		} else {
			ch.sprite.alpha( ALPHA );
		}
	}
}
