/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.scenes;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Point;

public class CellSelector extends TouchArea {

	public Listener listener = null;
	
	public boolean enabled;
	
	private float dragThreshold;

	private boolean pressed;
	private float pressTime;
	private boolean continuous;
	private float continuousThreshold = 0.2f;
	
	public CellSelector( DungeonTilemap map ) {
		super( map );
		camera = map.camera();
		
		dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;
	}
	
	@Override
	protected void onClick( Touch touch ) {
		if (dragging) {
			
			dragging = false;
			
		} else {
			
			select( ((DungeonTilemap)target).screenToTile( 
				(int)touch.current.x, 
				(int)touch.current.y ) );
		}
	}
	
	public void select( int cell ) {
		if (enabled && listener != null && cell != -1) {
			
			listener.onSelect( cell );
			GameScene.ready();
			
		} else {
			
			GameScene.cancel();
			
		}
	}
	
	private boolean pinching = false;
	private Touch another;
	private float startZoom;
	private float startSpan;
	
	@Override
	protected void onTouchDown( Touch t ) {
		continuous = false;

		if (t != touch && another == null) {
					
			if (!touch.down) {
				touch = t;
				onTouchDown( t );
				return;
			}
			
			pinching = true;
			
			another = t;
			startSpan = PointF.distance( touch.current, another.current );
			startZoom = camera.zoom;

			dragging = false;
		} else {
			pressed = true;
		}
	}

	@Override
	public void update () {
		if (!pressed || dragging || pinching)
			return;

		if (!(pressTime >= continuousThreshold)) {
			pressTime += Game.elapsed;
			return;
		}

		continuous = PixelDungeon.continuous();
		if (!continuous)
			return;

		float size = DungeonTilemap.SIZE*camera.zoom;
		PointF p = Dungeon.hero.sprite.worldToCamera( Dungeon.hero.pos );
		Point hero = camera.cameraToScreen (p.x, p.y);
		hero.x += size/2;
		hero.y += size/2;
		float x = Math.abs( touch.current.x - hero.x );
		float y = Math.abs( touch.current.y - hero.y );
		float x2 = 0, y2 = 0;
		if (Math.abs(x) > Math.abs(y)) {
			x2 += size;
			y2 += (y*(x2/x));
		} else {
			y2 += size;
			x2 += (x*(y2/y));
		}

		if (touch.current.x < hero.x)
			x2 *= -1;

		if (touch.current.y < hero.y)
			y2 *= -1;

		int cell = ((DungeonTilemap)target).screenToTile( hero.x + (int)x2, hero.y + (int)y2 );
		if (!Dungeon.level.passable[cell]) {
			int[] neighbours = {Dungeon.hero.pos+1,
					Dungeon.hero.pos+1+Level.WIDTH,
					Dungeon.hero.pos+Level.WIDTH,
					Dungeon.hero.pos-1+Level.WIDTH,
					Dungeon.hero.pos-1,
					Dungeon.hero.pos-1-Level.WIDTH,
					Dungeon.hero.pos-Level.WIDTH,
					Dungeon.hero.pos+1-Level.WIDTH};

			for (int i = 0; i < 8; i++) {
				if (neighbours[i] != cell)
					continue;

				int next = (i+1 >= 8) ? 0 : i+1;
				int prv = (i-1 < 0) ? 7 : i-1;

				if (Dungeon.level.passable[neighbours[next]])
					cell = neighbours[next];
			
				if (Dungeon.level.passable[neighbours[prv]])
					cell = neighbours[prv];
			}
		}

		select( cell );
	}
	
	@Override
	protected void onTouchUp( Touch t ) {
		pressed = false;
		pressTime = 0;
		if (pinching && (t == touch || t == another)) {
			
			pinching = false;
			
			int zoom = Math.round( camera.zoom );
			camera.zoom( zoom );
			PixelDungeon.zoom( (int)(zoom - PixelScene.defaultZoom) );
			
			dragging = true;
			if (t == touch) {
				touch = another;
			}
			another = null;
			lastPos.set( touch.current );
		}
	}	
	
	private boolean dragging = false;
	private PointF lastPos = new PointF();
	
	@Override
	protected void onDrag( Touch t ) {
		 
		if (continuous)
			return;

		camera.target = null;

		if (pinching) {

			float curSpan = PointF.distance( touch.current, another.current );
			camera.zoom( GameMath.gate( 
				PixelScene.minZoom, 
				startZoom * curSpan / startSpan, 
				PixelScene.maxZoom ) );

		} else {
		
			if (!dragging && PointF.distance( t.current, t.start ) > dragThreshold) {
				
				dragging = true;
				lastPos.set( t.current );
				
			} else if (dragging) {
				camera.scroll.offset( PointF.diff( lastPos, t.current ).invScale( camera.zoom ) );
				lastPos.set( t.current );	
			}	
		}
		
	}	
	
	public void cancel() {
		
		if (listener != null) {
			listener.onSelect( null );
		}
		
		GameScene.ready();
	}
	
	public interface Listener {
		void onSelect( Integer cell );
		String prompt();
	}
}
