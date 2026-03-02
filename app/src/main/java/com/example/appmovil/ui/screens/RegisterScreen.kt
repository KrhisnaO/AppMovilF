package com.example.appmovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import com.example.appmovil.ui.util.Validaciones
import com.example.appmovil.data.UserStore
import com.example.appmovil.data.Usuario


import com.example.appmovil.data.FirebaseUserRepository
import com.example.appmovil.data.FirestoreUserRepository
import com.google.firebase.auth.FirebaseAuthUserCollisionException
enum class MessageType {
    SUCCESS,
    ERROR
}

@Composable
fun RegisterScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var tipoDiscapacidad by remember { mutableStateOf("Habla") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC)))
    var messageType by remember { mutableStateOf(MessageType.SUCCESS) }

    val validaciones = Validaciones()

    // FIREBASE
    val authRepo = remember { FirebaseUserRepository() }
    val firestoreRepo = remember { FirestoreUserRepository() }

    val canSubmit = nombre.isNotBlank() &&
            apellidoPaterno.isNotBlank() &&
            apellidoMaterno.isNotBlank() &&
            correo.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            aceptaTerminos

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    containerColor = if (messageType == MessageType.SUCCESS)
                        Color(0xFF2E7D32) else Color(0xFFC62828),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        fontSize = 18.sp,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = CircleShape,
                                color = Color(0xFFF0F0F0)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.padding(15.dp),
                                    tint = Color(0xFF6A11CB)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row {
                            OutlinedTextField(
                                value = apellidoPaterno,
                                onValueChange = { apellidoPaterno = it },
                                label = { Text("Apellido Paterno") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(Modifier.width(8.dp))
                            OutlinedTextField(
                                value = apellidoMaterno,
                                onValueChange = { apellidoMaterno = it },
                                label = { Text("Apellido Materno") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Text("Tipo de discapacidad sensorial:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tipoDiscapacidad == "Habla",
                                onClick = { tipoDiscapacidad = "Habla" }
                            )
                            Text("Habla", fontSize = 18.sp)
                            Spacer(Modifier.width(16.dp))
                            RadioButton(
                                selected = tipoDiscapacidad == "Visual",
                                onClick = { tipoDiscapacidad = "Visual" }
                            )
                            Text("Visual", fontSize = 18.sp)
                        }

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation =
                                if (showPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar contraseña") },
                            visualTransformation =
                                if (showConfirmPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = aceptaTerminos,
                                onCheckedChange = { aceptaTerminos = it }
                            )
                            Text("Acepto los términos y condiciones", fontSize = 18.sp)
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            Button(
                                onClick = {
                                    val correoTrim = correo.trim()

                                    val nuevoUsuario = Usuario(
                                        nombre = nombre.trim(),
                                        apellidoPaterno = apellidoPaterno.trim(),
                                        apellidoMaterno = apellidoMaterno.trim(),
                                        correo = correoTrim,
                                        password = password,
                                        tipoDiscapacidad = tipoDiscapacidad
                                    )

                                    when {
                                        !validaciones.validarUsuario(nuevoUsuario) -> {
                                            messageType = MessageType.ERROR
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Datos inválidos. Revise el formulario")
                                            }
                                        }

                                        !validaciones.validarPasswordsIguales(password, confirmPassword) -> {
                                            messageType = MessageType.ERROR
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Las contraseñas no coinciden")
                                            }
                                        }

                                        else -> {
                                            isLoading = true
                                            scope.launch {
                                                delay(1500)

                                                // 1) REGISTRO EN FIREBASE AUTH
                                                val regResult = authRepo.register(correoTrim, password)

                                                regResult.fold(
                                                    onSuccess = { uid ->
                                                        // 2) CREAR PERFIL EN FIRESTORE
                                                        val profileResult = firestoreRepo.createUserProfile(
                                                            uid = uid,
                                                            nombre = nombre.trim(),
                                                            apellidoPaterno = apellidoPaterno.trim(),
                                                            apellidoMaterno = apellidoMaterno.trim(),
                                                            email = correoTrim,
                                                            tipoDiscapacidad = tipoDiscapacidad
                                                        )

                                                        isLoading = false

                                                        profileResult.fold(
                                                            onSuccess = {
                                                                messageType = MessageType.SUCCESS
                                                                snackbarHostState.showSnackbar("Usuario registrado con éxito")
                                                                delay(1000)
                                                                navController.popBackStack()
                                                            },
                                                            onFailure = { e ->
                                                                messageType = MessageType.ERROR
                                                                snackbarHostState.showSnackbar(
                                                                    "Error al crear perfil: ${e.message ?: "Intenta nuevamente"}"
                                                                )
                                                            }
                                                        )
                                                    },
                                                    onFailure = { e ->
                                                        isLoading = false
                                                        messageType = MessageType.ERROR

                                                        val msg = when (e) {
                                                            is FirebaseAuthUserCollisionException ->
                                                                "El correo ya está registrado"
                                                            else ->
                                                                "Error al registrar: ${e.message ?: "Intenta nuevamente"}"
                                                        }
                                                        snackbarHostState.showSnackbar(msg)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                },
                                enabled = canSubmit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Registrar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

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
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}