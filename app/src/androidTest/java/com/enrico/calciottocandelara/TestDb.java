package com.enrico.calciottocandelara;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.enrico.calciottocandelara.data.DataContract.PlayerEntry;
import com.enrico.calciottocandelara.data.DataDbHelper;

/**
 * Created by Enrico on 06/04/2015.
 */
    public class TestDb extends AndroidTestCase {

        public static final String LOG_TAG = TestDb.class.getSimpleName();

        public void testCreateDb() throws Throwable {
            mContext.deleteDatabase(DataDbHelper.DATABASE_NAME);
            SQLiteDatabase db = new DataDbHelper(
                    this.mContext).getWritableDatabase();
            assertEquals(true, db.isOpen());
            db.close();
        }

        public void testInsertReadDb() {


            // If there's an error in those massive SQL table creation Strings,
            // errors will be thrown here when you try to get a writable database.
            DataDbHelper dbHelper = new DataDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(PlayerEntry.COLUMN_NAME, "Enrico");
            values.put(PlayerEntry.COLUMN_NUMBER, 10);
            values.put(PlayerEntry.COLUMN_ROLE, "Attacante");
            values.put(PlayerEntry.COLUMN_RATE, 10);

            long playerRowId;
            playerRowId = db.insert(PlayerEntry.TABLE_NAME, null, values);

            // Verify we got a row back.
            assertTrue(playerRowId != -1);
            Log.d(LOG_TAG, "New row id: " + playerRowId);

            // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
            // the round trip.

            // Specify which columns you want.
            String[] columns = {
                    PlayerEntry._ID,
                    PlayerEntry.COLUMN_NAME,
                    PlayerEntry.COLUMN_NUMBER,
                    PlayerEntry.COLUMN_RATE,
                    PlayerEntry.COLUMN_ROLE
            };

            // A cursor is your primary interface to the query results.
            Cursor cursor = db.query(
                    PlayerEntry.TABLE_NAME,  // Table to Query
                    columns,
                    null, // Columns for the "where" clause
                    null, // Values for the "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    null // sort order
            );

            // If possible, move to the first row of the query results.
            if (cursor.moveToFirst()) {
                // Get the value in each column by finding the appropriate column index.
                int nameIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_NAME);
                String name = cursor.getString(nameIndex);

                int numberIndex = cursor.getColumnIndex((PlayerEntry.COLUMN_NUMBER));
                int number = cursor.getInt(numberIndex);

                int roleIndex = cursor.getColumnIndex((PlayerEntry.COLUMN_ROLE));
                String role = cursor.getString(roleIndex);

                int rateIndex = cursor.getColumnIndex((PlayerEntry.COLUMN_RATE));
                double rate = cursor.getDouble(rateIndex);

                // Hooray, data was returned!  Assert that it's the right data, and that the database
                // creation code is working as intended.
                // Then take a break.  We both know that wasn't easy.
                assertEquals("Enrico", name);
                assertEquals(10, number);
                assertEquals("Attacante", role);
                assertEquals(10, rate);

                // Fantastic.  Now that we have a location, add some weather!
            } else {
                // That's weird, it works on MY machine...
                fail("No values returned :(");
            }

            dbHelper.close();
        }

}
