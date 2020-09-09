package id.hardianadi.githubusersearch.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.AVATAR_URL
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.COMPANY
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.CONTENT_URI
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.HTML_URL
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.LOCATION
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.LOGIN
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.NAME
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.PUBLIC_REPOS
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion._ID
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.model.GithubUserNetwork
import id.hardianadi.githubusersearch.network.GithubInterface
import id.hardianadi.githubusersearch.network.ServiceBuilder
import id.hardianadi.githubusersearch.util.mapCursorToArrayList
import id.hardianadi.githubusersearch.util.toGitHubUser
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @version 1.0
 * @since 23/07/2020
 */
class UserDetailViewModel(private val userContentHelper: ContentResolver) : ViewModel() {
    companion object {
        val TAG = UserDetailViewModel::class.java.simpleName
    }

    private var _userData = MutableLiveData<GithubUser>()
    val userData: LiveData<GithubUser>
        get() = _userData

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _saveStatus = MutableLiveData<Boolean>()
    val saveStatus: LiveData<Boolean>
        get() = _saveStatus

    private var _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean>
        get() = _isFavorited

    private val request = ServiceBuilder.buildService(GithubInterface::class.java)

    fun fetchUserDetail(user: String, id: Int) {
        viewModelScope.launch {
            withContext(IO) {
                fetchUserDetailNetwork(user)
            }
        }
        checkIsAlreadyFavorite(id)
    }

    private fun fetchUserDetailNetwork(user: String) {
        _loadingStatus.postValue(true)
        val call = request.getUserDetail(user)
        call.enqueue(object : Callback<GithubUserNetwork> {

            override fun onResponse(
                call: Call<GithubUserNetwork>,
                response: Response<GithubUserNetwork>
            ) {
                _loadingStatus.postValue(false)
                if (response.isSuccessful) {
                    _userData.postValue(response.body()?.toGitHubUser())
                }
            }

            override fun onFailure(call: Call<GithubUserNetwork>, t: Throwable) {
                _loadingStatus.postValue(false)
                Log.e(TAG, t.message!!)
            }

        })
    }

    private fun checkIsAlreadyFavorite(id: Int) {
        val uriWithId = Uri.parse("$CONTENT_URI/$id")
        viewModelScope.launch(Main) {
            val deferredUser = async(IO) {
                val cursor = userContentHelper.query(uriWithId, null, null, null, null)
                var list = arrayListOf<GithubUser>()
                if (cursor != null) {
                    list = cursor.mapCursorToArrayList()
                    cursor.close()
                }
                list
            }

            val user = deferredUser.await()
            if (user.size > 0) {
                _isFavorited.postValue(true)
            } else {
                _isFavorited.postValue(false)
            }
        }

    }

    fun saveFavorite() {
        val user = _userData.value
        user?.let {
            val contentValues = contentValuesOf(
                _ID to it.id,
                LOGIN to it.login,
                COMPANY to it.company,
                AVATAR_URL to it.avatarUrl,
                HTML_URL to it.htmlUrl,
                NAME to it.name,
                LOCATION to it.location,
                PUBLIC_REPOS to it.publicRepos
            )
            viewModelScope.launch(Main) {
                withContext(IO) {
                    userContentHelper.insert(CONTENT_URI, contentValues)
                }
                _saveStatus.postValue(true)
                _isFavorited.postValue(true)

            }

        }
    }

    fun finishSave() {
        _saveStatus.value = false
    }

}