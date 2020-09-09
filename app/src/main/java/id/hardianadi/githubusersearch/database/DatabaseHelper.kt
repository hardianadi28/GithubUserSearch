package id.hardianadi.githubusersearch.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */
internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "githubuser.db"
        private const val DATABASE_VERSION = 2
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE ${GithubUserColumns.TABLE_NAME}" +
                " (${GithubUserColumns._ID} INTEGER PRIMARY KEY," +
                " ${GithubUserColumns.LOGIN} TEXT NOT NULL," +
                " ${GithubUserColumns.COMPANY} TEXT," +
                " ${GithubUserColumns.AVATAR_URL} TEXT," +
                " ${GithubUserColumns.HTML_URL} TEXT NOT NULL," +
                " ${GithubUserColumns.NAME} TEXT," +
                " ${GithubUserColumns.LOCATION} TEXT," +
                " ${GithubUserColumns.PUBLIC_REPOS} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${GithubUserColumns.TABLE_NAME}")
        onCreate(db)
    }

}