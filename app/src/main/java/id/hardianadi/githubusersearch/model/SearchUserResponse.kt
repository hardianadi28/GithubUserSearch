package id.hardianadi.githubusersearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchUserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int = 0,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<GithubUserNetwork>? = null
) : Parcelable