package com.example.leniad.prueba1.data;

import android.provider.BaseColumns;

/**
 * Created by leniad on 11/16/17.
 */

public class seccionesTablaEsquema {

    public static abstract class seccionesEntry implements BaseColumns {
        public static final String TABLE_NAME ="secciones";

        public static final String ID = "id";
        public static final String SECCION = "seccion";
        public static final String ABREV = "abrev";

    }

}
