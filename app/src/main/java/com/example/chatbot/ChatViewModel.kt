package com.example.chatbot

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.ui.theme.Constants
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel()
{
    val messagehistory by lazy {
        mutableStateListOf<message>()
    }
    val generativeModel: GenerativeModel = GenerativeModel(apiKey = Constants.apiKey, modelName = "gemini-pro")



    fun sendmessage(question :String){
        viewModelScope.launch {
           try {
               val chat = generativeModel.startChat(
                   history = messagehistory.map {
                       content(it.role){text(it.message )}
                   }.toList()
               )
               messagehistory.add(message(question,"user"))
               messagehistory.add(message("Typing.....","model"))
               val response = chat.sendMessage(question)
               messagehistory.removeLast()
               messagehistory.add(message(response.text.toString(),"model"))
           }
           catch (e: Exception){
               messagehistory.removeLast()
               messagehistory.add(message("Error! Please Check Your Internet Connection","model"))
           }

        }
    }
}