package com.example.appmovil.integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.appmovil.model.Cupo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EndToEndIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void testEndToEndCupoCreationAndRetrieval() {
        // Test data
        String username = "integrationTestUser";
        String movil = "123456789";
        double saldo = 100.0;
        double datos = 5.0;
        String plataforma = "android";

        // Crear POST URL
        String postUrl = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host("localhost")
            .port(port)
            .path("/usuarios/{username}/cupos")
            .queryParam("movil", movil)
            .queryParam("saldo", saldo)
            .queryParam("datos", datos)
            .queryParam("plataforma", plataforma)
            .buildAndExpand(username)
            .toUriString();

        // Crear cupo
        ResponseEntity<Cupo> postResponse = restTemplate.postForEntity(
            postUrl,
            null,
            Cupo.class
        );

        // Verificar respuesta POST
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode(), "Cupo should be created successfully");
        
        Cupo createdCupo = postResponse.getBody();
        assertNotNull(createdCupo, "Response body should not be null");
        
        // Verificar detalles del cupo creado
        assertAll("Created cupo verification",
            () -> assertEquals(movil, createdCupo.getMovil(), "Mobile number should match"),
            () -> assertEquals(saldo, createdCupo.getSaldo(), "Balance should match"),
            () -> assertEquals(datos, createdCupo.getDatos(), "Data amount should match"),
            () -> assertEquals(plataforma, createdCupo.getPlataforma(), "Platform should match")
        );

        // Get URL para GET request
        String getUrl = createURLWithPort("/usuarios/" + username + "/cupos/" + movil);

        // Recuperar cupo
        ResponseEntity<Cupo> getResponse = restTemplate.getForEntity(
            getUrl,
            Cupo.class
        );

        // Verificar GET response
        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Should successfully retrieve cupo");
        
        Cupo retrievedCupo = getResponse.getBody();
        assertNotNull(retrievedCupo, "Retrieved cupo should not be null");
        
        // Verificar detalles del cupo recuperado
        assertAll("Retrieved cupo verification",
            () -> assertEquals(movil, retrievedCupo.getMovil(), "Retrieved mobile number should match"),
            () -> assertEquals(saldo, retrievedCupo.getSaldo(), "Retrieved balance should match"),
            () -> assertEquals(datos, retrievedCupo.getDatos(), "Retrieved data amount should match"),
            () -> assertEquals(plataforma, retrievedCupo.getPlataforma(), "Retrieved platform should match")
        );
    }
}