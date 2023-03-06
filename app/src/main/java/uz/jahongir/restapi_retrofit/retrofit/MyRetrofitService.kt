package uz.jahongir.restapi_retrofit.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.jahongir.restapi_retrofit.models.MyToDo
import uz.jahongir.restapi_retrofit.models.MyToDoRequest

interface MyRetrofitService {

    @GET("plan")
    fun getAllToDo():Call<List<MyToDo>>

    @POST("plan/")
    fun addToDo(@Body myToDoRequest: MyToDoRequest):Call<MyToDo>



}