package com.example.leniad.prueba1.data;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by leniad on 11/16/17.
 */

public class loginTabla {
    private String success;
    private String error;
    private String message;
    private String token;
    private String id;


    public loginTabla(String success,
                      String error, String message,
                      String token, String id) {
        this.success = success;
        this.error = error;
        this.message = message;
        this.token = token;
        this.id = id;

    }

    public loginTabla(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(loginTablaEsquema.loginEntry.ID));
        success = cursor.getString(cursor.getColumnIndex(loginTablaEsquema.loginEntry.SUCCESS));
        error = cursor.getString(cursor.getColumnIndex(loginTablaEsquema.loginEntry.ERROR));
        message = cursor.getString(cursor.getColumnIndex(loginTablaEsquema.loginEntry.MESSAGE));
        token = cursor.getString(cursor.getColumnIndex(loginTablaEsquema.loginEntry.TOKEN));

    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(loginTablaEsquema.loginEntry.ID, id);
        values.put(loginTablaEsquema.loginEntry.SUCCESS, success);
        values.put(loginTablaEsquema.loginEntry.ERROR, error);
        values.put(loginTablaEsquema.loginEntry.MESSAGE, message);
        values.put(loginTablaEsquema.loginEntry.TOKEN, token);

        return values;
    }

    public String getId() {
        return id;
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

}

