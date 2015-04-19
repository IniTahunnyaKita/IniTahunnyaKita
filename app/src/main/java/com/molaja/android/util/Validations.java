package com.molaja.android.util;

import java.util.List;

/**
 * Created by florianhidayat on 18/4/15.
 */
public class Validations {

    public static boolean isEmptyOrNull(String s) {
        if(s == null) {
            return true;
        } else {
            return s.isEmpty();
        }
    }

    public static boolean isEmptyOrNull(Object[] o) {
        if (o == null) {
            return true;
        } else {
            return o.length == 0;
        }
    }

    public static boolean isEmptyOrNull(List<Object> list) {
        if (list == null) {
            return true;
        } else {
            return list.isEmpty();
        }
    }

}
