package com.example.restapp

import com.google.gson.annotations.SerializedName

class Todo(@SerializedName("userId") var userId:Int,
           @SerializedName("id") var id:Int?,
           @SerializedName("title") var title:String,
           @SerializedName("completed") var completed:Boolean) {

}