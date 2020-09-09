package id.hardianadi.githubusersearch.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.ui.setting.SettingsActivity
import id.hardianadi.githubusersearch.viewmodel.GithubUserViewModelFactory
import id.hardianadi.githubusersearch.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
    }

    private var user: String = ""
    private var id: Int = 0

    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var viewModelFactory: GithubUserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.apply {
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        user = intent.getStringExtra(EXTRA_USER) ?: "hardianadi28"
        id = intent.getIntExtra(EXTRA_ID, 0)

        setViewModel()
        setObserve()
        setTab()

        fabAdd.setOnClickListener {
            userDetailViewModel.saveFavorite()
        }

    }

    private fun setViewModel() {
        viewModelFactory = GithubUserViewModelFactory(contentResolver)
        userDetailViewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(UserDetailViewModel::class.java)
        userDetailViewModel.fetchUserDetail(user, id)
    }

    private fun setTab() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, user)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun setObserve() {
        userDetailViewModel.userData.observe(this, {
            tvName.text = it.name
            tvUsername.text = it.login
            tvLocation.text = it.location
            tvCompany.text = it.company
            tvRepo.text = it.publicRepos.toString()

            Glide.with(imgProfile.context)
                .load(it.avatarUrl)
                .into(imgProfile)
        })

        userDetailViewModel.loadingStatus.observe(this, {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        })

        userDetailViewModel.saveStatus.observe(this, {
            if (it) {
                Toast.makeText(
                    this@UserDetailActivity,
                    "Successfully addedd to favorite",
                    Toast.LENGTH_SHORT
                ).show()
                userDetailViewModel.finishSave()
            }
        })

        userDetailViewModel.isFavorited.observe(this, {
            if (it) {
                fabAdd.hide()
            } else {
                fabAdd.show()
            }
        })
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
}