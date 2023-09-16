package com.example.lab_1_new.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_1_new.R
import com.example.lab_1_new.databinding.RecipeBinding
import com.squareup.picasso.Picasso
import java.util.jar.Attributes.Name

class Recipe_Adapter(val listener: Listener): RecyclerView.Adapter<Recipe_Adapter.BlockHolder>() {

    val RecipeList=ArrayList<Recipe>()

    class BlockHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = RecipeBinding.bind(item)
        fun bind(recipe: Recipe, listener: Listener) = with(binding){
            NameRecipe.text=recipe.Description
            Picasso.get().load(recipe.Image).into(imageRecipe)
            carder.setOnClickListener{
                listener.onClick(recipe)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recipe,parent,false)
        return  BlockHolder(view)
    }

    override fun onBindViewHolder(holder: BlockHolder, position: Int) {
        holder.bind(RecipeList[position], listener )
    }

    override fun getItemCount(): Int {
        return RecipeList.size
    }

    fun RecipeCreate(recipe: Recipe){
        var marker=true
        var l=0
        while (l<RecipeList.size) {
            if (recipe.Description == RecipeList[l].Description) {
                marker = false
                break
            }
            l += 1
        }
        if(marker)
            RecipeList.add(Recipe(recipe.id,recipe.Description, recipe.Image))
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(recipe: Recipe)
    }
}