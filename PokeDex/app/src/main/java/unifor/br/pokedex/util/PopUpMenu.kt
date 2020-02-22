package unifor.br.pokedex.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import unifor.br.pokedex.R
import unifor.br.pokedex.model.PokemonListItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class PopUpMenu(val context: Context, val views: ViewGroup, val favPokemonList: MutableList<PokemonListItem>) {

    fun makePopUp(position: Int){
        // Initialize a new layout inflater instance
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.menu_share,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }


        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut

        }

        // Get the widgets reference from custom view
        //val tv = view.findViewById<TextView>(R.id.text_view)
        val buttonPopup = view.findViewById<Button>(R.id.button_popup)

        // Set click listener for popup window's text view
        //tv.setOnClickListener{
        //    // Change the text color of popup window's text view
        //    tv.setTextColor(Color.RED)
        //}

        // Set a click listener for popup's button widget
        buttonPopup.setOnClickListener{
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        val btnShare = view.findViewById<Button>(R.id.btn_share)

        btnShare.setOnClickListener{

            val sendIntent: Intent = Intent().apply {
                val whatToShare = "https://pokemondb.net/pokedex/$position"
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, whatToShare)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context.applicationContext, shareIntent,null)
        }



        // Set a dismiss listener for popup window
        popupWindow.setOnDismissListener {
            //Toast.makeText(context.applicationContext,"Popup closed", Toast.LENGTH_SHORT).show()
        }


        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(views)
        popupWindow.showAtLocation(
            views, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )

    }
}