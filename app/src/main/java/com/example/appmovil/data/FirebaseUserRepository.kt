package com.example.appmovil.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth
                .signInWithEmailAndPassword(email.trim(), password)
                .await()

            val uid = result.user?.uid
                ?: throw Exception("No se pudo obtener UID")

            Result.success(uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = auth
                .createUserWithEmailAndPassword(email.trim(), password)
                .await()

            val uid = result.user?.uid
                ?: throw Exception("No se pudo obtener UID")

            Result.success(uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    fun currentUid(): String? = auth.currentUser?.uid
}