package com.udacity.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.main.LoadingStatus

//Picasso loading the pic of the Day
@BindingAdapter("loadImgOfTheDay")
fun loadImageWithUri(imageView: ImageView, urlPic: String?){
    Log.i("urlPIC", urlPic.toString())
    if (urlPic != null)
    {
        urlPic.let{
            val imgUri = urlPic.toUri().buildUpon().scheme("https").build()

            val videoId =
                    (urlPic.toUri().buildUpon().scheme("https").toString()).split("embed", "?")

            if (imgUri.toString().contains("youtube"))
            {
                Picasso.get()
                        .load("https://img.youtube.com/vi" + videoId[1] + "/0.jpg")
                        .into(imageView)
            }
            else if (imgUri.toString().contains(".jpg") || imgUri.toString().contains(".jpeg")
                    || imgUri.toString().contains(".png") || imgUri.toString().contains("gif"))
            {

                Picasso.get()
                        .load(imgUri)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError(e: Exception?) {
                                //Try again online if cache failed
                                Picasso.get()
                                        .load(imgUri)
                                        .into(imageView)
                            }
                        })
            }
        }
    }
    else
    {
        Picasso.get()
                    Picasso.get()
                        .load(R.drawable.no_net_pic)
                        .into(imageView)
    }
}


//Asteroidlist for recyclerview
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}

//status for progressbar
@BindingAdapter("status")
fun bindStatus(statusBarView: ProgressBar, status : LoadingStatus){
    when (status) {
        LoadingStatus.LOADING -> {
            statusBarView.visibility = View.VISIBLE
        }
        LoadingStatus.ERROR -> {
            statusBarView.visibility = View.VISIBLE
        }
        LoadingStatus.DONE -> {
            statusBarView.visibility = View.GONE
        }
    }
}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = "potentially hazardous status image"
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = "not hazardous status image"
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = "Funny hazardous asteroid image"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = "Funny safe asteroid image"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
