package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class Prestecs {
    public static final String filepathPrestecs = "JSON/prestecs.json";
    public static final String filepathUsers = "JSON/usuaris.json";
    public static final String filepathLlibres = "JSON/llibres.json";


    public static JSONObject buscarLlibrePerNom(JSONArray jsonArray, String titol) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject llibre = jsonArray.getJSONObject(i);
            if (llibre.getString("titol").equals(titol)) {
                return llibre;
            }
        }
        return null;
    }

    public static JSONObject buscarUsuariPerNom(JSONArray jsonArray, String nom,String cognom) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject usuari = jsonArray.getJSONObject(i);
            if (usuari.getString("nom").equals(nom) && usuari.getString("cognom").equals(cognom)) {
                return usuari;
            }
        }
        return null;
    }


    public static void guardarJSON(JSONArray jsonArray,String filepath) {
        try {
            Files.write(Paths.get(filepath), jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void afegirPrestec(String titol, String nom, String cognom) {
        try {
            String contentLlibres = new String(Files.readAllBytes(Paths.get(filepathLlibres)));
            JSONArray llibresArray = new JSONArray(contentLlibres);
    
            String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepathUsers)));
            JSONArray usuarisArray = new JSONArray(contentUsuaris);
    
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
    
            JSONObject llibre = buscarLlibrePerNom(llibresArray, titol);
            JSONObject usuari = buscarUsuariPerNom(usuarisArray, nom, cognom);
    
            if (llibre == null || usuari == null) {
                System.out.println("Llibre o usuari no trobat.");
                return;
            }
    
            int idLlibre = llibre.getInt("id");
            int idUsuari = usuari.getInt("id");
    
            LocalDate dataPrestec = LocalDate.now();
            LocalDate dataDevolucio = dataPrestec.plusDays(30);
    
            JSONObject prestec = new JSONObject();
            prestec.put("idLlibre", idLlibre);
            prestec.put("idUsuari", idUsuari);
            prestec.put("dataPrestec", dataPrestec.toString());
            prestec.put("dataDevolucio", dataDevolucio.toString());
    
            prestecsArray.put(prestec);
    
            guardarJSON(prestecsArray, filepathPrestecs);
            System.out.println("Préstec afegit correctament.");
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
}    