package com.dicoding.syamsustoryapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.databinding.ItemRowStoryBinding
import java.text.SimpleDateFormat
import java.util.*

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: ((ListStoryItem) -> Unit)? = null

    fun setOnItemClickCallback(onItemClickCallback: (ListStoryItem) -> Unit) {
        this.onItemClickCallback = onItemClickCallback
    }

    class StoryViewHolder(val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.binding.apply {
            story?.let { currentStory ->
                holder.itemView.setOnClickListener { onItemClickCallback?.invoke(currentStory) }
                tvName.text = currentStory.name
                tvDate.text = getCurrentTime()
                tvDescription.text = currentStory.description

                Glide.with(ivStory.context)
                    .load(currentStory.photoUrl)
                    .apply(RequestOptions().fitCenter())
                    .into(ivStory)
            }
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val outputFormat = SimpleDateFormat("HH:mm:ss / dd-MMM-yyyy", Locale.getDefault())
        return outputFormat.format(currentTime)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
