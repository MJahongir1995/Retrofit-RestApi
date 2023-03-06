package uz.jahongir.restapi_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.jahongir.restapi_retrofit.adapters.MyRvAdapter
import uz.jahongir.restapi_retrofit.databinding.ActivityMainBinding
import uz.jahongir.restapi_retrofit.databinding.DialogItemBinding
import uz.jahongir.restapi_retrofit.models.MyToDo
import uz.jahongir.restapi_retrofit.models.MyToDoRequest
import uz.jahongir.restapi_retrofit.retrofit.APIClient

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var myRvAdapter:MyRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myRvAdapter = MyRvAdapter()
        binding.rv.adapter = myRvAdapter
        loadToDo()

        binding.swipe.setOnRefreshListener {
            loadToDo()
        }

        binding.btnAdd.setOnClickListener {
            addToDo()
        }
    }

    private fun addToDo() {
        val dialog = AlertDialog.Builder(this)
        val dialogItemBinding = DialogItemBinding.inflate(layoutInflater)
        dialog.setView(dialogItemBinding.root)

        dialogItemBinding.btnSave.setOnClickListener {
            val myToDoRequest = MyToDoRequest(
                "Yangi",
                dialogItemBinding.edtMatn.text.toString(),
                dialogItemBinding.edtDeadline.text.toString(),
                dialogItemBinding.edtTitle.text.toString()
            )

            dialogItemBinding.addProgress.visibility = View.VISIBLE
            APIClient.getApiService().addToDo(myToDoRequest)
                .enqueue(object :Callback<MyToDo>{
                    override fun onResponse(call: Call<MyToDo>, response: Response<MyToDo>) {
                        dialogItemBinding.addProgress.visibility = View.GONE
                        Toast.makeText( this@MainActivity, "${response.body()?.id} id bilan saqlandi", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<MyToDo>, t: Throwable) {
                        dialogItemBinding.addProgress.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Check network connection!", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        dialog.show()
    }

    private fun loadToDo(){

        APIClient.getApiService().getAllToDo()
            .enqueue(object :Callback<List<MyToDo>>{
                override fun onResponse(call: Call<List<MyToDo>>, response: Response<List<MyToDo>>) {
                    if (response.isSuccessful){
                        binding.myProgressBar.visibility = View.GONE
                        myRvAdapter.list.clear()
                        myRvAdapter.list.addAll(response.body()!!)
                        myRvAdapter.notifyDataSetChanged()
                        binding.swipe.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<List<MyToDo>>, t: Throwable) {
                    binding.myProgressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Check network connection!", Toast.LENGTH_SHORT).show()
                    binding.swipe.isRefreshing = false
                }
            })

    }
}