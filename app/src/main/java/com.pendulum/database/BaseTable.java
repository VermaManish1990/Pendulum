/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 *//*


package com.pendulum.database;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taxi.application.BaseApplication;
import com.taxi.model.BaseModel;

import java.util.ArrayList;

*/
/**
 * base class with common DB methods to be used as super class for all tables
 *
 * @author sachin.gupta
 *//*

public abstract class BaseTable {

    protected static final String CN_PRIMARY_KEY = "primary_key";

    protected SQLiteDatabase mWritableDatabase;
    protected String mTableName;

    */
/**
     * Get the global instance of the SQLiteDatabase.
     *//*

    private BaseTable() {
    }

    public BaseTable(Application pApplication, String pTableName) {
        if (pApplication instanceof BaseApplication) {
            BaseApplication baseApplication = (BaseApplication) pApplication;
            mWritableDatabase = baseApplication.getBaseDbHelper().getWritableDbInstance();
        } else {
            throw new RuntimeException("BaseApplication implementation is wrong.");
        }
        mTableName = pTableName;
    }

    */
/**
     * @param pModel
     * @return the rowId of newly inserted row, 0 in case of any error
     *//*

    public final long insertData(BaseModel pModel) {
        try {
            return mWritableDatabase.insert(mTableName, null, getContentValues(pModel, false));
        } catch (Exception e) {
            return 0;
        }
    }

    */
/**
     * inserts or updates data by primary key
     *
     * @param pModel
     * @return
     *//*

    public final boolean insertOrUpdate(BaseModel pModel) {
        BaseModel existingModel = getMatchingData(pModel);
        if (existingModel == null) {
            return insertData(pModel) > 0;
        } else {
            pModel.setPrimaryKey(existingModel.getPrimaryKey());
            return updateData(pModel) > 0;
        }
    }

    */
/**
     * delete data by whereClause and whereArgs
     *
     * @param pWhereClause
     * @param pWhereArgs
     * @return count of deleted rows
     *//*

    protected final int deleteData(String pWhereClause, String[] pWhereArgs) {
        try {
            return mWritableDatabase.delete(mTableName, pWhereClause, pWhereArgs);
        } catch (Exception e) {
            return 0;
        }
    }

    */
/**
     * delete data by primary key
     *
     * @param pPrimaryKey
     * @return true if one or more rows are deleted
     *//*

    public final boolean deleteData(int pPrimaryKey) {
        String whereClause = CN_PRIMARY_KEY + " = ?";
        String[] whereArgs = {"" + pPrimaryKey};
        return deleteData(whereClause, whereArgs) > 0;
    }

    */
/**
     * update data by whereClause and whereArgs
     *
     * @param contentValues
     * @param whereClause
     * @param whereArgs
     * @return count of affected rows
     *//*

    protected final int updateData(ContentValues contentValues, String whereClause, String[] whereArgs) {
        try {
            return mWritableDatabase.update(mTableName, contentValues, whereClause, whereArgs);
        } catch (Exception e) {
            return 0;
        }
    }

    */
/**
     * update data by primary key
     *
     * @param baseModel
     * @return count of affected rows
     *//*

    public final int updateData(BaseModel baseModel) {
        String whereClause = CN_PRIMARY_KEY + " = ?";
        String[] whereArgs = {"" + baseModel.getPrimaryKey()};
        return updateData(getContentValues(baseModel, true), whereClause, whereArgs);
    }

    */
/**
     * @return array list of all data in table
     *//*

    public final ArrayList<BaseModel> getAllData() {
        return getAllData(null, null);
    }

    */
/**
     * @return the number of rows deleted
     *//*

    public final int deleteAll() {
        return deleteData("1", null);
    }

    */
/**
     * @param pSelection     or null to get count of all rows in table
     * @param pSelectionArgs optional
     * @return count of selected rows in table, -1 in case of any exception.
     *//*

    public final int getRowsCount(String pSelection, String[] pSelectionArgs) {
        String columnName = "rowsCount";
        String query = "select count(*) as " + columnName + "  from " + mTableName;
        if (pSelection != null) {
            query += " where " + pSelection;
        }
        int rowsCount = -1;
        Cursor cursor = null;
        try {
            cursor = mWritableDatabase.rawQuery(query, pSelectionArgs);
            if (cursor.moveToNext()) {
                rowsCount = cursor.getInt(cursor.getColumnIndex(columnName));
            }
        } catch (Exception e) {
        } finally {
            closeCursor(cursor);
        }
        return rowsCount;
    }

    */
/**
     * Closes the pCursor.
     *
     * @param pCursor
     *//*

    protected final void closeCursor(Cursor pCursor) {
        if (pCursor != null && !pCursor.isClosed())
            pCursor.close();
    }

    */
/**
     * Helper method to create content value from BaseModel
     *
     * @param pModel
     * @param onlyUpdates
     * @return
     *//*

    protected abstract ContentValues getContentValues(BaseModel pModel, boolean onlyUpdates);

    */
/**
     * @param pSelection
     * @param pSelectionArgs
     * @return array list of data selected from table
     *//*

    protected abstract ArrayList<BaseModel> getAllData(String pSelection, String[] pSelectionArgs);

    */
/**
     * @param pModel
     * @return
     *//*

    protected BaseModel getMatchingData(BaseModel pModel) {
        throw new UnsupportedOperationException("Operation not implemented yet.");
    }
}
*/
