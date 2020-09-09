package id.hardianadi.githubusersearch.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.ui.detail.UserDetailActivity
import id.hardianadi.githubusersearch.ui.mainlist.GithubUserAdapter
import id.hardianadi.githubusersearch.ui.setting.SettingsActivity
import id.hardianadi.githubusersearch.viewmodel.FavoriteViewModel
import id.hardianadi.githubusersearch.viewmodel.GithubUserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var viewModelFactory: GithubUserViewModelFactory
    private val adapter = GithubUserAdapter(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        setViewModel()
        setObserve()
        prepareRecycleView()

    }

    private fun setViewModel() {
        viewModelFactory = GithubUserViewModelFactory(contentResolver)
        favoriteViewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(FavoriteViewModel::class.java)
    }

    private fun setObserve() {
        favoriteViewModel.userList.observe(this, {
            adapter.data = it
        })
        favoriteViewModel.loadingStatus.observe(this, {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        })
        favoriteViewModel.noData.observe(this, {
            tvNoData.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun prepareRecycleView() {
        rvUserList.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)

            this.adapter = this@FavoriteActivity.adapter
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

        adapter.setOnClickDeleteListener(object : GithubUserAdapter.AdapterOnClickListener {
            override fun onClick(user: GithubUser) {
                user.id?.let { showDeleteAlertDialog(it) }
            }
        })
    }

    private fun itemOnClick(user: GithubUser) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        intent.putExtra(UserDetailActivity.EXTRA_ID, user.id)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchMenu = menu?.findItem(R.id.search)
        searchMenu?.isVisible = false
        val favorite = menu?.findItem(R.id.action_favorite)
        favorite?.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(this, SettingsActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteAlertDialog(userId: Int) {
        val dialogMessage = getString(R.string.alert_delete_favorite)
        val dialogTitle = getString(R.string.title_delete_favorite)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                val result = favoriteViewModel.deleteFavorite(userId)
                if (result > 0) {
                    favoriteViewModel.loadNotesAsync()
                    Toast.makeText(
                        this@FavoriteActivity,
                        getString(R.string.alert_success_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@FavoriteActivity,
                        getString(R.string.alert_fail_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}