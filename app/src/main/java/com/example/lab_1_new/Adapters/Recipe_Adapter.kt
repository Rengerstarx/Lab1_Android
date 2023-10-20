package com.example.lab_1_new.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_1_new.Data_Classes.Recipe_pars
import com.example.lab_1_new.R
import com.example.lab_1_new.databinding.RecipeBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class Recipe_Adapter(val listener: Listener): RecyclerView.Adapter<Recipe_Adapter.BlockHolder>() {

    val RecipeList=ArrayList<Recipe_pars>()

    class BlockHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = RecipeBinding.bind(item)
        fun bind(recipe: Recipe_pars, listener: Listener) = with(binding){
            textName.text=recipe.Name
            textCalory.text=recipe.Calorie
            textHard.text=recipe.Difficulty.toString()
            when (recipe.Difficulty){
                1 -> StartDif.setImageResource(R.drawable.star)
                2 -> StartDif.setImageResource(R.drawable.star2)
                3 -> StartDif.setImageResource(R.drawable.star3)
                4 -> StartDif.setImageResource(R.drawable.star4)
                5 -> StartDif.setImageResource(R.drawable.star5)
            }
            carder.setOnClickListener{
                listener.onClick(recipe)
            }
            carder.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View): Boolean {
                    listener.onLongclick(recipe)
                    return true
                }
            })
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

    fun RecipeCreate(recipe: Recipe_pars){
        var marker=true
        var l=0
        while (l<RecipeList.size) {
            if (recipe.Name == RecipeList[l].Name) {
                marker = false
                break
            }
            l += 1
        }
        if(marker)
            RecipeList.add(Recipe_pars(recipe.id,recipe.Calorie, recipe.Time, recipe.Name, recipe.Ingredients, recipe.Difficulty))
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(recipe: Recipe_pars)
        fun onLongclick(recipe: Recipe_pars)
    }
}