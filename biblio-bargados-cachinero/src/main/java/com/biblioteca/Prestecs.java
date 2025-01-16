package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    
            // Comprovar si el llibre ja està prestat
            int idLlibre = llibre.getInt("id");
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
                int idPrestecLlibre = prestec.getInt("idLlibre");
                String dataDevolucio = prestec.getString("dataDevolucio");
    
                // Si el llibre ja està prestat i no s'ha retornat
                if (idLlibre == idPrestecLlibre && LocalDate.now().isBefore(LocalDate.parse(dataDevolucio))) {
                    System.out.println("El llibre ja està prestat.");
                    return;
                }
            }
    
            // Si no s'ha trobat cap préstec actiu per aquest llibre
            int idUsuari = usuari.getInt("id");
            int id = 1;
            if (prestecsArray.length() > 0) {
                JSONObject ultimPrestec = prestecsArray.getJSONObject(prestecsArray.length() - 1);
                int ultimaId = ultimPrestec.getInt("id");
                id = ultimaId + 1;
            }
    
            LocalDate dataPrestec = LocalDate.now();
            LocalDate dataDevolucio = dataPrestec.plusDays(7);
    
            JSONObject prestec = new JSONObject();
            prestec.put("id", id);
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
    

    public static void modificarPrestecs(int id, String clau, String nouValor) {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
    
            JSONObject prestecFiltrar = buscarPrestecPerID(prestecsArray, id);
            if (prestecFiltrar == null) {
                System.out.println("Préstec no trobat.");
                return;
            }
    
            if (!clau.equals("devolucio")) {
                throw new IllegalArgumentException("Només es pot modificar la data de devolució.");
            }
    
            // Validar la nova data
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
    
    public static void eliminarPrestec(int id) {
        Scanner scanner = new Scanner(System.in);

        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            
            boolean existeix = false;
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);
                int prestecId = prestec.getInt("id");
    
                if (id == prestecId) {
                    existeix = true; 
                    System.out.println("Vols eliminar el préstec?");
                    String confirmacio = scanner.nextLine();
                    if (confirmacio.equalsIgnoreCase("si")) {
                        System.out.println("Préstec eliminat correctament.");
                        jsonArray.remove(i);
                        guardarJSON(jsonArray, filepathPrestecs);
                        return;
                    } else if (confirmacio.equalsIgnoreCase("no")) {
                        System.out.println("Acció cancel·lada.");
                        return;
                    }
                }
            }
    
            if (!existeix) {
                System.out.println("Aquesta ID no existeix.");
            }
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
    public static void llistarPrestec(){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);

                String datadev = prestec.getString("dataDevolucio");
                String dataprest = prestec.getString("dataPrestec");
                int idllibre = prestec.getInt("idLlibre");
                int idprestec = prestec.getInt("id");
                int idUsuari = prestec.getInt("idUsuari");

                
                System.out.println("Data Devolucio: " + datadev);
                System.out.println("Data Prestec: " + dataprest);
                System.out.println("ID Prestec: " + idprestec);
                System.out.println("ID Llibre: " + idllibre);
                System.out.println("ID Usuari: " + idUsuari);
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void llistarPrestecUsuari(int id){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);
                String datadev = prestec.getString("dataDevolucio");
                String dataprest = prestec.getString("dataPrestec");
                int idllibre = prestec.getInt("idLlibre");
                int idprestec = prestec.getInt("id");
                int idUsuari = prestec.getInt("idUsuari");
                if (id == idUsuari){
                    System.out.println("Data Devolucio: " + datadev);
                    System.out.println("Data Prestec: " + dataprest);
                    System.out.println("ID Prestec: " + idprestec);
                    System.out.println("ID Llibre: " + idllibre);
                    System.out.println("ID Usuari: " + idUsuari);
                    System.out.println("");
                }

                
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void LlistarPrestecActius() {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
            LocalDate today = LocalDate.now();
    
            boolean prestecsActius = false;
    
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
    
                try {
                    if (prestec.has("dataPrestec") && prestec.has("dataDevolucio")) {
                        LocalDate dataPrestec = LocalDate.parse(prestec.getString("dataPrestec"));
                        LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
    
                        if ((today.isEqual(dataPrestec) || today.isAfter(dataPrestec)) && today.isBefore(dataDevolucio)) {
                            prestecsActius = true;
                            System.out.println("ID Préstec: " + prestec.getInt("id"));
                            System.out.println("ID Usuari: " + prestec.getInt("idUsuari"));
                            System.out.println("ID Llibre: " + prestec.getInt("idLlibre"));
                            System.out.println("Data Préstec: " + prestec.getString("dataPrestec"));
                            System.out.println("Data Devolució: " + prestec.getString("dataDevolucio"));
                            System.out.println("");
                        }
                    } else {
                        System.err.println("Préstec con formato incorrecto en el índice: " + i);
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando préstec en el índice " + i + ": " + e.getMessage());
                }
            }
    
            if (!prestecsActius) {
                System.out.println("No hi ha préstecs actius.");
            }
        } catch (Exception e) {
            System.err.println("Error al listar préstecs actius: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void LlistarPrestecForaTermini() {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
            LocalDate today = LocalDate.now();
    
            boolean prestecsFora = false;
    
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
    
                try {
                    if (prestec.has("dataPrestec") && prestec.has("dataDevolucio")) {
                        LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
    
                        if (today.isAfter(dataDevolucio)) {
                            prestecsFora = true;
                            System.out.println("ID Préstec: " + prestec.getInt("id"));
                            System.out.println("ID Usuari: " + prestec.getInt("idUsuari"));
                            System.out.println("ID Llibre: " + prestec.getInt("idLlibre"));
                            System.out.println("Data Préstec: " + prestec.getString("dataPrestec"));
                            System.out.println("Data Devolució: " + prestec.getString("dataDevolucio"));
                            System.out.println("");
                        }
                    } else {
                        System.err.println("Préstec con formato incorrecto en el índice: " + i);
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando préstec en el índice " + i + ": " + e.getMessage());
                }
            }
    
            if (!prestecsFora) {
                System.out.println("No hi ha préstecs fora de termini.");
            }
        } catch (Exception e) {
            System.err.println("Error al listar préstecs actius: " + e.getMessage());
            e.printStackTrace();
        }
    }


}    