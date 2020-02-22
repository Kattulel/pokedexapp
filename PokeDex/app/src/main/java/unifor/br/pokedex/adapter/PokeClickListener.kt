package unifor.br.pokedex.adapter

import android.view.View

interface PokeClickListener {
    fun onClick(view: View, position: Int)

    fun onLongClick(view: View, position: Int)

    fun onFavoritesClick(view: View, position: Int)

    fun onRemoveFavoritesClick(view: View, position: Int)

}