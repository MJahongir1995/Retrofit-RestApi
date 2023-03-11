package uz.jahongir.restapi_retrofit.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uz.jahongir.restapi_retrofit.models.MyToDo
import uz.jahongir.restapi_retrofit.models.MyToDoRequest

interface MyRetrofitService {

    @GET("plan")
    fun getAllToDo():Call<List<MyToDo>>

    @POST("plan/")
    fun addToDo(@Body myToDo: MyToDo):Call<MyToDo>

    @DELETE("plan/{id}/")
    fun deleteToDo(@Path("id") id:Int):Call<Int>

    @PATCH("plan/{id}/")
    fun updateToDo(@Path("id")id:Int, @Body myToDo: MyToDo):Call<MyToDo>
}