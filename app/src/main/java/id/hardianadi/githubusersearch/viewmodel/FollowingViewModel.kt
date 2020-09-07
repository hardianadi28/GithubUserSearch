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
 * @since 24/07/2020
 */
class FollowingViewModel : ViewModel() {

    companion object {
        val TAG = FollowingViewModel::class.java.simpleName
    }

    private var _followingList = MutableLiveData<List<GithubUser>>()
    val followingList: LiveData<List<GithubUser>>
        get() = _followingList

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean>
        get() = _noData

    private val request = ServiceBuilder.buildService(GithubInterface::class.java)

    fun fetchFollowing(user: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fetchFollowingNetwork(user)
            }
        }
    }

    private fun fetchFollowingNetwork(user: String) {
        _loadingStatus.postValue(true)
        _noData.postValue(false)
        val call = request.getFollowing(user)
        call.enqueue(object : Callback<List<GithubUser>> {


            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                _loadingStatus.postValue(false)
                if(response.isSuccessful) {
                    if(response.body()?.size!! > 0) {
                        _followingList.postValue(response.body())
                    }else{
                        _noData.postValue(true)
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _loadingStatus.postValue(false)
                Log.e(TAG, t.message!!)
            }

        })
    }

}