package com.chsev.usuariorestservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UsuarioController {
    public static List<Usuario> lista = new ArrayList<>();


    @PostMapping("/login")
    Usuario login(@RequestBody Login login) {
        Usuario usuario = lista.stream()
                .filter(usu -> usu.getLogin().equals(login.getLogin()) && usu.getSenha().equals(login.getSenha()))
                .findAny().orElse(null);
        return usuario;
    }


    @GetMapping(value = "/usuarios/{id}")
    Usuario buscarPorId(@PathVariable("id") int id){
         Usuario usuario = lista.stream()
            .filter(usu -> usu.getId() == id  )
            .findAny().orElse(null);
        return usuario;
    }


    @GetMapping(value = "/usuarios")
    List<Usuario> listarTodos(){
        return lista;
    }


    @PostMapping("/usuarios")
    Usuario inserir(@RequestBody Usuario usuario) {
        lista.add(usuario);
        Usuario usr = lista.stream()
        .filter(usu -> (usu.getId() == usuario.getId() ) )
        .findAny().orElse(null);

        return usr;
    }


    @DeleteMapping("/usuarios/{id}")
    Usuario remover(@PathVariable("id") int id){
        Usuario usuario = lista.stream()
        .filter(usu -> (usu.getId() == id ) )
        .findAny().orElse(null);

        lista.removeIf(usu -> usu.getId() == id );

        return usuario;
    }


    @PutMapping("/usuarios/{id}")
    Usuario alterar( @PathVariable("id") int id, @RequestBody Usuario usuario ) {
        lista.removeIf(usu -> usu.getId() == usuario.getId() );
        lista.add(usuario);

        Usuario usr = lista.stream()
        .filter(usu -> (usu.getId() == usuario.getId() ) )
        .findAny().orElse(null);

        return usr;
    }


    static {
        lista.add(new Usuario(1, "administr", "admin", "admin", "ADMIN"));
        lista.add(new Usuario(2, "gerent", "gerente", "gerente", "GERENTE"));
        lista.add(new Usuario(3, "funcion", "func", "func", "FUNC"));
    }
}