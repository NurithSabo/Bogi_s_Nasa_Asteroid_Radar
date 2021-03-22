package com.udacity.asteroidradar.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidRecyclerBinding


class AsteroidAdapter(val clickOnAsteroid : OnClickListener) :
ListAdapter<Asteroid, AsteroidAdapter.MyViewHolder>(DiffCallback) {

    class MyViewHolder(private var binding : AsteroidRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(ast : Asteroid){
            binding.asteroid = ast
            binding.executePendingBindings()
        }//End of bind

    }// End of ViewHolder

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(AsteroidRecyclerBinding.inflate(LayoutInflater.from(parent.context)))
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int){

        val asteroidData = getItem(position)
        holder.itemView.setOnClickListener {
            clickOnAsteroid.onClick(asteroidData)
        }
        holder.bind(asteroidData)
    }

    class OnClickListener(val clickListener: (asteroidData: Asteroid) -> Unit) {
        fun onClick(asteroidData: Asteroid) = clickListener(asteroidData)
    }


}