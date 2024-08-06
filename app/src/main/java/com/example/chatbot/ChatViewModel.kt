package com.example.chatbot

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.ui.theme.Constants
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup

class ChatViewModel : ViewModel()
{
    val messagehistory by lazy {
        mutableStateListOf<message>()
    }
    val generativeModel: GenerativeModel = GenerativeModel(
        apiKey = Constants.apiKey,
        modelName = "gemini-1.5-flash")





    fun convertMarkdownToHtml(markdown: String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(markdown)
        val renderer = HtmlRenderer.builder().build()
        return renderer.render(document)
    }
    fun convertHtmlToPlainText(html: String): String {
        return Jsoup.parse(html).text()
    }

    fun convertMarkdownToPlainText(markdown: String): String {
        val html = convertMarkdownToHtml(markdown)
        return convertHtmlToPlainText(html)
    }

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
               messagehistory.add(message(convertMarkdownToPlainText(response.text.toString()),"model"))
           }
           catch (e: Exception){
               messagehistory.removeLast()
               messagehistory.add(message("Error! Please Check Your Internet Connection","model"))
           }

        }
    }
}