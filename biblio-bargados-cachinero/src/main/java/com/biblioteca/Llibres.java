package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Llibres {

    public static final String filepath = "JSON/llibres.json";
    public static final String filepathPrestecs = "JSON/prestecs.json";

public static void guardarJSON(JSONArray jsonArray,String filepath) {
    try {
        Files.write(Paths.get(filepath), jsonArray.toString(4).getBytes());
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}


    public static JSONObject buscarLlibrePerId(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject llibre = jsonArray.getJSONObject(i);
            if (llibre.getInt("id") == id) {
                return llibre;
            }
        }
        return null;
    }

    public static void comprobarLlibre(String NouTitol, JSONArray llista) throws IllegalArgumentException {
        for (int i = 0; i < llista.length(); i++) {
            JSONObject llibre = llista.getJSONObject(i);
            String titol = llibre.getString("titol");

            if (NouTitol.equals(titol)) {
                throw new IllegalArgumentException("El titol ja existeix.");
            
            }
        }
    }

    public static void afegirLlibres(String titol,String[] autors) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            comprobarLlibre(titol, jsonArray);
            int id = jsonArray.length() + 1;
            JSONObject Llibre = new JSONObject();
            Llibre.put("id", id);
            Llibre.put("titol", titol);


            JSONArray autorsArray = new JSONArray();
            for (String autor : autors){
                autorsArray.put(autor);
            }
            Llibre.put("autors", autorsArray);
            jsonArray.put(Llibre);
            guardarJSON(jsonArray, filepath);
            System.out.println("Llibre afegit correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }

    public static void modificarLlibres(int id, Scanner scanner) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            JSONObject llibre = buscarLlibrePerId(jsonArray, id);
            if (llibre == null) {
                System.out.println("Aquesta ID no existeix.");
                return;
            }
    
            System.out.println("Escriu la clau que vols modificar (nom/cognom/telefon): ");
            String clau = scanner.nextLine();
    
            if (clau.equals("autors")) {
                JSONArray autors = llibre.getJSONArray("autors");
                System.out.println("Escriu el nou autor:");
                String nou_autor = scanner.nextLine();
    
                boolean autorExisteix = false;
                for (int j = 0; j < autors.length(); j++) {
                    String autor = autors.getString(j);
                    if (nou_autor.equals(autor)) {
                        autorExisteix = true;
                        break; 
                    }
                }
    
                if (autorExisteix) {
                    System.out.println("Aquest autor ja existeix");
                } else {
                    autors.put(nou_autor); 
                    guardarJSON(jsonArray, filepath);
                    System.out.println("Autor afegit correctament.");
                }
            } else if (clau.equals("id")) {
                System.out.println("No es pot modificar la ID.");
            } else {
                System.out.println("Escriu el nou valor:");
                String nouValor = scanner.nextLine();
                llibre.put(clau, nouValor);
                guardarJSON(jsonArray, filepath);
                System.out.println("Llibre modificat correctament.");
            }
    
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
    public static void eliminarLlibres(int id,Scanner scanner) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            
            boolean existeix = false; 
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
                int id_llibre = llibre.getInt("id");
    
                if (id == id_llibre) {
                    existeix = true; 
                    String nom = llibre.getString("nom");
                    String cognom = llibre.getString("cognom");
                    System.out.print("Vols eliminar a l'Llibre" + nom + cognom + "?: ");
                    String validacio = scanner.nextLine().toLowerCase();
    
                    if (validacio.equals("si")) {
                        System.out.println("Llibre eliminat correctament");
                        jsonArray.remove(i);
                        guardarJSON(jsonArray, filepath);
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
    public static void LlistarLlibres() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
    
                int id = llibre.getInt("id");
                String titol = llibre.getString("titol");
    
                JSONArray autorsArray = llibre.getJSONArray("autors");
                ArrayList<String> autors = new ArrayList<>();
                for (int j = 0; j < autorsArray.length(); j++) {
                    autors.add(autorsArray.getString(j));
                }
    
                System.out.println("ID: " + id);
                System.out.println("Títol: " + titol);
                System.out.println("Autors: " + String.join(", ", autors));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

        
    public static void LlistarPerTitol(String titolFiltar) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            boolean titolTrobat = false; 
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
    
                int id = llibre.getInt("id");
                String titol = llibre.getString("titol");
                if (titol.contains(titolFiltar)) {
                    titolTrobat = true;
    
                    JSONArray autorsArray = llibre.getJSONArray("autors");
                    ArrayList<String> autors = new ArrayList<>();
                    for (int j = 0; j < autorsArray.length(); j++) {
                        autors.add(autorsArray.getString(j));
                    }
    
                    System.out.println("ID: " + id);
                    System.out.println("Títol: " + titol);
                    System.out.println("Autors: " + String.join(", ", autors));
                }
            }
            
            if (!titolTrobat) {
                System.out.println("El títol no existeix.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void LlistarPerAutor(String autorFiltrat) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            boolean autorTrobat = false; 
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
                JSONArray autors = llibre.getJSONArray("autors");
    
                for (int j = 0; j < autors.length(); j++) {
                    String autor = autors.getString(j);
                    if (autorFiltrat.equals(autor)) {
                        autorTrobat = true; 
                        int id = llibre.getInt("id");
                        String titol = llibre.getString("titol");
    
                        ArrayList<String> autorsnous = new ArrayList<>();
                        for (int x = 0; x < autors.length(); x++) {
                            autorsnous.add(autors.getString(x));
                        }
    
                        System.out.println("ID: " + id);
                        System.out.println("Títol: " + titol);
                        System.out.println("Autors: " + String.join(", ", autorsnous));
                        break; 
                    }
                }
            }
    
            if (!autorTrobat) {
                System.out.println("L'autor no existeix.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void LlistarLlibresPrestec(int id) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            String prestecsContent = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(prestecsContent);
    
            boolean foundPrestec = false;
    
            for (int j = 0; j < prestecsArray.length(); j++) {
                JSONObject prestec = prestecsArray.getJSONObject(j);
                int idLlibrePrestec = prestec.getInt("idLlibre");
    
                if (idLlibrePrestec == id) {
                    foundPrestec = true;
    
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject llibre = jsonArray.getJSONObject(i);
                        if (llibre.getInt("id") == id) {
    
                            String titol = llibre.getString("titol");
    
                            JSONArray autorsArray = llibre.getJSONArray("autors");
                            ArrayList<String> autors = new ArrayList<>();
                            for (int k = 0; k < autorsArray.length(); k++) {
                                autors.add(autorsArray.getString(k));
                            }
                            String dataPrestec = prestec.getString("dataPrestec");
                            String dataDevolucio = prestec.getString("dataDevolucio");
                            System.out.println("ID: " + id);
                            System.out.println("TÃ­tol: " + titol);
                            System.out.println("Autors: " + String.join(", ", autors));
                            System.out.println("Data de prestec: " + dataPrestec);
                            System.out.println("Data de devolucio: " + dataDevolucio);
                            break; 
                        }
                    }
                }
            }
    
            if (!foundPrestec) {
                System.out.println("No s'han trobat préstecs per al llibre amb ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}    