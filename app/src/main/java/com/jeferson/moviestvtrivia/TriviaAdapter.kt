package com.jeferson.moviestvtrivia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeferson.moviestvtrivia.databinding.TriviaItemLayoutBinding

class TriviaAdapter (val items: ArrayList<TriviaModel>, val context: Context) :
    RecyclerView.Adapter<TriviaAdapter.ViewHolder>() {
    /**
     * Inflates the item view which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.trivia_item_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: TriviaModel = items[position]
        holder.tvTriviaText!!.text = model.getName()
        holder.tvTriviaCatTime!!.text = model.getCatTime()
        if(model.getImage() != null) {
            holder.ivTriviaImage!!.setImageResource(model.getImage()!!)
        }
//        if(model.getIsSelected()) {
//            holder.tvItem.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_light_bg)
//            holder.tvItem.setTextColor(Color.parseColor("#212121"))
//        } else  if(model.getIsCompleted()) {
//            holder.tvItem.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_teal_bg)
//            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
//        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvTriviaCatTime: TextView? = view.findViewById<TextView>(R.id.tvTriviaCatTime!!)
        val tvTriviaText: TextView? = view.findViewById<TextView>(R.id.tvTriviaText!!)
        val ivTriviaImage: ImageView? = view.findViewById<ImageView>(R.id.ivTriviaImage!!)
    }
}