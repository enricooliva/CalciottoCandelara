/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.enrico.calciottocandelara.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.enrico.calciottocandelara.data.DataContract.PlayerEntry;
import com.enrico.calciottocandelara.data.DataContract.GameEntry;


public class DataDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "footbal.db";

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlayerEntry.COLUMN_NAME  + " TEXT NOT NULL, " +
                PlayerEntry.COLUMN_NUMBER  + " INT NOT NULL, " +
                PlayerEntry.COLUMN_RATE  + " REAL NOT NULL, " +
                PlayerEntry.COLUMN_ROLE  + " TEXT NOT NULL, " +
                PlayerEntry.COLUMN_NUMBER_VOTE  + " INT NOT NULL );";



        final String SQL_CREATE_GAME_TABLE = "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                GameEntry.COLUMN_GAME + " TEXT NOT NULL, " +
                GameEntry.COLUMN_RESULT + " TEXT NOT NULL, " +
                GameEntry.COLUMN_VICTORY + " BOOLEAN NOT NULL, " +
                GameEntry.COLUMN_GOALS + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_PLAYER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GAME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
