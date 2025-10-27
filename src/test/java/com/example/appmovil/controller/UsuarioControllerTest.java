package com.example.appmovil.controller;

import com.example.appmovil.model.Cupo;
import com.example.appmovil.model.Usuario;
import com.example.appmovil.service.CupoService;
import com.example.appmovil.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CupoService cupoService;

    @Test
    public void testGetCupo() throws Exception {
        Usuario u = new Usuario("testuser");
        u.setId(1L);

        Cupo c = new Cupo("555666777", 12.3, 1.5, "android");
        c.setId(10L);
        c.setUsuario(u);

        when(usuarioService.findByNombre("testuser")).thenReturn(u);
        when(cupoService.getCupoByUsuarioAndMovil(u, "555666777")).thenReturn(c);

        mockMvc.perform(get("/usuarios/testuser/cupos/555666777"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movil").value("555666777"))
                .andExpect(jsonPath("$.saldo").value(12.3))
                .andExpect(jsonPath("$.datos").value(1.5))
                .andExpect(jsonPath("$.plataforma").value("android"));
    }

    @Test
    public void testCreateCupo() throws Exception {
        Usuario u = new Usuario("testuser");
        u.setId(1L);

        Cupo c = new Cupo("555666777", 12.3, 1.5, "android");
        c.setId(11L);
        c.setUsuario(u);

        when(cupoService.createCupo("testuser", "555666777", 12.3, 1.5, "android")).thenReturn(c);

        mockMvc.perform(post("/usuarios/testuser/cupos")
                        .param("movil", "555666777")
                        .param("saldo", "12.3")
                        .param("datos", "1.5")
                        .param("plataforma", "android"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movil").value("555666777"));
    }
}
