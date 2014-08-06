package com.watabou.pixeldungeon.windows;

import java.util.List;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;

public class WndLog extends Window {
	
	private static final int WIDTH	= 112;
	private static final int HEIGHT	= 160;
	
	private static final String TXT_TITLE	= "Log";
	
	private BitmapText txtTitle;

	public WndLog(List<BitmapTextMultiline> entries) {
		
		super();
		resize( WIDTH, HEIGHT );
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
		add( txtTitle );
		
		float pos = txtTitle.height();
		Component content = new Component();
		for (int i = entries.size()-1; i >= 0; i--) {
			BitmapTextMultiline entry = entries.get( i );
			BitmapTextMultiline text = PixelScene.createMultiline( entry.text(), 6 );
			text.maxWidth = entry.maxWidth;
			text.measure();
			text.hardlight( entry.rm, entry.gm, entry.bm );
			content.add (text);
			text.x = 0;
			text.y = pos;
			pos += entry.height();
		}

		content.setSize ( WIDTH, pos );
		
		ScrollPane list = new ScrollPane( content );
		add( list );
		
		list.setRect( 0, txtTitle.height(), WIDTH, HEIGHT - txtTitle.height() );
	}
}
