package com.standard.pacebook.ui.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.databinding.ItemRecruitingPostBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RecruitingPostAdapter(
    private var posts: List<RecruitingPost>,
    private val onItemClick: (RecruitingPost) -> Unit
) : RecyclerView.Adapter<RecruitingPostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemRecruitingPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
        // ViewHolder에 데이터가 바인딩될 때 클릭 리스너를 설정
        holder.itemView.setOnClickListener {
            onItemClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<RecruitingPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    // ViewHolder의 init 블록 제거
    inner class PostViewHolder(private val binding: ItemRecruitingPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: RecruitingPost) {
            binding.textViewTitle.text = post.title
            try {
                val parsedTime = OffsetDateTime.parse(post.scheduled_time)
                val formatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN)
                binding.textViewTime.text = parsedTime.format(formatter)
            } catch (e: Exception) {
                binding.textViewTime.text = post.scheduled_time.substringBefore("T")
            }

            binding.textViewDistance.text = "${post.distance_km} km"
            binding.textViewDescription.text = post.description
            
            val minutes = post.pace / 100
            val seconds = post.pace % 100
            binding.textViewPace.text = "${minutes}'${String.format("%02d", seconds)}\""
        }
    }
}