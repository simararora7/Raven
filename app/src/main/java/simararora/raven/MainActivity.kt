package simararora.raven

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import simararora.ravenlib.Raven

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Raven.init(this)
        Raven.testRead()
    }
}
