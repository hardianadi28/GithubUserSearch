package id.hardianadi.githubuserconsumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */
@Parcelize
data class GithubUser(
    val login: String? = null,
    val company: String? = null,
    val id: Int? = null,
    val avatarUrl: String? = null,
    val htmlUrl: String? = null,
    val name: String? = null,
    val location: String? = null,
    val publicRepos: Int? = null
) : Parcelable