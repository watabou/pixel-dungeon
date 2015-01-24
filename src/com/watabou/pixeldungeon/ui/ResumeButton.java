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
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;

public class ResumeButton extends Tag {
	
	private Image icon;
	
	public ResumeButton() {
		super( 0xCDD5C0 );
		
		setSize( 24, 22 );
		
		visible = false;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		icon = Icons.get( Icons.RESUME );
		add( icon );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = PixelScene.align( PixelScene.uiCamera, x+1 + (width - icon.width) / 2 );
		icon.y = PixelScene.align( PixelScene.uiCamera, y + (height - icon.height) / 2 );
	}
	
	@Override
	public void update() {
		boolean prevVisible = visible;
		visible = (Dungeon.hero.lastAction != null);
		if (visible && !prevVisible) {
			flash();
		}

		super.update();
	}
	
	@Override
	protected void onClick() {
		Dungeon.hero.resume();
	}
}
