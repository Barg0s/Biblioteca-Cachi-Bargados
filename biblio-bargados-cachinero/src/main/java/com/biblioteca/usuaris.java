package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class usuaris {

    public static final String filepath = "JSON/usuaris.json";

    public static void guardarJSON(JSONArray jsonArray, String filepath) {
        try (PrintWriter out = new PrintWriter(filepath)) {
            out.write(jsonArray.toString(4)); 
            out.flush();
        } catch (Exception e) {
            System.out.println("No s'ha pogut guardar el fitxer: " + e.getMessage());
        }
    }

    public static JSONObject buscarUsuariPerId(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getInt("id") == id) {
                return user;
            }
        }
        return null;
    }

    public static void comprobarTelefon(int num, JSONArray llista) throws IllegalArgumentException {
        for (int i = 0; i < llista.length(); i++) {
            JSONObject user = llista.getJSONObject(i);
            int telefon = user.getInt("telefon");
            int lenTelefon = String.valueOf(num).length();

            if (num == telefon) {
                throw new IllegalArgumentException("El número de telèfon ja existeix.");
            } else if (lenTelefon != 9) {
                throw new IllegalArgumentException("El número de telèfon no és correcte.");
            }
        }
    }

    public static void afegirUsuaris(String nom, String cognom, int telefon) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);

            comprobarTelefon(telefon, jsonArray);

            int id = jsonArray.length() + 1;
            JSONObject usuari = new JSONObject();
            usuari.put("id", id);
            usuari.put("nom", nom);
            usuari.put("cognom", cognom);
            usuari.put("telefon", telefon);

            jsonArray.put(usuari);
            guardarJSON(jsonArray, filepath);
            System.out.println("Usuari afegit correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }

    public static void modificarUsuaris(int id, Scanner scanner) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);

            JSONObject user = buscarUsuariPerId(jsonArray, id);
            if (user == null) {
                System.out.println("Aquesta ID no existeix.");
                return;
            }

            System.out.println("Escriu la clau que vols modificar (nom/cognom/telefon): ");
            String clau = scanner.nextLine();

            if (clau.equals("telefon")) {
                System.out.println("Escriu el nou valor:");
                while (!scanner.hasNextInt()) {
                    System.out.println("El valor ha de ser un número.");
                    scanner.next();
                }
                int nouTelefon = scanner.nextInt();
                scanner.nextLine(); 
                comprobarTelefon(nouTelefon, jsonArray);
                user.put(clau, nouTelefon);
            } else if (clau.equals("id")) {
                System.out.println("No es pot modificar la ID.");
            } else if (clau.equals("nom")||clau.equals("cognom")){
                System.out.println("Escriu el nou valor:");
                String nouValor = scanner.nextLine();
                user.put(clau, nouValor);
            }
            
            else {
                System.out.println("No es pot modificar aquesta opcio.");
            }

            guardarJSON(jsonArray, filepath);
            System.out.println("Usuari modificat correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }

    public static void eliminarUsuaris(int id,Scanner scanner) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            
            boolean existeix = false; 
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = jsonArray.getJSONObject(i);
                int id_user = user.getInt("id");
    
                if (id == id_user) {
                    existeix = true; 
                    String nom = user.getString("nom");
                    String cognom = user.getString("cognom");
                    System.out.print("Vols eliminar a l'usuari" + nom + cognom + "?: ");
                    String validacio = scanner.nextLine().toLowerCase();
    
                    if (validacio.equals("si")) {
                        System.out.println("Usuari eliminat correctament");
                        jsonArray.remove(i);
                        guardarJSON(jsonArray, filepath);
                        return;
                        } else if (validacio.equals("no")) {
                            menus.clearScreen();
                            
                            menus.menUsuaris(scanner);                        
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


    public static void LlistarUsuaris(){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject usuari = jsonArray.getJSONObject(i);

                int id = usuari.getInt("id");
                String nom = usuari.getString("nom");
                String cognom = usuari.getString("cognom");
                int telefon = usuari.getInt("telefon");

                System.out.println("ID: " + id);
                System.out.println("Nom: " + nom);
                System.out.println("Cognom: " + cognom);
                System.out.println("telefon: " + telefon);
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

           
            
        }

    public static void LlistarUsuarisActiu() {
    try {
        // Leer archivos JSON
        String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray prestecsArray = new JSONArray(contentPrestecs);

        String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray usuarisArray = new JSONArray(contentUsuaris);

        // Indexar usuarios por ID para búsquedas rápidas
        Map<Integer, JSONObject> usuarisMap = new HashMap<>();
        for (int j = 0; j < usuarisArray.length(); j++) {
            JSONObject usuari = usuarisArray.getJSONObject(j);
            usuarisMap.put(usuari.getInt("id"), usuari);
        }

        LocalDate today = LocalDate.now(); // Fecha actual

        System.out.println("Usuaris amb préstec actiu:");

        for (int i = 0; i < prestecsArray.length(); i++) {
            JSONObject prestec = prestecsArray.getJSONObject(i);

            // Validar y parsear fechas del préstamo
            if (prestec.has("dataPrestec") && prestec.has("dataDevolucio")) {
                LocalDate dataPrestec = LocalDate.parse(prestec.getString("dataPrestec"));
                LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));

                // Verificar si el préstamo está activo
                if (!today.isBefore(dataPrestec) && !today.isAfter(dataDevolucio)) {
                    int idUsuari = prestec.getInt("idUsuari");

                    // Buscar el usuario en el mapa
                    if (usuarisMap.containsKey(idUsuari)) {
                        JSONObject usuari = usuarisMap.get(idUsuari);

                        // Imprimir información del usuario
                        System.out.println("ID: " + usuari.getInt("id"));
                        System.out.println("Nom: " + usuari.getString("nom"));
                        System.out.println("Cognom: " + usuari.getString("cognom"));
                        System.out.println("Telefon: " + usuari.getInt("telefon"));
                        System.out.println("");
                    }
                }
            } else {
                System.err.println("Préstec con formato incorrecto en el índice: " + i);
            }
        }
    } catch (Exception e) {
        System.err.println("Error al listar usuaris amb préstec actiu: " + e.getMessage());
        e.printStackTrace();
    }
}

public static void LlistarUsuarisForaTermini() {
    try {
        // Leer archivos JSON
        String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray prestecsArray = new JSONArray(contentPrestecs);

        String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray usuarisArray = new JSONArray(contentUsuaris);

        // Indexar usuarios por ID para búsquedas rápidas
        Map<Integer, JSONObject> usuarisMap = new HashMap<>();
        for (int j = 0; j < usuarisArray.length(); j++) {
            JSONObject usuari = usuarisArray.getJSONObject(j);
            usuarisMap.put(usuari.getInt("id"), usuari);
        }

        LocalDate today = LocalDate.now(); // Fecha actual

        System.out.println("Usuaris amb préstecs fora de termini:");

        for (int i = 0; i < prestecsArray.length(); i++) {
            JSONObject prestec = prestecsArray.getJSONObject(i);

            // Validar y parsear fechas del préstamo
            if (prestec.has("dataPrestec") && prestec.has("dataDevolucio")) {
                LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));

                // Verificar si el préstamo está fuera de plazo
                if (today.isAfter(dataDevolucio)) {
                    int idUsuari = prestec.getInt("idUsuari");

                    // Buscar el usuario en el mapa
                    if (usuarisMap.containsKey(idUsuari)) {
                        JSONObject usuari = usuarisMap.get(idUsuari);

                        // Imprimir información del usuario
                        System.out.println("ID: " + usuari.getInt("id"));
                        System.out.println("Nom: " + usuari.getString("nom"));
                        System.out.println("Cognom: " + usuari.getString("cognom"));
                        System.out.println("Telefon: " + usuari.getInt("telefon"));
                        System.out.println("");
                    }
                }
            } else {
                System.err.println("Préstec con formato incorrecto en el índice: " + i);
            }
        }
    } catch (Exception e) {
        System.err.println("Error al listar usuaris fora de termini: " + e.getMessage());
        e.printStackTrace();
    }
}





}