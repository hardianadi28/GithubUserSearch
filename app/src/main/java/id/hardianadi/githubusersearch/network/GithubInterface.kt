package id.hardianadi.githubusersearch.network

import id.hardianadi.githubusersearch.model.GithubUserNetwork
import id.hardianadi.githubusersearch.model.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @version 1.0
 * @since 23/07/2020
 */
interface GithubInterface {

    @GET("/search/users")
    fun searchUser(@Query("q") value: String): Call<SearchUserResponse>

    @GET("/users/{user}")
    fun getUserDetail(@Path("user") user: String) : Call<GithubUserNetwork>

    @GET("/users/{user}/followers")
    fun getFollower(@Path("user") user: String) : Call<List<GithubUserNetwork>>

    @GET("/users/{user}/following")
    fun getFollowing(@Path("user") user: String) : Call<List<GithubUserNetwork>>
}