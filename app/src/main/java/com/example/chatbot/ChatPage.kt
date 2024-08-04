package com.example.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.ui.theme.Pink80
import com.example.chatbot.ui.theme.Purple40
import com.example.chatbot.ui.theme.modelcolor
import com.example.chatbot.ui.theme.usercolor


@Composable
fun chatPage(modifier: Modifier , viewModel: ChatViewModel){
    Column(modifier = modifier) {

        appheader()
        Messagelist(modifier=Modifier.weight(1f) ,viewModel.messagehistory )
        messageinput(onmessagesend ={
        viewModel   .sendmessage(it)
        } )
    }
}

@Composable
fun messageinput(onmessagesend:(String)-> Unit){
    var text by remember {
        mutableStateOf("")
    }
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(  modifier = Modifier.weight(1f) ,value = text, onValueChange ={text=it} )
                IconButton(onClick = {
                   if(text.isNotEmpty()) {
                        onmessagesend(text)
                        text = ""
                    }}
                ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message ")
                }
            }
}

@Composable
fun appheader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)){
       Column(modifier = Modifier.fillMaxWidth() , horizontalAlignment = Alignment.CenterHorizontally) {




        Text( modifier = Modifier.padding(16.dp)  ,text = "ChatBot", fontSize = 40.sp, textAlign = TextAlign.Center )
    }}
}

@Composable
fun Messagelist(modifier: Modifier , messagelist: List<message>){


        LazyColumn(modifier, reverseLayout = true) {
            items(messagelist.reversed()) { message ->
                chat(message = message)
            }

        }
    }

@Composable
fun chat(message: message){
    val isModel = message.role=="model"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Box(modifier = Modifier
                .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                .padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp, bottom = 8.dp, top = 8.dp
                )
                .clip(RoundedCornerShape(48f))
                .background(
                    color = if (isModel) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.primaryContainer
                )
                .padding(16.dp)) {


            Text(text = message.message, fontWeight = FontWeight.W700, color = Color.White)
        }}
    }
}