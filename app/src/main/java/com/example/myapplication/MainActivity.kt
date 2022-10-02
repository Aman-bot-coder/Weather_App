package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY:String = "bareilly,in"
    val API: String = "6fb8b182f19d1b85496df4d7e1ff339"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()

    }

    inner class weatherTask(): AsyncTask<String, Void ,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.p_bar).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.main).visibility = View.GONE
            findViewById<TextView>(R.id.error).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8)
            }
            catch (e:Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonob = JSONObject(result)
                val main = jsonob.getJSONObject("main")
                val sys = jsonob.getJSONObject("sys")
                val wind = jsonob.getJSONObject("wind")
                val weather = jsonob.getJSONArray("weather").getJSONObject(0)
                val updateAt:Long = jsonob.getLong("dt")
                val updatedAtText = "updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updateAt*1000))
                val temp = main.getString("temp")+"0°C"
                val mintemp = "Min Temp: " +main.getString("temp_min")+"0°C"
                val maxtemp = "Max Temp"+main.getString("temp_max")+"0°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("Sunrise")
                val sunset:Long = sys.getLong("Sunset")
                val windSpeed = wind.getString("wind")
                val weatheeDes = weather.getString("Description")
                val address = jsonob.getString("name")+" , "+sys.getString("Country")
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.dt).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatheeDes.capitalize()
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_max).text = maxtemp
                findViewById<TextView>(R.id.min_temp).text = mintemp
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.syst).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.sys).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<ProgressBar>(R.id.p_bar).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.main).visibility = View.VISIBLE


            }
            catch (e:Exception){
                findViewById<RelativeLayout>(R.id.main).visibility = View.GONE
                findViewById<ProgressBar>(R.id.p_bar).visibility = View.VISIBLE


            }

        }

    }
}