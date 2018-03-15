package simararora.raven

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import simararora.ravenlib.ParseCompleteListener
import simararora.ravenlib.Raven
import simararora.ravenlib.model.RavenResource
import java.lang.Exception

class MainActivity : AppCompatActivity(), ParseCompleteListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Raven.init(this)
        Raven.getInstance().parse(Uri.parse("https://www.google.com/ac/p8ukD/aa"), this)
    }

    override fun onParseComplete(ravenResource: RavenResource?) {
        Log.d("Simar", ravenResource.toString())
    }

    override fun onParseFailed(exception: Exception?) {

    }
}
