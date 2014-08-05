package com.watabou.pixeldungeon.ui;

import com.watabou.utils.PointF;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;

public class Joystick extends Component
{
	public static float longClick = 1f;

	protected float pressTime;
	protected boolean pressed;
	protected boolean draging;

	protected TouchArea hotArea;

	protected Image bg;

	protected PointF current;

	public Joystick () {
		bg = new Image (Assets.JOYSTICK);
		add( bg );
		setSize (30, 30);
	}
	
	@Override
	protected void createChildren() {
		hotArea = new TouchArea( 0, 0, 0, 0 ) {
			protected void onDrag( Touch touch ) {
				pressTime = 0;
				PointF p = camera().screenToCamera( (int)touch.current.x, (int)touch.current.y );
				if (draging)
					setPos (p.x-15, p.y-15);
				else {
					if (p.x > left()+(width()/3) && p.x < right()-(width()/3)
						&& p.y < bottom()-(height()/3) && p.y > top()+(height()/3))
						pressed = true;
					else {
						pressed = false;
						current = p;
					}
				}
			}

			protected void onTouchDown( Touch touch ) {
				onDrag (touch);
			}
			
			protected void onTouchUp( Touch touch ) {
				pressed = false;
				draging = false;
				current = null;
				bg.x = x;
				bg.y = y;
			}

		};
		add( hotArea );
	}
	
	@Override
	public void update() {
		super.update();
		
		hotArea.active = visible;

		if (current != null)
			step (current);

		if (pressed) {
			if ((pressTime += Game.elapsed) >= longClick) {
				pressed = false;
				draging = true;
				Game.vibrate( 50 );
			}
		}
	}
		
	protected boolean onLongClick() {
		return false;
	};
	
	@Override
	protected void layout() {
		hotArea.x = x;
		hotArea.y = y;
		hotArea.width = width;
		hotArea.height = height;

		bg.x = x;
		bg.y = y;
	}

	private void step (PointF p) {
		if (p.x > left()+(width()/3) && p.x < right()-(width()/3)) {
			if (p.y < top()+(height()/3)) {
				bg.x = x;
				bg.y = y-1;
				GameScene.handleCell (Dungeon.hero.pos-Level.WIDTH);
			} else if (p.y > bottom()-(height()/3)) {
				bg.x = x;
				bg.y = y+1;
				GameScene.handleCell (Dungeon.hero.pos+Level.WIDTH);
			}
		} else if (p.y < bottom()-(height()/3) && p.y > top()+(height()/3)) {
			if (p.x < left()+(width()/3)) {
				bg.y = y;
				bg.x = x-1;
				GameScene.handleCell (Dungeon.hero.pos-1);
			} else if (p.x > right()-(width()/3)) {
				bg.y = y;
				bg.x = x+1;
				GameScene.handleCell (Dungeon.hero.pos+1);
			}
		}
	}
}