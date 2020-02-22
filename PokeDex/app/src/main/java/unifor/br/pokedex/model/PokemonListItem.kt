package unifor.br.pokedex.model

import android.net.Uri

data class PokemonListItem (
    val id:Int,
    val name:String,
    var img:Uri
)
