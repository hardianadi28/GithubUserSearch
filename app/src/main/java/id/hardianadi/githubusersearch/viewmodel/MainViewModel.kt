package id.hardianadi.githubusersearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.model.SearchUserResponse
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
 * @since 22/07/2020
 */
class MainViewModel : ViewModel() {

    companion object {
        val TAG = MainViewModel::class.java.simpleName
    }

    private var _userList = MutableLiveData<List<GithubUser>>()
    val userList: LiveData<List<GithubUser>>
        get() = _userList

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean>
        get() = _noData

    private val request = ServiceBuilder.buildService(GithubInterface::class.java)

    fun searchUser(param: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchUserNetwork(param)
            }
        }
    }

    private fun searchUserNetwork(param: String) {
        _loadingStatus.postValue(true)
        _noData.postValue(false)
        _userList.postValue(listOf())
        val call = request.searchUser(param)
        call.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _loadingStatus.postValue(false)
                if (response.isSuccessful) {
                    if (response.body()?.totalCount!! > 0) {
                        response.body()?.items?.let {
                            _userList.postValue(it)
                        }
                    } else {
                        _noData.postValue(true)
                    }

                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _loadingStatus.postValue(false)
                Log.e(TAG, t.message!!)
            }
        })
    }
}