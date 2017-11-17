package com.example.leniad.prueba1.data;

import android.provider.BaseColumns;

/**
 * Created by leniad on 11/16/17.
 */

public class loginTablaEsquema {

    public static abstract class loginEntry implements BaseColumns {
        public static final String TABLE_NAME ="login";

        public static final String ID = "id";
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String MESSAGE = "message";
        public static final String TOKEN = "token";

    }
}