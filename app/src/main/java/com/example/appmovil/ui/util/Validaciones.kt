package com.example.appmovil.ui.util

import com.example.appmovil.data.Usuario

class Validaciones {

    fun validarNombre(texto: String): Boolean {
        return texto.trim().length >= 2
    }

    fun validarCorreo(correo: String): Boolean {
        return correo.contains("@") && correo.contains(".")
    }

    fun validarPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun validarTipoDiscapacidad(tipo: String): Boolean {
        return tipo.isNotBlank()
    }

    fun validarPasswordsIguales(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }


    fun validarUsuario(usuario: Usuario): Boolean {
        return validarNombre(usuario.nombre) &&
                validarNombre(usuario.apellidoPaterno) &&
                validarNombre(usuario.apellidoMaterno) &&
                validarCorreo(usuario.correo) &&
                validarPassword(usuario.password) &&
                validarTipoDiscapacidad(usuario.tipoDiscapacidad)
    }
}