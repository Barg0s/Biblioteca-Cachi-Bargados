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


    public static void comprobarData(String data, JSONArray llista) throws IllegalArgumentException {
        for (int i = 0; i < llista.length(); i++) {
            String[] dataSeparada = data.split("-");
            if (dataSeparada[0].length() < 4 || dataSeparada[1].length() < 2 || dataSeparada[2].length() < 2 ){
                throw new IllegalArgumentException("el format de la data no es correcte (YYYY-MM-DD)");
            }
            }
        }
    

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

    public static JSONObject buscarPrestecPerID(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject prestec = jsonArray.getJSONObject(i);
            if (prestec.getInt("id") == id) {
                return prestec;
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
            LocalDate dataDevolucio = dataPrestec.plusDays(7);
    
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

    public static void modificarPrestecs(int id, Scanner scanner) {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
    
            JSONObject prestecFiltrar = buscarPrestecPerID(prestecsArray, id);
            if (prestecFiltrar == null) {
                System.out.println("Préstec no trobat.");
                return;
            }
    
            System.out.println("Escriu la clau que vols modificar (devolucio): ");
            String clau = scanner.nextLine().trim();
    
            if (!clau.equals("devolucio")) {
                throw new IllegalArgumentException("Només es pot modificar la data de devolució.");
            }
    
            System.out.println("Introdueix el nou valor per a '" + clau + "': ");
            String nouValor = scanner.nextLine().trim();
            comprobarData(nouValor, prestecsArray);
            prestecFiltrar.put("dataDevolucio", nouValor);
    
            guardarJSON(prestecsArray, filepathPrestecs);
    
            System.out.println("Préstec actualitzat correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validació: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al accedir als fitxers: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperat: " + e.getMessage());
        }
    }
    public static void eliminarPrestec(int id,Scanner scanner) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            
            boolean existeix = false; 
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);
                int prestecId = prestec.getInt("id");
    
                if (id == prestecId) {
                    existeix = true; 
                    System.out.print("Vols eliminar el prestec?: ");
                    String validacio = scanner.nextLine().toLowerCase();
    
                    if (validacio.equals("si")) {
                        System.out.println("prestec eliminat correctament");
                        jsonArray.remove(i);
                        guardarJSON(jsonArray, filepathPrestecs);
                        return;
                        } else if (validacio.equals("no")) {
                            menus.clearScreen();
                            
                            menus.menuLlibres(scanner);                        
                    }
    
                }
            }
    
            if (!existeix) {
                System.out.println("Aquesta ID no existeix");
            }
            } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
}    
