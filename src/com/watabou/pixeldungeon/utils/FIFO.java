package com.watabou.pixeldungeon.utils;

import java.util.LinkedList;

/**
 * Created by shaia on 5/9/15.
 */
public class FIFO<E> extends LinkedList<E> {

    private int limit;

    public FIFO(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
