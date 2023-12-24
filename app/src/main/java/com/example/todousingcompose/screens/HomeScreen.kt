package com.example.todousingcompose.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todousingcompose.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val task = remember { mutableStateOf("") }
    val isInsert = remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isInsert.value = true
                },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item"
                )
            }
        }
    ) {

        if (isInsert.value) {
            AlertDialog(
                onDismissRequest = { isInsert.value = false },
                text = {
                    TextField(
                        value = task.value,
                        onValueChange = {
                            task.value = it
                        },
                        placeholder = { Text(text = "Task") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                },
                confirmButton = {
                    // TODO
                    Button(onClick = { isInsert.value = false }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    // TODO
                    Button(onClick = { isInsert.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }

        LazyColumn (
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(getSampleTaskList()) { task ->
                ToDoItem(task = task)
            }
        }

    }
}

@Composable
fun ToDoItem(task: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                ),
                modifier = Modifier
                    .weight(3f)
                    .padding(8.dp)
            )
            Row (
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit item",

                        )
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete item",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

fun getSampleTaskList(): MutableList<String> {
    val list = mutableListOf<String>()
    list.add("Grocery Shopping.")
    list.add("Feeding the pets.")
    list.add("Take the dog out for a walk.")
    list.add("Buy treats for the pets on the way back home.")
    list.add("Solve 4 questions on Leetcode")
    list.add("Remember to drink enough water!")
    list.add("Go for a walk. Alone time.")
    list.add("Prepare food.")
    return list
}