package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.utils.Name;
import com.watabou.pixeldungeon.scenes.StartScene;

public class WndName extends Window {
	
	private static final int WIDTH			= 120;
	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 15;
	public static final String [] LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ok", "<-"};
	private static final char [] HERONAME = {'_', '_', '_', '_', '_', '_', '_', '_', '_', '_'};
	public static String heroName = new String(HERONAME);
	
	private static int j = 0;
	public WndName(){
		super();
                
		BitmapTextMultiline tfTitle = PixelScene.createMultiline( "Scegli il nome del tuo eroe", 9 );
		tfTitle.hardlight( TITLE_COLOR );
		tfTitle.x = tfTitle.y = MARGIN;
		tfTitle.maxWidth = WIDTH - MARGIN * 2;
		tfTitle.measure();
		add( tfTitle );
		
		
		BitmapTextMultiline text = PixelScene.createMultiline(heroName, 8);
		text.x = MARGIN;
		text.y = tfTitle.y + tfTitle.height() + MARGIN;
		text.maxWidth = WIDTH - MARGIN * 2;
		text.measure();
		add( text );
				
		float pos = text.y + text.height() + MARGIN;
		int k = 0;
		
		for(int i = 0; i < 28; i++){
		final int index = i;
		RedButton btn = new RedButton(LETTERS[i]){
			@Override
			protected void onClick(){
				
				onSelect(index);
                                text.destroy();
                                text.text(heroName);
                                add(text);
				
			}
		};
		
		j = i - k; 
		int a = MARGIN + (j * 15);
		int b = a + 15;
		btn.setRect( a , pos, (b - MARGIN * 2)/(j+1), BUTTON_HEIGHT );
		add( btn );
		if((b+ 15)>WIDTH){
		pos += BUTTON_HEIGHT + MARGIN;
		k = i + 1;
		}
		}
		resize( WIDTH - 15 , (int)pos );
	}
	

	
	protected void onSelect(int index){
		if (LETTERS[index].equals("ok"))
                {
                    StartScene.startNewGame();
                }
                else if (LETTERS[index].equals("<-"))
                {
                    heroName = heroName.concat("\b_");
                }
                else 
                {
                    heroName = heroName.concat("\b" + LETTERS[index]);
                }
	}
	
	
}
