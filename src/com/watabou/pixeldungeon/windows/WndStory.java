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

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.pixeldungeon.Chrome;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;
	
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();
	
	static {
		CHAPTERS.put( ID_SEWERS, 
		"Il Dungeon giace nelle fondamento della Citta', il suo livello piu' superficiale e' costuitito proprio dalle fognature della Citta'. " +
		"Essendo, almeno per il nome, una parte della Citta', questi livelli non sono pericolosi. Nessuno lo chiamerebbe un posto sicuro, " +
		"ma almeno non avrai a che fare con della magia nera qui." );
		
		CHAPTERS.put( ID_PRISON, 
		"Molti anni fa una prigione sotterranea fu costruita qui per rinchiudere i criminali piu' pericolosi. Al momento sembrava " +
		"proprio una bella idea, perche' da questo posto e' veramente difficile scappare. Ma presto aura malvagia inizio' ad arrivare dalle profondita', " +
		"facendo diventare pazzi prigionieri e guardie. Infine la prigione fu abbandonata, anche se qualche " +
		"prigioniero potrebbe essere stato lasciato bloccato qui giu'." );
		
		CHAPTERS.put( ID_CAVES, 
		"Le caverne, che iniziano appena sotto l'abbandonata prigione, sono scarsamente popolate. Troppo in basso da essere esplorate dagli abitanti  " +
		"della Citta' e troppo poveri di minerali per interessare ai nani. Nel passato c'erano posti di vendita " +
		" da qualche parte sulla strada tra questi due regni, ma sono scomparsi con la caduta della Metropoli Nanica. " +
		"Solo gnomi onnipresenti ed animali sotterranei popolano questi posti." );
		
		CHAPTERS.put( ID_METROPOLIS, 
		"La Metropoli Nanica era una delle piu' grandi citta'-stato dei nani. Nei suoi anni migliori l'esercito meccanizzato nanico " +
		"svento' l'attacco degli dei anziani e del loro esercito demoniaco. Ma si dice, che i guerrieri ritornati " +
		"portarono semi del caos con loro, e che quella vittoria fu solo l'inizio della fine d quel sotterraneo regno." );
		
		CHAPTERS.put( ID_HALLS,
		"Nel passato questi livelli erano le periferie della Metropoli. Dopo la costosa vittoria contro gli dei anziani " +
		"i nani erano troppo indeboliti per ripulirle dai demoni rimasti. Piano piano i demoni allungarono le loro grinfie su questo luogo " +
		"ed ora vengono chiamate Sale Demoniache.\n\n" +
		"Veramente pochi avventurieri scesero fin qui giu'..." );
	};
	
	private BitmapTextMultiline tf;
	
	private float delay;
	
	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );
		
		tf = PixelScene.createMultiline( text, 7 );
		tf.maxWidth = WIDTH - MARGIN * 2;
		tf.measure();
		tf.ra = bgR;
		tf.ga = bgG;
		tf.ba = bgB;
		tf.rm = -bgR;
		tf.gm = -bgG;
		tf.bm = -bgB;
		tf.x = MARGIN;
		add( tf );
		
		add( new TouchArea( chrome ) {
			@Override
			protected void onClick( Touch touch ) {
				hide();
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height(), 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
	
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		
		String text = CHAPTERS.get( id );
		if (text != null) {
			WndStory wnd = new WndStory( text );
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
			}
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
}
