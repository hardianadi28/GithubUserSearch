package id.hardianadi.githubuserconsumerapp.viewmodel

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 07/09/2020
 */
class GithubUserViewModelFactory(private val userContentHelper: ContentResolver) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userContentHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}