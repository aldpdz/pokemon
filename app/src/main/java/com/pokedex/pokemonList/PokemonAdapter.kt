package com.pokedex.pokemonList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pokedex.R
import com.pokedex.data.remote.BasicInfo

class PokemonAdapter () :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    var dataSet = listOf<BasicInfo>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPokemonName: TextView = view.findViewById(R.id.tv_pokemon_name)
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val img: ImageView = view.findViewById(R.id.pokemon_image)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_pokemon, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvPokemonName.text = dataSet[position].name
        viewHolder.tvType.text = dataSet[position].types

        val imgUri = dataSet[position].urlImg.toUri().buildUpon().scheme("https").build()
        Glide.with(viewHolder.img.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image))
            .into(viewHolder.img)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}