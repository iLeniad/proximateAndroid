package com.example.leniad.prueba1.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leniad on 11/16/17.
 */

public class loginDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "proximate.db";

    public loginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + loginTablaEsquema.loginEntry.TABLE_NAME + " ("
                + loginTablaEsquema.loginEntry.ID + " TEXT NOT NULL,"
                + loginTablaEsquema.loginEntry.SUCCESS + " TEXT NOT NULL,"
                + loginTablaEsquema.loginEntry.ERROR + " TEXT NOT NULL,"
                + loginTablaEsquema.loginEntry.MESSAGE + " TEXT NOT NULL,"
                + loginTablaEsquema.loginEntry.TOKEN + " TEXT NOT NULL,"
                + "UNIQUE (" + loginTablaEsquema.loginEntry.ID + "))");


        db.execSQL("CREATE TABLE " + perfilTablaEsquema.perfilEntry.TABLE_NAME + " ("
                + perfilTablaEsquema.perfilEntry.ID + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.NOMBRES + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.APELLIDOS + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.CORREO + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.NUMERO_DOCUMENTO + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.ULTIMA_SESION + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.ELIMINADO + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.DOCUMENTOS_ID + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.DOCUMENTOS_ABREV + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.DOCUMENTOS_LABEL + " TEXT NOT NULL,"
                + perfilTablaEsquema.perfilEntry.ESTADOS_USUARIOS_LABEL + " TEXT NOT NULL,"
                + "UNIQUE (" + perfilTablaEsquema.perfilEntry.ID + "))");


        db.execSQL("CREATE TABLE " + seccionesTablaEsquema.seccionesEntry.TABLE_NAME + " ("
                + seccionesTablaEsquema.seccionesEntry.ID + " TEXT NOT NULL,"
                + seccionesTablaEsquema.seccionesEntry.SECCION + " TEXT NOT NULL,"
                + seccionesTablaEsquema.seccionesEntry.ABREV + " TEXT NOT NULL,"
                + "UNIQUE (" + seccionesTablaEsquema.seccionesEntry.ID + "))");



    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public boolean insertar(String sql){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.execSQL(sql);

        return true;
    }

    public Cursor query(String sql){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();



        return sqLiteDatabase.rawQuery(sql,null);
    }


    public long salvarLogin(loginTabla logintabla) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                loginTablaEsquema.loginEntry.TABLE_NAME,
                null,
                logintabla.toContentValues());

    }

    public Cursor getAllLoginTabla() {
        return getReadableDatabase()
                .query(
                        loginTablaEsquema.loginEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }






}

