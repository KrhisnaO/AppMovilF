package com.example.appmovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmovil.navigation.NavRouter
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.appmovil.ui.util.Validaciones

import com.example.appmovil.data.UserStore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await


@Composable
fun LoginScreen(navController: NavController) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2575FC), Color(0xFF6A11CB))
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var messageType by remember { mutableStateOf(MessageType.SUCCESS) }

    val validaciones = Validaciones()

    // Firebase Auth 
    val auth = remember { FirebaseAuth.getInstance() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                val backgroundColor = when (messageType) {
                    MessageType.SUCCESS -> Color(0xFF2E7D32)
                    MessageType.ERROR -> Color(0xFFC62828)
                }

                Snackbar(
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
                .padding(18.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation =
                                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword)
                                            Icons.Filled.VisibilityOff
                                        else
                                            Icons.Filled.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val emailTrim = email.trim()

                                when {
                                    emailTrim.isBlank() -> {
                                        messageType = MessageType.ERROR
                                        scope.launch { snackbarHostState.showSnackbar("Completa tu correo") }
                                    }

                                    !validaciones.validarCorreo(emailTrim) -> {
                                        messageType = MessageType.ERROR
                                        scope.launch { snackbarHostState.showSnackbar("Correo inválido") }
                                    }

                                    password.isBlank() -> {
                                        messageType = MessageType.ERROR
                                        scope.launch { snackbarHostState.showSnackbar("Completa tu contraseña") }
                                    }

                                    !validaciones.validarPassword(password) -> {
                                        messageType = MessageType.ERROR
                                        scope.launch {
                                            snackbarHostState.showSnackbar("La contraseña debe tener al menos 8 caracteres")
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            try {
                                                auth.signInWithEmailAndPassword(emailTrim, password).await()

                                                messageType = MessageType.SUCCESS
                                                snackbarHostState.showSnackbar("Inicio de sesión correcto")

                                                navController.navigate(NavRouter.HomeScreen.route) {
                                                    popUpTo(NavRouter.LoginScreen.route) { inclusive = true }
                                                    launchSingleTop = true
                                                }

                                            } catch (e: Exception) {
                                                messageType = MessageType.ERROR
                                                val msg = when (e) {
                                                    is FirebaseAuthInvalidUserException ->
                                                        "Usuario no registrado"
                                                    is FirebaseAuthInvalidCredentialsException ->
                                                        "Correo o contraseña incorrectos"
                                                    else ->
                                                        "Error al iniciar sesión: ${e.message ?: "Intenta nuevamente"}"
                                                }
                                                snackbarHostState.showSnackbar(msg)
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Ingresar",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { navController.navigate(NavRouter.RecoverScreen.route) },
                            color = Color(0xFF2575FC)
                        )
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Volver al inicio",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5090FD)
                            )
                        }
                    }
                }
            }
        }
    }
}