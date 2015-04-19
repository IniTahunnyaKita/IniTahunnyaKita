package com.molaja.android.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by florianhidayat on 12/4/15.
 */
public class MainTabStack {
    private static List<Integer> stack = new ArrayList<>();
    public static int SHOULD_FINISH_ACTIVITY = -1;

    /**
     * push tab index to top of the stack after popping the index in stack, if any.
     * @param i tab index.
     */
    public static void push(int i) {
        for (Iterator<Integer> iter = stack.listIterator(); iter.hasNext();) {
            int inStack = iter.next();
            if (inStack == i) {
                iter.remove();
            }
        }
        stack.add(i);
    }

    public static int pop() {
        if (stack.size() > 1) {
            stack.remove(stack.size() - 1);
            return stack.get(stack.size() - 1);
        }
        return SHOULD_FINISH_ACTIVITY;
    }

}
