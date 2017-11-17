package com.example.leniad.prueba1.data;

import android.provider.BaseColumns;

/**
 * Created by leniad on 11/16/17.
 */

public class perfilTablaEsquema {

    public static abstract class perfilEntry implements BaseColumns {
        public static final String TABLE_NAME ="perfil";

        public static final String ID = "id";
        public static final String NOMBRES = "nombres";
        public static final String APELLIDOS = "apellidos";
        public static final String CORREO = "correo";
        public static final String NUMERO_DOCUMENTO = "numero_documento";
        public static final String ULTIMA_SESION = "ultima_sesion";
        public static final String ELIMINADO = "eliminado";
        public static final String DOCUMENTOS_ID = "documentos_id";
        public static final String DOCUMENTOS_ABREV = "documentos_abrev";
        public static final String DOCUMENTOS_LABEL = "documentos_label";
        public static final String ESTADOS_USUARIOS_LABEL = "estados_usuarios_label";

    }

}
