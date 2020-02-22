package unifor.br.pokedex.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import unifor.br.pokedex.model.PokemonListItem

class PokemonListAdapter(val context: Context, val pokemonlist: MutableList<PokemonListItem>):
    RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {
    // Adicionando Listener

    var listener: PokeListEvent? = null

    /* cria a instancia */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        // cria o layout visual do items
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            layoutInflater.inflate(unifor.br.pokedex.R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view, listener)
    }

    /* retorna a quantidade de items que deve exibir na tela */
    override fun getItemCount(): Int {
        return pokemonlist.size
    }

    /* atualizacao dos dados */
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        if(!pokemonlist[position].img.equals("null")) {
            Picasso.get().load(pokemonlist[position].img).into(holder.pokemonimg)
        }
        holder.pokemonname.text = pokemonlist[position].name
    }

    class PokemonViewHolder(view: View, listener: PokeListEvent?) : RecyclerView.ViewHolder(view) {
        val pokemonimg: ImageView = view.findViewById(unifor.br.pokedex.R.id.item_imageview_photo)
        val pokemonname: TextView = view.findViewById(unifor.br.pokedex.R.id.item_imageview_name)
        val buttonFavorites: Button = view.findViewById(unifor.br.pokedex.R.id.btnFavorite)

        init {
            view.setOnClickListener {
                listener?.onClick(it, adapterPosition)
            }

            view.setOnLongClickListener{
                listener?.onLongClick(it, adapterPosition)
                return@setOnLongClickListener true
            }

            buttonFavorites.setOnClickListener{
                listener?.onFavoritesClick(it, adapterPosition)
            }
        }
    }
}