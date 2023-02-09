package com.example.storyblokimg

import com.example.storyblokimg.modelDataClasses.SampleStory
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("cdn/stories/samplefolder/samplestory?&token=gew3EykKkWotFDjPXuecYQtt&version=published")
    fun getData(): Call<SampleStory>
}