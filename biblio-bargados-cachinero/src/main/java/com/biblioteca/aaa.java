package com.biblioteca;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class aaa {
public static void guardarJSON(JSONArray jsonArray,String filepath) {
    try {
        Files.write(Paths.get(filepath), jsonArray.toString(4).getBytes());
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}





    public static final String filepath = "JSON/llibres.json";

    public static JSONObject buscarLlibrePerId(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject llibre = jsonArray.getJSONObject(i);
            if (llibre.getInt("id") == id) {
                return llibre;
            }
        }
        return null;
    }



    public static void modificarLlibres(int id, String clau, String nouValor) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            JSONObject llibre = buscarLlibrePerId(jsonArray, id);
            if (llibre == null) {
                System.out.println("Aquesta ID no existeix.");
                return;
            }
    
            if (clau.equals("autors")) {
                JSONArray autors = llibre.getJSONArray("autors");
    
                String[] nousAutors = nouValor.split(",");
    
                boolean autorAfegit = false;
                for (String nouAutor : nousAutors) {
                    nouAutor = nouAutor.trim();
                    boolean autorExisteix = false;
    
                    for (int j = 0; j < autors.length(); j++) {
                        if (nouAutor.equalsIgnoreCase(autors.getString(j))) {
                            autorExisteix = true;
                            break;
                        }
                    }
    
                    if (!autorExisteix && !nouAutor.isEmpty()) {
                        autors.put(nouAutor); 
                        autorAfegit = true;
                    }
                }
    
                if (autorAfegit) {
                    guardarJSON(jsonArray, filepath);
                    System.out.println("Autors afegits correctament.");
                } else {
                    System.out.println("Tots els autors ja existeixen o la entrada és buida.");
                }
    
            } else if (clau.equals("id")) {
                System.out.println("No es pot modificar la ID.");
            } else if (clau.equals("autors") || clau.equals("titol")) {
                llibre.put(clau, nouValor);
                guardarJSON(jsonArray, filepath);
                System.out.println("Llibre modificat correctament.");
            }else{
                System.out.println("Aquesta clau no existeix");
            }
    
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
    

    public static void main(String[] args) {
        modificarLlibres(2, "id", "manuel,pepe");
    }
}
