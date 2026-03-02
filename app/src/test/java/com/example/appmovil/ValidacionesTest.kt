package com.example.appmovil

import com.example.appmovil.ui.util.Validaciones
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ValidacionesTest {

    private lateinit var validaciones: Validaciones

    @Before
    fun setUp() {
        validaciones = Validaciones()
    }

    @Test
    fun validarCorreo_valido() {
        assertTrue(validaciones.validarCorreo("usuario@email.com"))
    }

    @Test
    fun validarCorreo_invalido() {
        assertFalse(validaciones.validarCorreo("usuarioemail.com"))
    }

    @Test
    fun validarPassword_valido() {
        assertTrue(validaciones.validarPassword("12345678"))
    }

    @Test
    fun validarPassword_invalido() {
        assertFalse(validaciones.validarPassword("1234"))
    }

    @Test
    fun validarPasswordsIguales_true() {
        assertTrue(validaciones.validarPasswordsIguales("12345678", "12345678"))
    }

    @Test
    fun validarPasswordsIguales_false() {
        assertFalse(validaciones.validarPasswordsIguales("12345678", "87654321"))
    }
}