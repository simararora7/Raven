package simararora.raven

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import simararora.ravenlib.ParseCompleteListener
import simararora.ravenlib.Raven
import simararora.ravenlib.model.RavenResource
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Raven.init(this)

        Raven.parse(Uri.parse("https://www.google.com/ab/abcde/abc"), object : ParseCompleteListener() {
            override fun onParseComplete(ravenResource: RavenResource?) {

            }

            override fun onParseFailed(exception: Exception?) {

            }
        })
    }
}
