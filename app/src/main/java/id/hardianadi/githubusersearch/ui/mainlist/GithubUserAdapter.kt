package id.hardianadi.githubusersearch.ui.mainlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.model.GithubUser
import kotlinx.android.synthetic.main.item_list.view.*

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @version 1.0
 * @since 22/07/2020
 */
class GithubUserAdapter(private val isAbleToDelete: Boolean) :
    RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {

    var data = listOf<GithubUser>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var onClickListener: AdapterOnClickListener

    fun setOnClickListener(onClickListener: AdapterOnClickListener) {
        this.onClickListener = onClickListener
    }

    private lateinit var onClickDeleteListener: AdapterOnClickListener

    fun setOnClickDeleteListener(onClickDeleteListener: AdapterOnClickListener) {
        this.onClickDeleteListener = onClickDeleteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: GithubUser) {
            with(itemView) {
                tvUsername.text = user.login
                tvUrl.text = user.htmlUrl
                Glide.with(context)
                    .load(user.avatarUrl)
                    .apply(
                        RequestOptions().override(350, 550)
                            .placeholder(R.drawable.loading_animation)
                    )
                    .into(imgProfile)

                setOnClickListener { onClickListener.onClick(user) }
                ivDelete.visibility = if (isAbleToDelete) View.VISIBLE else View.GONE
                if (isAbleToDelete)ivDelete.setOnClickListener { onClickDeleteListener.onClick(user) }

            }
        }
    }

    interface AdapterOnClickListener {
        fun onClick(user: GithubUser)
    }
}