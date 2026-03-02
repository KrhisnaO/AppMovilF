package com.example.appmovil.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

import com.google.firebase.firestore.SetOptions


class FirestoreUserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun createUserProfile(
        uid: String,
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        email: String,
        tipoDiscapacidad: String
    ): Result<Unit> {
        return try {

            val userDoc = hashMapOf(
                "uid" to uid,
                "nombre" to nombre,
                "apellidoPaterno" to apellidoPaterno,
                "apellidoMaterno" to apellidoMaterno,
                "email" to email,
                "tipoDiscapacidad" to tipoDiscapacidad,
                "role" to "user",
                "active" to true,
                "createdAt" to FieldValue.serverTimestamp(),
                "lastLoginAt" to FieldValue.serverTimestamp()
            )

            db.collection("users")
                .document(uid)
                .set(userDoc)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String) =
        db.collection("users").document(uid).get().await()

    suspend fun updateLastLogin(uid: String): Result<Unit> {
        return try {
            db.collection("users")
                .document(uid)
                .set(
                    mapOf("lastLoginAt" to FieldValue.serverTimestamp()),
                    SetOptions.merge()
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}