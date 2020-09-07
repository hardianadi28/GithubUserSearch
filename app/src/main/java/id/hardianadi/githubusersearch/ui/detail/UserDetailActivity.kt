package id.hardianadi.githubusersearch.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_detail.pbLoading

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private var user: String = ""

    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.apply {
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        user = intent.getStringExtra(EXTRA_USER) ?: "hardianadi28"

        userDetailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserDetailViewModel::class.java)

        setObserve()

        userDetailViewModel.fetchUserDetail(user)

        setTab()

    }

    private fun setTab() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, user)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun setObserve() {
        userDetailViewModel.userData.observe(this) {
            tvName.text = it.name
            tvUsername.text = it.login
            tvLocation.text = it.location
            tvCompany.text = it.company
            tvRepo.text = it.publicRepos.toString()

            Glide.with(imgProfile.context)
                .load(it.avatarUrl)
                .into(imgProfile)
        }

        userDetailViewModel.loadingStatus.observe(this) {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
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

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}