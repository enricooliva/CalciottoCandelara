package com.enrico.calciottocandelara.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {

     // The URI Matcher used by this content provider.
     private static final UriMatcher sUriMatcher = buildUriMatcher();
     private DataDbHelper mOpenHelper;

     private static final int PLAYER = 100;
     private static final int PLAYER_ID = 101;

     private static final int GAME = 200;
     private static final int GAME_ID = 201;

     private static UriMatcher buildUriMatcher() {

         final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
         final String authority = DataContract.CONTENT_AUTHORITY;

         // For each type of URI you want to add, create a corresponding code.
         matcher.addURI(authority, DataContract.PATH_PLAYER, PLAYER);
         matcher.addURI(authority, DataContract.PATH_PLAYER + "/#", PLAYER_ID);
         //matcher.addURI(authority, DataContract.PATH_PLAYER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);

         matcher.addURI(authority, DataContract.PATH_GAME, GAME);
         matcher.addURI(authority, DataContract.PATH_GAME + "/#", GAME_ID);

         return matcher;
     }



    @Override
     public boolean onCreate() {
         mOpenHelper = new DataDbHelper(getContext());
         return true;
     }

     @Override
     public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder) {
         // Here's the switch statement that, given a URI, will determine what kind of request it is,
         // and query the database accordingly.
         Cursor retCursor;
         switch (sUriMatcher.match(uri)) {
             // "weather/*/*"
             case PLAYER:
             {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.PlayerEntry.TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );
                 break;
             }
             // "weather/*"
             case PLAYER_ID: {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.PlayerEntry.TABLE_NAME,
                         projection,
                         DataContract.PlayerEntry._ID + "=" + ContentUris.parseId(uri),
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );

                 break;
             }

             case GAME: {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.GameEntry.TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );
                 break;
             }

             case GAME_ID: {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.GameEntry.TABLE_NAME,
                         projection,
                         DataContract.GameEntry._ID+"="+ ContentUris.parseId(uri),
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );
                 break;

             }


             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         retCursor.setNotificationUri(getContext().getContentResolver(), uri);
         return retCursor;
     }

     @Override
     public String getType(Uri uri) {

         // Use the Uri Matcher to determine what kind of URI this is.
         final int match = sUriMatcher.match(uri);

         switch (match) {
             case PLAYER_ID:
                 return DataContract.PlayerEntry.CONTENT_ITEM_TYPE;
             case PLAYER:
                 return DataContract.PlayerEntry.CONTENT_TYPE;
             case GAME:
                 return DataContract.GameEntry.CONTENT_TYPE;
             case GAME_ID:
                 return DataContract.GameEntry.CONTENT_ITEM_TYPE;

             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
     }

     @Override
     public Uri insert(Uri uri, ContentValues values) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         Uri returnUri;

         switch (match) {
             case PLAYER: {
                 long _id = db.insert(DataContract.PlayerEntry.TABLE_NAME, null, values);
                 if ( _id > 0 )
                     returnUri = DataContract.PlayerEntry.buildPlayerUri(_id);
                 else
                     throw new android.database.SQLException("Failed to insert row into " + uri);
                 break;
             }
             case GAME: {
                 long _id = db.insert(DataContract.GameEntry.TABLE_NAME, null, values);
                 if ( _id > 0 )
                     returnUri = DataContract.GameEntry.buildGameUri(_id);
                 else
                     throw new android.database.SQLException("Failed to insert row into " + uri);
                 break;
             }
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         getContext().getContentResolver().notifyChange(uri, null);
         return returnUri;
     }

     @Override
     public int delete(Uri uri, String selection, String[] selectionArgs) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsDeleted;
         switch (match) {
             case PLAYER:
                 rowsDeleted = db.delete(
                         DataContract.PlayerEntry.TABLE_NAME, selection, selectionArgs);
                 break;
             case GAME:
                 rowsDeleted = db.delete(
                         DataContract.GameEntry.TABLE_NAME, selection, selectionArgs);
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         // Because a null deletes all rows
         if (selection == null || rowsDeleted != 0) {
             getContext().getContentResolver().notifyChange(uri, null);
         }
         return rowsDeleted;
     }

     @Override
     public int update(
             Uri uri, ContentValues values, String selection, String[] selectionArgs) {

         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsUpdated;
         switch (match) {
             case PLAYER:
                 rowsUpdated = db.update(
                         DataContract.PlayerEntry.TABLE_NAME, values, selection, selectionArgs);
                 break;
             case GAME:
                 rowsUpdated = db.update(
                         DataContract.GameEntry.TABLE_NAME, values, selection, selectionArgs);
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         // Because a null deletes all rows
         if (selection == null || rowsUpdated != 0) {
             getContext().getContentResolver().notifyChange(uri, null);
         }
         return rowsUpdated;

     }

     @Override
     public int bulkInsert(Uri uri, ContentValues[] values) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         switch (match) {
             case PLAYER:
                 db.beginTransaction();
                 int returnCount = 0;
                 try {
                     for (ContentValues value : values) {
                         long _id = db.insert(DataContract.PlayerEntry.TABLE_NAME, null, value);
                         if (_id != -1) {
                             returnCount++;
                         }
                     }
                     db.setTransactionSuccessful();
                 } finally {
                     db.endTransaction();
                 }
                 getContext().getContentResolver().notifyChange(uri, null);

                 return returnCount;

             case GAME:
                 db.beginTransaction();
                 int returnCount1 = 0;
                 try {
                     for (ContentValues value : values) {
                         long _id = db.insert(DataContract.GameEntry.TABLE_NAME, null, value);
                         if (_id != -1) {
                             returnCount1++;
                         }
                     }
                     db.setTransactionSuccessful();
                 } finally {
                     db.endTransaction();
                 }
                 getContext().getContentResolver().notifyChange(uri, null);
                 return returnCount1;


             default:
                 return super.bulkInsert(uri, values);
         }
     }

}

