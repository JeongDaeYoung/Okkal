package com.daeng.okkal.view.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daeng.okkal.R
import com.daeng.okkal.databinding.ColorListItemBinding
import javax.inject.Inject

class ColorListAdapter (val selListColor: (Int) -> Unit) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    lateinit var binding: ColorListItemBinding
    private var arColorSet: HashSet<Int> = HashSet()
    lateinit var  context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ColorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_stroke) as GradientDrawable
        drawable.setColor(arColorSet.elementAt(position))
        holder.colorView.setImageDrawable(drawable)
        holder.colorView.setOnClickListener {
            selListColor(arColorSet.elementAt(position))
        }
    }

    override fun getItemCount(): Int {
        return arColorSet.size
    }

    fun setItem(arColorSet: HashSet<Int>) {
        this.arColorSet = arColorSet
    }

    fun addItem(colorItem: Int) {
        arColorSet.add(colorItem)
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var colorView: ImageView = itemView.findViewById(R.id.img_color)
    }
}