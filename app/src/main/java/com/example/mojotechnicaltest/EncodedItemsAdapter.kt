package com.example.mojotechnicaltest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mojotechnicaltest.models.EncodedItem
import kotlinx.android.synthetic.main.item_encoded_data.view.*

class EncodedItemsAdapter : RecyclerView.Adapter<EncodedItemsAdapter.ViewHolder>() {

    private val encodedItems: MutableList<EncodedItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_encoded_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(encodedItems[position])
    }

    override fun getItemCount() = encodedItems.size

    fun setData(newEncodedData: List<EncodedItem>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallback(
                encodedItems,
                newEncodedData
            )
        )

        this.encodedItems.clear()
        this.encodedItems.addAll(newEncodedData)

        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(encodedItem: EncodedItem) {
            Glide.with(itemView.context)
                .load(encodedItem.picturePath)
                .into(itemView.ivEncoded)

            itemView.tvHiddenMessage.text = encodedItem.hiddenText
        }
    }

    class DiffUtilCallback(
        private val newList: List<EncodedItem>,
        private val oldList: List<EncodedItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition].picturePath == oldList[oldItemPosition].picturePath
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}