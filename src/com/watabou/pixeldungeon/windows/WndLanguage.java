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

import java.util.ArrayList;

import com.watabou.pixeldungeon.R;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;

public class WndLanguage extends Window {
   private static final int WIDTH_P     = 120;
   private static final int HEIGHT_P    = 160;

   private static final int WIDTH_L     = 128;
   private static final int HEIGHT_L    = 128;

   private static final int ITEM_HEIGHT = 18;

   private static final String TXT_TITLE  = Game.getVar(R.string.WndLanguage_Title);
   private static final String[] LANGS    = Game.getVars(R.array.WndLanguage_Langs);

   private ArrayList<ListItem> items = new ArrayList<WndLanguage.ListItem>();

   public WndLanguage() {
      super();

      if (PixelDungeon.landscape()) {
         resize( WIDTH_L, HEIGHT_L );
      } else {
         resize(WIDTH_P, HEIGHT_P);
      }

      BitmapText txtTitle = PixelScene.createText(TXT_TITLE, 9);
      txtTitle.hardlight(Window.TITLE_COLOR);
      txtTitle.measure();
      txtTitle.x = PixelScene.align(PixelScene.uiCamera,
            (width - txtTitle.width()) / 2);
      add(txtTitle);

      ScrollPane list = new ScrollPane(new Component()) {
         @Override
         public void onClick(float x, float y) {
            int size = items.size();
            for (int i = 0; i < size; i++) {
               if (items.get(i).onClick(x, y)) {
                  hide();
                  break;
               }
            }
         }
      };

      add(list);
      list.setRect(0, txtTitle.height() + 2, width, height - txtTitle.height() - 2);
      Component content = list.content();

      int pos = 0;
      for (String lang : LANGS) {

         ListItem.Status sta = ListItem.Status.NORMAL;
         if (lang.split(",")[1].equals(PixelDungeon.language())) {
            sta = ListItem.Status.SELECTED;
         } else if (lang.split(",")[2].equals("0")){
            sta = ListItem.Status.INACTIVE;
         }

         ListItem item = new ListItem(lang.split(",")[0], lang.split(",")[1], sta);
         item.setRect(0, pos, width, ITEM_HEIGHT);
         content.add(item);
         items.add(item);

         pos += item.height();
      }

      content.setSize(width, pos);
   }

   private static class ListItem extends Component {

      private BitmapText lang;
      private String value;
      private Status status;
      private Image icon;

      public enum Status {
         NORMAL, SELECTED, INACTIVE
      }

      public ListItem(String text, String value, Status sta) {
         super();

         lang.text(text);
         lang.measure();

         this.value = value;
         this.status = sta;

         if (sta == Status.SELECTED) {
            lang.hardlight(TITLE_COLOR);
         } else if (sta == Status.INACTIVE) {
            lang.hardlight(0xBBBBBB);
         }
      }

      @Override
      protected void createChildren() {
         lang = PixelScene.createText(9);
         add(lang);

         icon = Icons.get(Icons.MASTERY);
         add(icon);
      }

      @Override
      protected void layout() {
         lang.x = icon.width + 2;
         lang.y = PixelScene.align(y + (height - lang.baseLine()) / 2) + 2;

         icon.y = lang.y - 3;
      }

      public boolean onClick(float x, float y) {
         if (inside(x, y)) {
            if (status == Status.NORMAL){
               PixelDungeon.language(value);
            }
            return true;
         } else {
            return false;
         }
      }
   }

}
