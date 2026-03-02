package com.example.appmovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

import com.example.appmovil.data.UserStore
import com.example.appmovil.ui.util.Validaciones
import com.example.appmovil.data.FirebaseUserRepository

@Composable
fun RecoverScreen(navController: NavController) {

    var correo by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2575FC), Color(0xFF6A11CB))
    )
    val validaciones = Validaciones()

    // MENSAJE CORRECTO/INCORRECTO
    var messageType by remember { mutableStateOf(MessageType.SUCCESS) }

    // FIREBASE
    val authRepo = remember { FirebaseUserRepository() }

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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Recuperar Contraseña",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "Ingresa tu correo para recuperar tu contraseña",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val correoTrim = correo.trim()

                                when {
                                    correoTrim.isBlank() -> {
                                        messageType = MessageType.ERROR
                                        scope.launch { snackbarHostState.showSnackbar("Ingresa el correo") }
                                    }

                                    !validaciones.validarCorreo(correoTrim) -> {
                                        messageType = MessageType.ERROR
                                        scope.launch { snackbarHostState.showSnackbar("Correo inválido") }
                                    }

                                    else -> {
                                        scope.launch {
                                            val result = authRepo.sendPasswordReset(correoTrim)

                                            result.fold(
                                                onSuccess = {
                                                    messageType = MessageType.SUCCESS
                                                    snackbarHostState.showSnackbar("Enlace enviado al correo")
                                                    navController.popBackStack()
                                                },
                                                onFailure = { e ->
                                                    messageType = MessageType.ERROR
                                                    snackbarHostState.showSnackbar(
                                                        "No se pudo enviar el enlace: ${e.message ?: "Intenta nuevamente"}"
                                                    )
                                                }
                                            )
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
                                "Enviar enlace",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Volver a inicio",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}