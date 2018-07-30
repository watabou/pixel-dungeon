package com.watabou.pixeldungeon.utils;

import com.watabou.pixeldungeon.windows.WndName;

public class Name {
	
	public static String editString (String [] str, int i, int index){
		if(index<26){
		str[i] = WndName.LETTERS[index]; 
		}
		return str[i];
	}
	
	
}
