package com.example.demo.service;

import com.example.demo.data.Voiture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatistiqueTests {

    private StatistiqueImpl statistique;

    @BeforeEach
    public void setUp() {
        statistique = new StatistiqueImpl();
    }

    @Test
    public void testPrixMoyenAvecDeuxVoitures() {
        Voiture v1 = new Voiture("Peugeot", 10000);
        Voiture v2 = new Voiture("Renault", 20000);

        statistique.ajouter(v1);
        statistique.ajouter(v2);

        Echantillon echantillon = statistique.prixMoyen();

        assertEquals(2, echantillon.getNombreDeVoitures());
        assertEquals(15000, echantillon.getPrixMoyen());
    }

    @Test
    public void testPrixMoyenAvecUneVoiture() {
        Voiture v1 = new Voiture("Tesla", 50000);

        statistique.ajouter(v1);

        Echantillon echantillon = statistique.prixMoyen();

        assertEquals(1, echantillon.getNombreDeVoitures());
        assertEquals(50000, echantillon.getPrixMoyen());
    }

    @Test
    public void testPrixMoyenSansVoiture() {
        assertThrows(ArithmeticException.class, () -> {
            statistique.prixMoyen();
        });
    }
}
