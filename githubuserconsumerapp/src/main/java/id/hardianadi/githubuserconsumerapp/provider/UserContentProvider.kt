package id.hardianadi.githubuserconsumerapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import id.hardianadi.githubuserconsumerapp.database.DatabaseGithubUser.AUTHORITY
import id.hardianadi.githubuserconsumerapp.database.DatabaseGithubUser.GithubUserColumns.Companion.CONTENT_URI
import id.hardianadi.githubuserconsumerapp.database.DatabaseGithubUser.GithubUserColumns.Companion.TABLE_NAME
import id.hardianadi.githubuserconsumerapp.database.GithubUserHelper

class UserContentProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: GithubUserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                USER_ID
            )
        }
    }

    override fun onCreate(): Boolean {
        userHelper = GithubUserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }


}
