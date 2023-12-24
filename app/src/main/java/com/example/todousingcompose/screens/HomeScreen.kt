package com.example.todousingcompose.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todousingcompose.model.FirestoreResponse
import com.example.todousingcompose.util.Resource
import com.example.todousingcompose.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val task = remember { mutableStateOf("") }
    val isInsert = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loading = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val res = viewModel.res.value
    val isRefresh = remember { mutableStateOf(false) }

    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ToDo List",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Button(
                    onClick = { isInsert.value = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add task")
                }
            }
        }

        if (loading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (isInsert.value) {
            AlertDialog(
                onDismissRequest = {
                    isInsert.value = false
                    task.value = ""
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add a task",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                },
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
                    Button(onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.insert(
                                FirestoreResponse(task.value)
                            ).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        loading.value = false
                                        isInsert.value = false
                                        isRefresh.value = true
                                        task.value = ""
                                        Toast.makeText(
                                            context,
                                            "Task added successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    is Resource.Failure -> {
                                        loading.value = false
                                        Toast.makeText(
                                            context,
                                            it.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    is Resource.Loading -> {
                                        loading.value = true
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        loading.value = false
                        isInsert.value = false
                        task.value = ""
                    }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }

        if (isRefresh.value) {
            isInsert.value = false
            isRefresh.value = false
            viewModel.getItems()
        }

        if (isUpdate.value) {
            Update(
                viewModel.updateData.value,
                isUpdate,
                viewModel,
                isRefresh
            )
        }

        if (res.data.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(res.data, key = {
                    it.key!!
                }) { user ->
                    ToDoItem(user = user, onUpdate = {
                        isUpdate.value = true
                        viewModel.setData(
                            FirestoreResponse(
                                key = user.key,
                                task = user.task
                            )
                        )
                    }) {
                        scope.launch {
                            viewModel.delete(user.key!!).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        loading.value = false
                                        isRefresh.value = true
                                        Toast.makeText(context, "Task deleted!", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    is Resource.Failure -> {
                                        loading.value = false
                                        Toast.makeText(
                                            context,
                                            it.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    Resource.Loading -> {
                                        loading.value = true
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        if (res.error.isNotEmpty())
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = res.error)
            }

        if (res.isLoading)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
    }
}

@Composable
fun ToDoItem(
    user: FirestoreResponse,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
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
                text = user.task!!,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                ),
                modifier = Modifier
                    .weight(3f)
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = { onUpdate() },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update task",

                        )
                }
                IconButton(
                    onClick = { onDelete() },
                    modifier = Modifier.align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
    item: FirestoreResponse,
    loading: MutableState<Boolean>,
    viewModel: HomeViewModel,
    isRefresh: MutableState<Boolean>
) {
    val task = remember { mutableStateOf(item.task) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val progress = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { loading.value = false },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Update task",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        },
        text = {
            TextField(
                value = task.value!!,
                onValueChange = {
                    task.value = it
                },
                placeholder = { Text(text = "Enter Task") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        },
        confirmButton = {
            Button(onClick = {
                scope.launch(Dispatchers.Main) {
                    viewModel.update(
                        FirestoreResponse(
                            key = item.key,
                            task = task.value
                        )
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                loading.value = false
                                progress.value = false
                                isRefresh.value = true
                                Toast.makeText(
                                    context, "Task updated!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Resource.Failure -> {
                                loading.value = false
                                progress.value = false
                                Toast.makeText(
                                    context,
                                    it.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Resource.Loading -> {
                                progress.value = true
                            }
                        }
                    }
                }
            }) {
                Text(text = "Update")
            }
        },
    )

    if (progress.value) {
        Dialog(onDismissRequest = { }) {
            CircularProgressIndicator()
        }
    }

}