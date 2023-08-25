package com.example.adminapp


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminapp.databinding.ItemCategryBinding


class CategoryAdapter (var context:Context, val list:ArrayList<categoryMdel>) :
RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding:ItemCategryBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategryBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
           holder.binding.textView.text =list[position].cate
          Glide.with(context).load(list[position].image).into(holder.binding.imageView2)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}