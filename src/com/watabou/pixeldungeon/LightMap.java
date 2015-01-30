package com.watabou.pixeldungeon;

import android.graphics.Bitmap;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.utils.Random;

public class LightMap extends Image {
	
	private int[] pixels;
	
	private int pWidth;
	private int pHeight;
	
	private int width2;
	private int height2;
	
	public LightMap( int mapWidth, int mapHeight ) {
		
		super();
		
		pWidth = mapWidth;
		pHeight = mapHeight;
		
		width2 = 1;
		while (width2 < pWidth) {
			width2 <<= 1;
		}
		
		height2 = 1;
		while (height2 < pHeight) {
			height2 <<= 1;
		}
		
		float size = DungeonTilemap.SIZE;
		width = width2 * size;
		height = height2 * size;
		
		texture( new FogTexture() );
		
		scale.set( 
			DungeonTilemap.SIZE, 
			DungeonTilemap.SIZE );
		
		updateVisibility();
	}
	
	public void updateVisibility() {
		
		if (pixels == null) {
			pixels = new int[width2 * height2];
		}
		
		for (int i=0; i < pixels.length; i++) {
			if (Level.water[i]) {
				pixels[i] = 0x00000000;
			} else {
				pixels[i] = (Random.Int( 0x22 ) + (Level.solid[i] ? 0x44 : 0x00)) << 24;
			}
		}
		
		texture.pixels( width2, height2, pixels );
	}
	
	private class FogTexture extends SmartTexture {
		
		public FogTexture() {
			super( Bitmap.createBitmap( width2, height2, Bitmap.Config.ARGB_8888 ) );
			filter( Texture.LINEAR, Texture.LINEAR );
			TextureCache.add( LightMap.class, this );
		}
		
		@Override
		public void reload() {
			super.reload();
			updateVisibility();
		}
	}
}
