package com.example.demo.web;

import com.example.demo.data.Voiture;
import com.example.demo.service.Echantillon;
import com.example.demo.service.Statistique;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebTests {

    @MockBean
    Statistique statistique;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetStatistiques() throws Exception {
        Echantillon echantillon = new Echantillon(2, 15000);
        when(statistique.prixMoyen()).thenReturn(echantillon);

        mockMvc.perform(get("/statistique"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreDeVoitures").value(2))
                .andExpect(jsonPath("$.prixMoyen").value(15000));
    }

    @Test
    void testCreerVoiture() throws Exception {
        Voiture voiture = new Voiture("Peugeot", 10000);

        ArgumentCaptor<Voiture> captor = ArgumentCaptor.forClass(Voiture.class);

        mockMvc.perform(post("/voiture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voiture)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(statistique, times(1)).ajouter(captor.capture());

        Voiture capturedVoiture = captor.getValue();
        assertEquals(voiture.getNom(), capturedVoiture.getNom());
        assertEquals(voiture.getPrix(), capturedVoiture.getPrix());
    }

    @Test
    void testGetStatistiquesNoCars() throws Exception {
        when(statistique.prixMoyen()).thenThrow(ArithmeticException.class);

        mockMvc.perform(get("/statistique"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Aucune voiture disponible"));
    }
}
