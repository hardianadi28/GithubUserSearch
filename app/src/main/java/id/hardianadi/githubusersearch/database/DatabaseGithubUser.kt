package id.hardianadi.githubusersearch.database

import android.net.Uri
import android.provider.BaseColumns

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */
object DatabaseGithubUser {
    const val AUTHORITY = "id.hardianadi.githubusersearch"
    const val SCHEME = "content"

    class GithubUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "github_users"
            const val _ID = "id"
            const val LOGIN = "login"
            const val COMPANY = "company"
            const val AVATAR_URL = "avatarUrl"
            const val HTML_URL = "htmlUrl"
            const val NAME = "name"
            const val LOCATION = "location"
            const val PUBLIC_REPOS = "publicRepos"

            // untuk membuat URI content://id.hardianadi.githubusersearch/github_users
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}