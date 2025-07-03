package com.example.healingyuk.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healingyuk.data.model.Place
import com.example.healingyuk.databinding.ItemPlaceBinding
import com.squareup.picasso.Picasso

class PlaceAdapter(
    private val items: List<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.tvName.text = item.name
            binding.tvCategory.text = item.category
            binding.tvShortDesc.text = item.short_description
            Picasso.get().load(item.image_url).into(binding.ivPlace)

            binding.btnReadMore.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
}
