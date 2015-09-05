Translating Pixel Dungeon
=============

> **It was a great mission, but with much effort, all texts, phrases, words and other strings were copied all the source code of the game and grouped into a single file. The ```string.xml``` file.**

By grouping all the strings of the game, it becomes possible to take advantage of translation support android as described in [Andoid Guide](http://developer.android.com/training/basics/supporting-devices/languages.html).

#####To translate the game you simply do the following:

1. Copy the contents of the folder ```/res/values``` to a new directory with the corresponding site to be translated as ```/res/values-fr``` for a translation into French.
2. After making the copy, simply translating the content of all tags within the ```strings.xml``` file in your new directory.
3. After that, please test your game translated.

**NOTE:** Currently, the font files have the characters below. any character other than those below, will not appear in the game. following valid characters:
```
 !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007FàáâäãèéêëìíîïòóôöõùúûüñçÀÁÂÄÃÈÉÊËÌÍÎÏÒÓÔÖÕÙÚÛÜÑÇº
```

**Good Luck**