package com.example.lantsev.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lantsev.dto.Post
import com.example.lantsev.R
import com.example.lantsev.databinding.CardPostBinding
import kotlin.math.ln
import kotlin.math.pow

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onVideo(post: Post){}
}
class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            textt.text = post.content
            textlike.text = post.likes.toString()
            textshare.text = post.share.toString()
            like.isChecked = post.likedByMe
            textlike.text = getFormatedNumber(post.likes.toLong())
            textshare.text = getFormatedNumber(post.share.toLong())
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.popup_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            textlike.text = post.likes.toString()
            when {
                post.likes in 1000..999999 -> textlike.text = "${post.likes / 1000}K"
                post.likes < 1000 -> textlike.text = post.likes.toString()
                else -> textlike.text = String.format("%.1fM", post.likes.toDouble() / 1000000)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            textshare.text = post.share.toString()
            when {
                post.share < 1000 -> textshare.text = post.share.toString()
                post.share in 1000..999999 -> textshare.text = "${post.share / 1000}K"
                else -> textshare.text = String.format(
                    "%.1fM", post.share.toDouble() / 1000000
                )
            }
            video.setOnClickListener {
                onInteractionListener.onVideo(post)
            }
            play.setOnClickListener {
                onInteractionListener.onVideo(post)
            }
        }
    }
}
fun getFormatedNumber(count: Long): String {
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}
class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}