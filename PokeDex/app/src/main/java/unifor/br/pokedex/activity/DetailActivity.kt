package unifor.br.pokedex.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import unifor.br.pokedex.R
import java.util.*

class DetailActivity : AppCompatActivity() {

    private val api = PokeApiClient()
    private lateinit var detailPhoto: ImageView
    private lateinit var detailName: TextView
    private lateinit var labelDesc: TextView
    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        title = "Loading Data"

        val pokemonId = intent.getIntExtra("poke_id", 0)

        doAsync{
            pokemon = api.getPokemon(pokemonId)
            uiThread{
                loadInfo(pokemon)
            }
        }

    }

    private fun loadInfo(pokemon: Pokemon){
        val type1Text: TextView = findViewById(R.id.type1_text)
        val type2Text: TextView = findViewById(R.id.type2_text)
        //val cardType1: CardView = findViewById(R.id.card_type1)
        val cardType2: CardView = findViewById(R.id.card_type2)
        val labelWeight: TextView = findViewById(R.id.label_peso)
        val labelHeight: TextView = findViewById(R.id.label_height)
        val labelSpecies: TextView = findViewById(R.id.label_species)

        detailPhoto = findViewById(R.id.detail_pokemon_image)
        labelDesc = findViewById(R.id.label_desc)

        val pokemonPhoto = Uri.parse(pokemon.sprites.frontDefault)

        // Carrega Imagem do Pokemon
        Picasso.get().load(pokemonPhoto).into(detailPhoto)
        // Nome do pokemon na barra de titulo
        title = pokemon.name.toUpperCase(Locale.getDefault())
        //Tipos do pokemon

        if(pokemon.types.size > 1) {
            type1Text.text = pokemon.types[0].type.name.toUpperCase(Locale.getDefault())
            type2Text.text = pokemon.types[1].type.name.toUpperCase(Locale.getDefault())
        }else{
            type1Text.text = pokemon.types[0].type.name.toUpperCase(Locale.getDefault())
            cardType2.visibility = View.INVISIBLE
        }

        doAsync{
            val desc = api.getPokemonSpecies(pokemon.id).flavorTextEntries[1].flavorText
            val species = "Species: " + api.getPokemonSpecies(pokemon.id).genera[2].genus
            val height = "Height: " + pokemon.height.toString()
            val weight = "Weight: " + pokemon.weight.toString()
            onComplete{
                    labelDesc.text = desc
                    labelSpecies.text = species
                    labelHeight.text = height
                    labelWeight.text = weight
            }

        }
    }
}
