package com.example.appmovil.data


object UserStore {
    val usuarios = mutableListOf<Usuario>(
        Usuario("Elsa", "Pallo", "Naranjo", "elsa@email.com", "1234", "visual"),
        Usuario("Alan", "Brito", "Delgado", "alan@email.com", "abcd","visual"),
        Usuario("Armando", "Casas", "Madera", "armando@email.com", "1111", "visual"),
        Usuario("Aquiles", "Baeza", "Parra", "aquiles@email.com", "2222","visual"),
        Usuario("Susana", "Oria", "De la Fuente", "susana@email.com", "3333","visual")
    )

    private fun buscarPorCorreo(correo: String) =
        usuarios.find { it.correo.equals(correo.trim(), ignoreCase = true) }

    fun login(correo: String, password: String): Boolean {
        val usuario = buscarPorCorreo(correo)
        return usuario != null && usuario.password == password.trim()
    }

    fun registrarUsuario(usuario: Usuario): Boolean {
        val existe = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        if (existe) return false
        usuarios.add(usuario)
        return true
    }


    fun existeCorreo(correo: String): Boolean = buscarPorCorreo(correo) != null

}
