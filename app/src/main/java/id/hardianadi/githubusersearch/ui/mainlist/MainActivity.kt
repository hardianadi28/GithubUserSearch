package id.hardianadi.githubusersearch.ui.mainlist

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.service.AlarmReceiver
import id.hardianadi.githubusersearch.ui.detail.UserDetailActivity
import id.hardianadi.githubusersearch.ui.favorite.FavoriteActivity
import id.hardianadi.githubusersearch.ui.setting.SettingsActivity
import id.hardianadi.githubusersearch.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private val adapter = GithubUserAdapter(false)
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        setObserve()
        prepareRecycleView()
        setAlarm()

    }

    private fun setAlarm() {
        alarmReceiver = AlarmReceiver()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isNotifActive = prefs.getBoolean(this.getString(R.string.key_notification), false)
        alarmReceiver.setAlarm(this, isNotifActive)
    }

    private fun setObserve() {
        mainViewModel.userList.observe(this, { adapter.data = it })
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
                itemOnClick(user)
            }
        })
    }

    private fun itemOnClick(user: GithubUser) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        intent.putExtra(UserDetailActivity.EXTRA_ID, user.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        setSearchBar(menu)
        return true
    }

    private fun setSearchBar(menu: Menu?) {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchMenu = menu?.findItem(R.id.search)
        val searchView = searchMenu?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchMenu.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(this, SettingsActivity::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.action_favorite) {
            val mIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}