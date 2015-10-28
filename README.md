Pixel Dungeon
=============

Traditional roguelike game with pixel-art graphics and simple interface.

Pixel Dungeon on GooglePlay: 
https://play.google.com/store/apps/details?id=br.com.rodriformiga.pixeldungeon

Official web-site: 
http://pixeldungeon.watabou.ru/

Developer's Official blog: 
http://pixeldungeon.tumblr.com/

To build the game you will need my unnamed game library:
https://github.com/rodriformiga/PD-classes



Translating Pixel Dungeon
=============

> **It was a great mission, but with much effort, all texts, phrases, words and other strings were copied all the source code of the game and grouped into a single file. The ```string.xml``` file.**

By grouping all the strings of the game, it becomes possible to take advantage of translation support android as described in [Andoid Guide](http://developer.android.com/training/basics/supporting-devices/languages.html).


-----
#####To translate the game in [Transifex](https://www.transifex.com/pixel-dungeon/pixel-1/)

* You can make or help translate the online game, accessed the website:
  * https://www.transifex.com/pixel-dungeon/pixel-1/
* It is very simple to use, especially for those without programming skills. If you have questions on how to use the program or want me to generate a version for testing, you can contact me via e-mail rodriformiga@gmail.com
* When a translation is done into any language, the translation is downloaded, reviewed and compiled, and a new Pixel version is released. This normally occurs once every 2 months.


-----
#####To translate the game in Eclipse:

1. Copy the contents of the folder ```/res/values``` to a new directory with the corresponding site to be translated like as ```/res/values-fr``` for a translation into French.
2. After making the copy, simply translating the content of all tags within the ```strings.xml``` file in your new directory.
3. After that, please test your game translated. :wink::video_game:

-----

**NOTE:** Currently, the font files have the characters below. Any character other than those below, will _**not appear in the game**_.
Following valid characters:
```
 !¡\"#$%&'()*+,-./0123456789:;<=>?¿@
ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`
abcdefghijklmnopqrstuvwxyz{|}~\u007F
àáâäãąèéêëęìíîïòóôöõùúûüñńçćłśźż
ÀÁÂÄÃĄÈÉÊËĘÌÍÎÏÒÓÔÖÕÙÚÛÜÑŃÇĆŁŚŹŻº
```


**Help us make the Pixel Dungeon best and join the translations of the game**