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
package com.watabou.pixeldungeon.levels.features;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.levels.DeadEndLevel;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END = 
		"Che stai facendo qui?!";
	
	private static final String[] TIPS = {
		"Non sopravvalutare la tua forza, usa armi e armature che puoi gestire.",
		"Non tutte le porte nel dungeon sono visibili a prima vista. Se sei bloccato, cerca porte nascoste.",
		"Ricorda, aumentaere la forza non e' l'unico modo per usufruire di equipaggiamenti migliori. Puoi " +
			"invece, abbassare la forza richiesta con la Pergamena del Potenziamento.",
		"Puoi spendere il tuo oro nei negozi piu' in basso nel dungeon. Il primo e' al sesto livello.",
			
		"Stai attento al Goo!",
		
		"Pixel-Mart - Tutto quel che ti serve per una grande avventura!",
		"Identifica le tue pozioni e pergamene il prima possibile. Non buttarli " +
			"quando ti servono.",
		"Essere affamati non ti ferisce, ma essere molto affamati si.",
		"Gli attacchi a sorpresa hanno piu' possibilita' di colpire. Per esempio, puoi tendere un'imboscata ai tuoi nemici " +
			"dietro una porta chiusa quando si avvicinano.",
		
		"Non lasciare scappare il Tengu !",
		
		"Pixel-Mart. Spendi soldi. vivi piu' a lungo.",
		"Quando sei attaccato da piu' mostri insieme, prova a ritirarti dietro una porta.",
		"Se vai a fuoco, non puoi spegnere le fiamme in acqua mentre leviti.",
		"Non ha senso avere piu' Miky allo stesso tempo, perche' li perderai risorgendo.",
		
		"ATTENZIONE! Macchinari pesanti possono causare ferite permanenti, perdita di arti o la morte!",
		
		"Pixel-Mart. Per una vita piu' sicura nel dungeon.",
		"Quando migliori un'arma incantata, c'e' la possibilita' di distruggere l'incantamento.",
		"Armi e armature deteriorano piu' velocemente di bacchette e anelli, ma ci sono piu' modi per ripararle.",
		"L'unico modo per ottenere la Pergamena degli Spiriti e' ottenerla come regalo dagli spiriti del dungeon.",
		
		"Le armi non sono ammesse in presenza di Sua Maesta'!",
		
		"Pixel-Mart. Prezzi speciali per cacciatori di demoni!"
	};
	
	private static final String TXT_BURN =
		"Non appena hai provato a leggere questo cartello, e' bruciato in un fuoco verdastro.";
	
	public static void read( int pos ) {
		
		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;
			
			if (index < TIPS.length) {
				GameScene.show( new WndMessage( TIPS[index] ) );
			} else {
				
				Dungeon.level.destroy( pos );
				GameScene.updateMap( pos );
				GameScene.discoverTile( pos, Terrain.SIGN );
				
				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
				Sample.INSTANCE.play( Assets.SND_BURNING );
				
				GLog.w( TXT_BURN );
				
			}
		}
	}
}
