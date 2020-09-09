package id.hardianadi.githubusersearch.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.model.GithubUser
import id.hardianadi.githubusersearch.ui.mainlist.GithubUserAdapter
import id.hardianadi.githubusersearch.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {

    private var userParam: String? = null
    private lateinit var viewModel: FollowerViewModel

    companion object {
        private const val USER_PARAM = "user_param"

        @JvmStatic
        fun newInstance(userParam: String) =
            FollowerFragment().apply {
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
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory())
            .get(FollowerViewModel::class.java)
        viewModel.fetchFollower(userParam ?: "")
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = getAdapter()
        prepareRecycleView(adapter)
        setObserve(adapter)
    }

    private fun getAdapter(): GithubUserAdapter {
        val adapter = GithubUserAdapter(false)
        adapter.data = listOf()
        adapter.setOnClickListener(object : GithubUserAdapter.AdapterOnClickListener {
            override fun onClick(user: GithubUser) {

            }
        })
        return adapter
    }

    private fun prepareRecycleView(adapter: GithubUserAdapter) {
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
    }

    private fun setObserve(adapter: GithubUserAdapter) {
        viewModel.followerList.observe(viewLifecycleOwner, {
            adapter.data = it
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, {
            pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.noData.observe(viewLifecycleOwner, {
            tvNoData.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}