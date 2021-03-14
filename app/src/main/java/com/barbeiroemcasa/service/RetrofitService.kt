package com.barbeiroemcasa.service

import com.barbeiroemcasa.model.Barbeiro
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface RetrofitService {

//    @GET("{idYoutubeVideo}{key}{part}")
//    fun getVideoInformation( @Path("idYoutubeVideo") idYoutubeVideo: String,
//                             @Path("key") key:String,
//                             @Path("part") part:String): Call<YoutubeVideo>


    @GET("/aroundBarbers")
    fun getAroundBarbers(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): Call<List<Barbeiro>>


}