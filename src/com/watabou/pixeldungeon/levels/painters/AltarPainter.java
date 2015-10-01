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
package com.watabou.pixeldungeon.levels.painters;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.SacrificialFire;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class AltarPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Dungeon.bossLevel( Dungeon.depth + 1 ) ? Terrain.HIGH_GRASS : Terrain.CHASM );
		
		Point c = room.center();
		Room.Door door = room.entrance();
		if (door.x == room.left || door.x == room.right) {
			Point p = drawInside( level, room, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
			for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
				set( level, p, Terrain.EMPTY_SP );
			}
		} else {
			Point p = drawInside( level, room, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
			for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
				set( level, p, Terrain.EMPTY_SP );
			}
		}

		fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMBERS );
		set( level, c, Terrain.PEDESTAL );
		
		SacrificialFire fire = (SacrificialFire)level.blobs.get( SacrificialFire.class );
		if (fire == null) {
			fire = new SacrificialFire();
		}
		fire.seed( c.x + c.y * Level.WIDTH, 5 + Dungeon.depth * 5 );
		level.blobs.put( SacrificialFire.class, fire );
		
		door.set( Room.Door.Type.EMPTY );
	}
}
