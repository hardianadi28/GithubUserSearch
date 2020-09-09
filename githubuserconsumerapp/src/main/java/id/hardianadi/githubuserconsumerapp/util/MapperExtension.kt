package id.hardianadi.githubuserconsumerapp.util

import android.database.Cursor
import id.hardianadi.githubuserconsumerapp.database.DatabaseGithubUser
import id.hardianadi.githubuserconsumerapp.model.GithubUser

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */

fun Cursor.mapCursorToArrayList(): ArrayList<GithubUser> {
    val userList = ArrayList<GithubUser>()
    this.apply {
        while (moveToNext()) {
            with(DatabaseGithubUser.GithubUserColumns) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val login = getString(getColumnIndexOrThrow(LOGIN))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                val htmlUrl = getString(getColumnIndexOrThrow(HTML_URL))
                val name = getString(getColumnIndexOrThrow(NAME))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val publicRepos = getInt(getColumnIndexOrThrow(PUBLIC_REPOS))
                userList.add(GithubUser(login, company, id, avatarUrl, htmlUrl, name, location, publicRepos))
            }
        }
    }
    return userList
}