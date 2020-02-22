package unifor.br.pokedex.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.TextView
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import unifor.br.pokedex.R
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 2000

    private lateinit var statusLabel: TextView

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            loadMainActivity()
        }
    }

    private fun loadMainActivity(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
        statusLabel = findViewById(R.id.statusLabel)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

}
