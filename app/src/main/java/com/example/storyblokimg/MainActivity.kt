package com.example.storyblokimg


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.storyblokimg.modelDataClasses.SampleStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

//base url
const val BASE_URL = "https://api.storyblok.com/v2/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())//converts json file to Gson
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<SampleStory> {

            override fun onResponse(call: Call<SampleStory>, response: Response<SampleStory>) {
                val responseBody = response.body()!!

                //Title
                val title = responseBody.story.content.Title
                findViewById<TextView>(R.id.title).text = title

                //Body
                val body1 = responseBody.story.content.Desciption.content[0].content[0].text
                val body2 = responseBody.story.content.Desciption.content[1].content[0].text
                findViewById<TextView>(R.id.body).text = body1 + "\n\n" + body2

                //Image
                val imageUrl = responseBody.story.content.Image
                imgUrlToImg(imageUrl)
            }

            override fun onFailure(call: Call<SampleStory>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

        })
    }

    //converts image url to image
    private fun imgUrlToImg(imageUrl: String) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            //adding https:
            val url = "https:" + imageUrl
            try {
                val `in` = java.net.URL(url.toString()).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // Only for making changes in UI
                handler.post {
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(image)
                }
            }
            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}