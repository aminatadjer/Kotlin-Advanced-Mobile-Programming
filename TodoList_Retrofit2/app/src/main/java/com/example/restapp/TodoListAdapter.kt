package com.example.restapp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class TodoListAdapter(var context : Context?, var todos: ArrayList<Todo>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val item= layoutInflater.inflate(R.layout.todo_item, parent, false)

        val current = todos[position]
        var todoId = item.findViewById<TextView>(R.id.todoId)
        var taskName = item.findViewById<TextView>(R.id.taskName)
        var done = item.findViewById<CheckBox>(R.id.done)
        todoId.text = current.id.toString()
        taskName.text = current.title
        done.isChecked = current.completed
        return item
    }

    override fun getCount(): Int {
        return  todos.count()
    }
    override fun getItem(position: Int): Any {
        return todos[position]
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

}