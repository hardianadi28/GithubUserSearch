package id.hardianadi.githubusersearch.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hardianadi.githubusersearch.database.DatabaseGithubUser.GithubUserColumns.Companion.CONTENT_URI
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.util.mapCursorToArrayList
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */
class FavoriteViewModel(private val userContentHelper: ContentResolver) : ViewModel() {

    private var _userList = MutableLiveData<List<GithubUser>>()
    val userList: LiveData<List<GithubUser>>
        get() = _userList

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean>
        get() = _noData

    init {
        loadNotesAsync()
    }

    fun loadNotesAsync() {
        viewModelScope.launch(Main) {
            _loadingStatus.postValue(true)
            _noData.postValue(false)
            _userList.postValue(listOf())

            val deferredNotes = async(IO) {
                val cursor = userContentHelper.query(CONTENT_URI, null, null, null, null)
                var list = arrayListOf<GithubUser>()
                if (cursor != null) {
                    list = cursor.mapCursorToArrayList()
                    cursor.close()
                }
                list
            }
            _loadingStatus.postValue(false)
            val userList = deferredNotes.await()
            if (userList.size > 0) {
                _userList.postValue(userList)
            } else {
                _noData.postValue(true)
            }
        }
    }

    fun deleteFavorite(id: Int): Int = runBlocking(IO) {
        val uriWithId = Uri.parse("$CONTENT_URI/$id")
        _loadingStatus.postValue(true)

        val deferredIdDeleted = async(IO) {
            userContentHelper.delete(uriWithId, null, null)
        }
        _loadingStatus.postValue(false)
        deferredIdDeleted.await()
    }
}