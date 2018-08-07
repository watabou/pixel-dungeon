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
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.items.armor.ClothArmor;
import com.watabou.pixeldungeon.items.bags.Keyring;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.ShortSword;
import com.watabou.pixeldungeon.items.weapon.missiles.Dart;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "guerriero", "GUERRIERO"), MAGE( "mago", "MAGO" ), ROGUE( "rogue", "ROGUE" ), HUNTRESS( "cacciatrice", "CACCIATRICE" );

	private String title;

	private String className;
	
	private HeroClass( String title, String className ) {
		this.title = title;
		this.className = className;
	}

	public static final String[] WAR_PERKS = {
		"I guerrieri iniziano con 11 punti di Forza.",
		"I guerrieri iniziano con un'unica spada corta. Questa spada puo' essere \"riforgiata\" per migliorare un'altra arma da corpo a corpo.",
		"I guerrieri sono poco competenti con le armi da tiro.",
		"Ciasciuna razione di cibo restuisce un po' di salute quando consumata.",
		"Le Pozioni di Forza sono identificati sin dall'inizio.",
	};

	public static final String[] MAG_PERKS = {
		"I maghi iniziano con un'unica Bacchetta del Dardo Incantato. Questa bacchetta puo' essere successivamente \"disincantata\" per migliorare un'altra bacchetta.",
		"I maghi ricaricano le loro bacchette piu' velocemente.",
		"Quando mangiato, ciascuna razione di cibo restutisce 1 carica per tutte le bacchette nell'inventario.",
		"I maghi possono usare le bacchette come armi da corpo a corpo.",
		"Le Pergamene dell'Identificazione sono identificate sin dall'inizio."
	};

	public static final String[] ROG_PERKS = {
		"I furfanti iniziano con un Anello delle Tenebre+1.",
		"I furfanti identificano un tipo di anello equipaggiandolo.",
		"I furfanti sono migliori con armature leggere, schivando meglio gli attacchi quando ne indossano una.",
		"I furfanti sono esperti nel individuare porte nascoste e trappole.",
		"I furfanti possono resistere senza cibo piu' a lungo.",
		"Le Pergamene della Mappatura Magica sono identificate sin dall'inizio."
	};

	public static final String[] HUN_PERKS = {
		"Le cacciatrici iniziano con 15 punti di Salute.",
		"Le cacciatrici iniziano con un unico boomerang migliorabile.",
		"Le cacciatrici sono migliori con armi da tiro e ottengono un danno aggiuntivo quando le usano.",
		"Le cacciatrici guadagnano piu' salute dalle gocce di rugiada.",
		"Le cacciatrici avvertono i mostri nelle vicinanze anche se questi sono nascosti dietro gli ostacoli."
	};

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
		case WARRIOR:
			initWarrior( hero );
			break;

		case MAGE:
			initMage( hero );
			break;

		case ROGUE:
			initRogue( hero );
			break;

		case HUNTRESS:
			initHuntress( hero );
			break;
		}

		if (Badges.isUnlocked( masteryBadge() )) {
			new TomeOfMastery().collect();
		}

		hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
		(hero.belongings.armor = new ClothArmor()).identify();
		new Food().identify().collect();
		new Keyring().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
		case MAGE:
			return Badges.Badge.MASTERY_MAGE;
		case ROGUE:
			return Badges.Badge.MASTERY_ROGUE;
		case HUNTRESS:
			return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		hero.STR = hero.STR + 1;

		(hero.belongings.weapon = new ShortSword()).identify();
		new Dart( 8 ).identify().collect();

		QuickSlot.primaryValue = Dart.class;

		new PotionOfStrength().setKnown();
	}

	private static void initMage( Hero hero ) {
		(hero.belongings.weapon = new Knuckles()).identify();

		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();

		QuickSlot.primaryValue = wand;

		new ScrollOfIdentify().setKnown();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();

		hero.belongings.ring1.activate( hero );

		QuickSlot.primaryValue = Dart.class;

		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress( Hero hero ) {

		hero.HP = (hero.HT -= 5);

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		QuickSlot.primaryValue = boomerang;
	}

	public String title() {
		return title;
	}
	
	public String className() {
		return className;
	}

	public String spritesheet() {

		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}

		return null;
	}

	public String[] perks() {

		switch (this) {
		case WARRIOR:
			return WAR_PERKS;
		case MAGE:
			return MAG_PERKS;
		case ROGUE:
			return ROG_PERKS;
		case HUNTRESS:
			return HUN_PERKS;
		}

		return null;
	}

	private static final String CLASS	= "class";

	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}

	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
