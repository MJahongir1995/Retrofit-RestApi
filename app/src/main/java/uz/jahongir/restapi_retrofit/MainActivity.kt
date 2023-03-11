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
import uz.jahongir.restapi_retrofit.models.MyToDoDel
import uz.jahongir.restapi_retrofit.models.MyToDoRequest
import uz.jahongir.restapi_retrofit.retrofit.APIClient

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), MyRvAdapter.RvClick {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var myRvAdapter: MyRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myRvAdapter = MyRvAdapter(this)
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
            val myToDo = MyToDo(
                "Yangi",
                dialogItemBinding.edtMatn.text.toString(),
                dialogItemBinding.edtDeadline.text.toString(),
                dialogItemBinding.edtTitle.text.toString()
            )

            dialogItemBinding.addProgress.visibility = View.VISIBLE
            APIClient.getApiService().addToDo(myToDo)
                .enqueue(object : Callback<MyToDo> {
                    override fun onResponse(call: Call<MyToDo>, response: Response<MyToDo>) {
                        dialogItemBinding.addProgress.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "${response.body()?.id} id bilan saqlandi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<MyToDo>, t: Throwable) {
                        dialogItemBinding.addProgress.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Check network connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        dialog.show()
    }

    private fun loadToDo() {

        APIClient.getApiService().getAllToDo()
            .enqueue(object : Callback<List<MyToDo>> {
                override fun onResponse(
                    call: Call<List<MyToDo>>,
                    response: Response<List<MyToDo>>
                ) {
                    if (response.isSuccessful) {
                        binding.myProgressBar.visibility = View.GONE
                        myRvAdapter.list.clear()
                        myRvAdapter.list.addAll(response.body()!!)
                        myRvAdapter.notifyDataSetChanged()
                        binding.swipe.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<List<MyToDo>>, t: Throwable) {
                    binding.myProgressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "Check network connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.swipe.isRefreshing = false
                }
            })

    }

    override fun deleteToDO(myToDo: MyToDo) {
        APIClient.getApiService().deleteToDo(myToDo.id)
            .enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    Toast.makeText(this@MainActivity, "${myToDo.id} deleted", Toast.LENGTH_SHORT)
                        .show()
                    loadToDo()
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Check network connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    override fun updateToDo(myToDo: MyToDo) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogItemBinding = DialogItemBinding.inflate(layoutInflater)
        dialog.setView(dialogItemBinding.root)
        dialogItemBinding.edtHolat.visibility = View.VISIBLE

        dialogItemBinding.edtTitle.setText(myToDo.sarlavha)
        dialogItemBinding.edtDeadline.setText(myToDo.oxirgi_muddat)
        dialogItemBinding.edtMatn.setText(myToDo.matn)

        when (myToDo.holat) {
            "yangi" -> dialogItemBinding.edtHolat.setSelection(0)
            "bajarilmoqda" -> dialogItemBinding.edtHolat.setSelection(1)
            "yakunlangan" -> dialogItemBinding.edtHolat.setSelection(2)
        }

        dialogItemBinding.btnSave.setOnClickListener {

            if (dialogItemBinding.edtTitle.text.isNotEmpty() && dialogItemBinding.edtDeadline.text.isNotEmpty()) {

                loadToDo()
                myToDo.sarlavha = dialogItemBinding.edtTitle.text.toString()
                myToDo.matn = dialogItemBinding.edtMatn.text.toString()
                myToDo.oxirgi_muddat = dialogItemBinding.edtDeadline.text.toString()
                myToDo.holat = dialogItemBinding.edtHolat.selectedItem.toString()

                APIClient.getApiService().updateToDo(
                    myToDo.id, myToDo)
                    .enqueue(object : Callback<MyToDo> {
                        override fun onResponse(call: Call<MyToDo>, response: Response<MyToDo>){

                            if (response.isSuccessful) {
                                Toast.makeText(this@MainActivity, "${myToDo.id} id o'zgartirildi", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                dialogItemBinding.addProgress.visibility = View.GONE
                            }
                        }

                        override fun onFailure(call: Call<MyToDo>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                            dialogItemBinding.addProgress.visibility = View.GONE
                        }
                    })

            }else {
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}