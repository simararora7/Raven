package simararora.raven

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import simararora.ravenlib.RavenActivity
import simararora.ravenlib.model.RavenResource
import java.lang.Exception

class MainActivity : RavenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("WrongViewCast")
    override fun onParseComplete(ravenResource: RavenResource?) {
        Log.d("Simar", ravenResource.toString())
        findViewById<TextView>(R.id.tv_data).text = Html.fromHtml(ravenResource.toString())
    }

    override fun onParseFailed(exception: Exception?) {

    }
}
