package unifor.br.pokedex.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import unifor.br.pokedex.R
import unifor.br.pokedex.adapter.PokeListEvent
import unifor.br.pokedex.adapter.PokemonListAdapter
import unifor.br.pokedex.adapter.PokemonListAdapterFav
import unifor.br.pokedex.model.PokemonListItem
import unifor.br.pokedex.util.Listeners
import unifor.br.pokedex.util.PopUpMenu
import android.widget.Toast




class MainActivity : AppCompatActivity(), PokeListEvent {

    private lateinit var pokemonList: RecyclerView
    private lateinit var favPokemonList: RecyclerView
    private lateinit var btnPokemon: Button
    private lateinit var btnFavorites: Button
    private lateinit var popUp: PopUpMenu
    private var pokemons: MutableList<PokemonListItem> = java.util.ArrayList()
    private var favPokemons: MutableList<PokemonListItem> = java.util.ArrayList()
    private val api = PokeApiClient()
    private lateinit var listeners: Listeners
    private var isLoading = false

    private var currentPokemon = 0

    private lateinit var adapter2: PokemonListAdapterFav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val layoutManager = GridLayoutManager(this, 3)
        val layoutManagerFav = GridLayoutManager(this, 3)

        val adapter = PokemonListAdapter(this, pokemons)
        adapter2 = PokemonListAdapterFav(this, favPokemons)

        adapter.listener = this
        adapter2.listener = this

        pokemonList = findViewById(R.id.main_recyclerview_pokemonlist)
        pokemonList.layoutManager = layoutManager

        favPokemonList = findViewById(R.id.main_recyclerview_fav_pokemonlist)
        favPokemonList.layoutManager = layoutManagerFav
        favPokemonList.visibility = View.INVISIBLE //Invisivel

        btnPokemon = findViewById(R.id.btnPokemons)
        btnFavorites = findViewById(R.id.btnFavorites)
        pokemonList.adapter = adapter
        favPokemonList.adapter = adapter2

        dynamicLoad(adapter)

        val view: View = this.findViewById(android.R.id.content)
        listeners = Listeners(this, view, pokemonList, favPokemonList)
        listeners.initializeListeners()
        popUp = PopUpMenu(this, root_layout, favPokemons)

        /*
        Carregamento Dinamico
        */

        pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        dynamicLoad(adapter)
                    }
                }
            }
        })

    }

    @SuppressLint("DefaultLocale")
    private fun dynamicLoad(adapter: PokemonListAdapter) {
        isLoading = true
        pokemonList.isEnabled = false
        doAsync{
        if (currentPokemon <= 151) {
            try {
                val start = currentPokemon + 1
                val end = currentPokemon + 10
                for (i in start..end) {

                    val name = api.getPokemon(i).name.capitalize()
                    val img = Uri.parse(api.getPokemon(i).sprites.frontDefault)
                    pokemons.add(PokemonListItem(i, name, img))
                    Log.i("dynamicLoad", "Loaded: $i")
                    uiThread{
                        adapter.notifyDataSetChanged()
                        // mostrando cada um, caso queira remover, apenas tirar.
                    }
                }
                currentPokemon = end
            } catch (e: Exception) {
                print(e)
            }
        }
            onComplete {
                isLoading = false
                pokemonList.isEnabled = true
            }

        }
    }

    override fun onClick(view: View, position: Int) {
        val it = Intent(this, DetailActivity::class.java)
        if (listeners.isOnFavorites() && favPokemons.size > 0){
            it.putExtra("poke_id", favPokemons[position].id)
            startActivity(it)
        }else{
            it.putExtra("poke_id", pokemons[position].id)
            startActivity(it)
        }
    }

    override fun onLongClick(view: View, position: Int) {
        popUp.makePopUp(position)
    }

    override fun onFavoritesClick(view: View, position: Int){
        val item = pokemons[position].copy()
        val text = "Favoritou: " + pokemons[position].name

        if (favPokemons.contains(item)) { // caso o pokemon já esteja na lista, ele não adiciona.
            Toast.makeText(this, "Esse pokemon já é favorito", Toast.LENGTH_SHORT).show()
        }else{
            favPokemons.add(item) // caso o pokemon não esteja na lista, ele adiciona.
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            loadFavoritesList()
        }
    }


    private fun loadFavoritesList(){
        adapter2.notifyDataSetChanged()
    }

    override fun onRemoveFavoritesClick(view: View, position: Int){
        val text = "Removeu dos Favoritos: " + favPokemons[position].name
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        favPokemons.removeAt(position)
        loadFavoritesList()
    }

}
