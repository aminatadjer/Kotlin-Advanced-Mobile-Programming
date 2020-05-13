package com.example.restapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var todoList: ArrayList<Todo> = arrayListOf()
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getStart()
        todoListAdapter = TodoListAdapter(this,todoList)
        mylist.adapter = todoListAdapter
        all.setOnClickListener {
            getStart()
        }
        add.setOnClickListener {
            create(Todo(1,null,editText.text.toString(),false))
        }
        completed.setOnClickListener {
           getCompletedTodos()
        }
        waiting.setOnClickListener {
            getNonCompletedTodos()
        }
        }


    private fun getStart(){
        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getAllTodos().enqueue(object: Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>> , response: retrofit2.Response<List<Todo>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Todo>? = response.body()
                    if (listBody != null) {
                        todoList.clear()
                        todoList.addAll(listBody)
                        todoListAdapter.notifyDataSetChanged()
                    }
                    Toast.makeText(this@MainActivity, "Get all work!", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<Todo>> , t: Throwable) {
                Toast.makeText(this@MainActivity, "Echec", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun create(todo: Todo){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Service>(Service::class.java)
        service.createTodo(todo).enqueue(object: Callback<Todo> {
            override fun onResponse(call: Call<Todo> , response: retrofit2.Response<Todo>?) {
                if ((response != null) && (response.code() == 200)) {
                    todoList.reverse()
                    response.body()?.let { todoList.add(it) }
                    todoList.reverse()
                    todoListAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Create work!", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Todo> , t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getCompletedTodos(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getCompleted().enqueue(object: Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>> , response: retrofit2.Response<List<Todo>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Todo>? = response.body()
                    if (listBody != null) {
                        todoList.clear()
                        todoList.addAll(listBody)
                        todoListAdapter.notifyDataSetChanged()
                    }
                    Toast.makeText(this@MainActivity, "Get completed work!", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<Todo>> , t: Throwable) {
                Toast.makeText(this@MainActivity, "Echec", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getNonCompletedTodos(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getNonCompleted().enqueue(object: Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>> , response: retrofit2.Response<List<Todo>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Todo>? = response.body()
                    if (listBody != null) {
                        todoList.clear()
                        todoList.addAll(listBody)
                        todoListAdapter.notifyDataSetChanged()
                    }
                    Toast.makeText(this@MainActivity, "Get Non completed work!", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<Todo>> , t: Throwable) {
                Toast.makeText(this@MainActivity, "Echec", Toast.LENGTH_LONG).show()
            }
        })
    }
}
