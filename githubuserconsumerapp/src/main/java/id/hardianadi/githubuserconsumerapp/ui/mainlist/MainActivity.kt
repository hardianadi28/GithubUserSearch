package id.hardianadi.githubuserconsumerapp.ui.mainlist

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.hardianadi.githubuserconsumerapp.R
import id.hardianadi.githubuserconsumerapp.model.GithubUser
import id.hardianadi.githubuserconsumerapp.viewmodel.GithubUserViewModelFactory
import id.hardianadi.githubuserconsumerapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: GithubUserViewModelFactory
    private val adapter = GithubUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
        setObserve()
        prepareRecycleView()

    }

    private fun setViewModel() {
        viewModelFactory = GithubUserViewModelFactory(contentResolver)
        mainViewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    private fun setObserve() {
        mainViewModel.userList.observe(this, {
            adapter.data = it
        })
        mainViewModel.loadingStatus.observe(this, {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        })
        mainViewModel.noData.observe(this, {
            tvNoData.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun prepareRecycleView() {
        rvUserList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)

            this.adapter = this@MainActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        adapter.setOnClickListener(object : GithubUserAdapter.AdapterOnClickListener {
            override fun onClick(user: GithubUser) {
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}
