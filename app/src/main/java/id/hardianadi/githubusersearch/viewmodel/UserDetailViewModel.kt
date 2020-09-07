package id.hardianadi.githubusersearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.network.GithubInterface
import id.hardianadi.githubusersearch.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
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
class UserDetailViewModel : ViewModel() {
    companion object {
        val TAG = UserDetailViewModel::class.java.simpleName
    }

    private var _userData = MutableLiveData<GithubUser>()
    val userData: LiveData<GithubUser>
        get() = _userData

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private val request = ServiceBuilder.buildService(GithubInterface::class.java)

    fun fetchUserDetail(user: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fetchUserDetailNetwork(user)
            }
        }
    }

    private fun fetchUserDetailNetwork(user: String) {
        _loadingStatus.postValue(true)
        val call = request.getUserDetail(user)
        call.enqueue(object : Callback<GithubUser> {

            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                _loadingStatus.postValue(false)
                if (response.isSuccessful) {
                    _userData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                _loadingStatus.postValue(false)
                Log.e(TAG, t.message!!)
            }

        })
    }

}