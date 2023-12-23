package com.example.todousingcompose.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todousingcompose.util.Resource
import com.example.todousingcompose.util.Routes
import com.example.todousingcompose.viewmodel.PhoneAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhoneAuthScreen(
    activity: Activity,
    navController: NavController,
    viewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val phoneNumber = remember { mutableStateOf("") }
    var otpValue by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    if (loading) {
        Loader()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
            },
            leadingIcon = { Text(text = "+91") },
            label = { Text("Enter phone number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            supportingText = {
                if (phoneNumber.value.length != 10 && phoneNumber.value.isNotEmpty())
                    Text(text = "Enter a valid number!")
            },
            isError = phoneNumber.value.length != 10 && phoneNumber.value.isNotEmpty()
        )


        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                scope.launch(Dispatchers.Main) {
                    viewModel.createUserWithPhone(
                        phoneNumber.value,
                        activity
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                loading = false
                                Toast.makeText(
                                    activity.applicationContext,
                                    it.data,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Resource.Failure -> {
                                loading = false
                                Toast.makeText(
                                    activity.applicationContext,
                                    "${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Resource.Loading -> {
                                loading = true
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send OTP")
            }
        }


        Spacer(modifier = Modifier.height(80.dp))


        BasicTextField(
            value = otpValue,
            onValueChange = {
                if (it.length <= 6) {
                    otpValue = it
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            decorationBox = {
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(6) { index ->
                        val char = when {
                            index >= otpValue.length -> ""
                            else -> otpValue[index].toString()
                        }
                        Text(
                            modifier = Modifier
                                .width(40.dp)
                                .wrapContentHeight()
                                .border(
                                    1.dp,
                                    Color.Gray,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(2.dp),
                            text = char,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                scope.launch(Dispatchers.Main){
                    viewModel.signInWithCredential(
                        otpValue
                    ).collect{
                        when(it){
                            is Resource.Success->{
                                loading = false
                                Toast.makeText(
                                    activity.applicationContext,
                                    it.data,
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Routes.HOME_SCREEN)
                            }
                            is Resource.Failure->{
                                loading = false
                                Toast.makeText(
                                    activity.applicationContext,
                                    "${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Resource.Loading->{
                                loading = true
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verify")
            }
        }
    }
}

@Composable
fun Loader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}