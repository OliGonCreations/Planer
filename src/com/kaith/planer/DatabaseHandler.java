package com.kaith.planer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "eventManager";

	// Table name
	private static final String TABLE_EVENT = "event";

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_BEGINNING = "beginning";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_DAY = "day";

	DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_EVENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_BEGINNING
				+ " INTEGER," + KEY_LENGTH + " INTEGER," + KEY_DAY + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		onCreate(db);
	}

	void addVeranstaltung(Veranstaltung veranstaltung) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, veranstaltung.getName());
		values.put(KEY_DESCRIPTION, veranstaltung.getDescription());
		values.put(KEY_BEGINNING, veranstaltung.getBeginning());
		values.put(KEY_LENGTH, veranstaltung.getLength());
		values.put(KEY_DAY, veranstaltung.getDay());
		db.insert(TABLE_EVENT, null, values);
		db.close();
	}

	public String[] getEvent(int btId, int day) {
		String[] bundle = new String[2];
		String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE " + KEY_BEGINNING + " LIKE '" + btId + "' AND " + KEY_DAY + " LIKE '" + day + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				bundle[0] = cursor.getString(1);
				bundle[1] = cursor.getString(2);
			} while (cursor.moveToNext());
		}
		return bundle;
	}

	public void deleteEvent(int beginning, int day) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENT, KEY_BEGINNING + "=" + beginning + " AND " + KEY_DAY + "=" + day, null);
		db.close();
	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENT, null, null);
		db.close();
	}
	
	public String getAll() {
		String bundle = "";
//		String[] day = new String[14];
		String selectQueryMon = "SELECT * FROM " + TABLE_EVENT + " WHERE " + KEY_DAY + " = 2";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQueryMon, null);
		if (cursor.moveToFirst()) {
			do {
				
			} while (cursor.moveToNext());
		}
		return bundle;
	}
}
