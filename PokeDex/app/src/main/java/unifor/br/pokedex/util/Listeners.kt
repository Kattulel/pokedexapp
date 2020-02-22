package unifor.br.pokedex.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import unifor.br.pokedex.R
import android.view.animation.TranslateAnimation



class Listeners(val context: Context, view: View,
    private val pokemonList: RecyclerView,
    private val favPokemonList: RecyclerView) {

    private val btnPokemon: Button = view.findViewById(R.id.btnPokemons)
    private val btnFavorites: Button = view.findViewById(R.id.btnFavorites)
    private var onFavorites: Boolean = false

    fun initializeListeners() {

        btnPokemon.setOnClickListener {
            swapLists(0)
        }

        btnFavorites.setOnClickListener {
            swapLists(1)
        }
    }

    fun isOnFavorites() : Boolean{
        return onFavorites
    }

    private fun swapLists(id: Int){
        if (id == 0) { // ButtonPokemon Pressed
            btnFavorites.setBackgroundColor(Color.rgb(244,67,54))
            btnFavorites.setTextColor(Color.WHITE)
            btnPokemon.setBackgroundColor(Color.TRANSPARENT)
            btnPokemon.setTextColor(Color.BLACK)
            favPokemonList.visibility = View.INVISIBLE
            pokemonList.visibility = View.VISIBLE
            onFavorites = false
        }else if(id == 1) { // ButtonFavorites Pressed
            btnPokemon.setBackgroundColor(Color.rgb(244,67,54))
            btnPokemon.setTextColor(Color.WHITE)
            btnFavorites.setBackgroundColor(Color.TRANSPARENT)
            btnFavorites.setTextColor(Color.BLACK)
            pokemonList.visibility = View.INVISIBLE
            favPokemonList.visibility = View.VISIBLE
            onFavorites = true
        }
    }

    private fun AnimatePokemonBtn(){
        slideLeftAnimation(favPokemonList)
        favPokemonList.visibility = View.INVISIBLE
        slideRightAnimation(pokemonList)
        pokemonList.visibility = View.VISIBLE
    }

    private fun AnimateFavPokemonBtn(){
        slideLeftAnimation(pokemonList)
        pokemonList.visibility = View.INVISIBLE
        slideRightAnimation(favPokemonList)
        favPokemonList.visibility = View.VISIBLE
    }

    private fun slideRightAnimation(view: View)
    {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            view.height.toFloat(), // fromXDelta
            0f, // toXDelta
            0f, // fromYDelta
            0f // toYDelta
        )
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.clearAnimation()
    }

    private fun slideLeftAnimation(view: View)
    {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            view.height.toFloat(), // toXDelta
            0f, // fromYDelta
            0f // toYDelta
        )
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.clearAnimation()
    }
}