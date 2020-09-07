package id.hardianadi.githubusersearch.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.ui.mainlist.GithubUserAdapter
import id.hardianadi.githubusersearch.viewmodel.FollowerViewModel
import id.hardianadi.githubusersearch.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_follower.pbLoading
import kotlinx.android.synthetic.main.fragment_follower.rvUserList
import kotlinx.android.synthetic.main.fragment_follower.tvNoData

class FollowingFragment : Fragment() {

    private var userParam: String? = null
    private lateinit var viewModel: FollowingViewModel

    companion object {
        private const val USER_PARAM = "user_param"

        @JvmStatic
        fun newInstance(userParam: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_PARAM, userParam)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userParam = it.getString(USER_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)
        viewModel.fetchFollowing(userParam ?: "")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = GithubUserAdapter()
        adapter.data = listOf()

        rvUserList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)

            this.adapter = adapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        adapter.setOnClickListener(object : GithubUserAdapter.AdapterOnClickListener {
            override fun onClick(user: GithubUser) {

            }
        })

        setObserve(adapter)
    }

    private fun setObserve(adapter: GithubUserAdapter) {
        viewModel.followingList.observe(viewLifecycleOwner) {
            adapter.data = it
        }

        viewModel.loadingStatus.observe(this) {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.noData.observe(this) {
            tvNoData.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

}