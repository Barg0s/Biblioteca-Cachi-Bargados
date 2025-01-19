package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Llibres {

    public static final String filepath = "JSON/llibres.json";
    public static final String filepathPrestecs = "JSON/prestecs.json";
    public static final String filepathUsers = "JSON/usuaris.json";


public static void guardarJSON(JSONArray jsonArray,String filepath) {
    try {
        Files.write(Paths.get(filepath), jsonArray.toString(4).getBytes());
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}
    public static void mostrarInformacio(JSONObject llibre){
        int id = llibre.getInt("id");
        String titol = llibre.getString("titol");

        JSONArray autorsArray = llibre.getJSONArray("autors");
        ArrayList<String> autors = new ArrayList<>();
        for (int j = 0; j < autorsArray.length(); j++) {
            autors.add(autorsArray.getString(j));
        }
        System.out.println("·······Informació del llibre··············");
        System.out.println("ID: " + id);
        System.out.println("Títol: " + titol);
        System.out.println("Autors: " + String.join(", ", autors));
        System.out.println("··········································");
        System.out.println("");
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
        if (titol == null || titol.trim().isEmpty()) {
            System.out.println("Error: El títol no pot estar buit.");
            return;
        }
        if (autors == null || autors.length == 0) {
            System.out.println("Error: Cal indicar almenys un autor.");
            return;
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            comprobarLlibre(titol, jsonArray);
            
            JSONObject Llibre = new JSONObject();
            int id = 1;
            if (jsonArray.length() > 0) {
                JSONObject ultimLLibre = jsonArray.getJSONObject(jsonArray.length() - 1);
                int ultimaId = ultimLLibre.getInt("id");
                id = ultimaId + 1; 
            }
    
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








    private static void modificarTitol(JSONObject llibre, JSONArray llibres, String nouTitol) {
        if (nouTitol.equals(llibre.getString("titol"))) {
            System.out.println("El títol no pot ser el mateix.");
            return;
        }
    
        llibre.put("titol", nouTitol);
        guardarJSON(llibres, filepath);
        System.out.println("Títol modificat correctament.");
    }
    

    private static void modificarAutors(JSONObject llibre, JSONArray llibres, String nouValor) {
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
            guardarJSON(llibres, filepath);
            System.out.println("Autors afegits correctament.");}else
            {
                System.out.println("L'autor ja existeix");
            }
    
    }


    
    public static void modificarLlibres(int id, String clau, String nouValor) {
        if (nouValor == null || nouValor.trim().isEmpty()) {
            System.out.println("El nou valor no pot estar buit.");
            return;
        }    
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            JSONObject llibre = buscarLlibrePerId(jsonArray, id);
            if (llibre == null) {
                System.out.println("Aquesta ID no existeix.");
                return;
            }
            switch (clau) {
                case "autors":
                    modificarAutors(llibre, jsonArray, nouValor);    
                    break;
                case "titol":
                    modificarTitol(llibre, jsonArray, nouValor);
                    break;
                case "id":
                    System.out.println("no es pot modificar l'id");
                    break;
                default:
                    System.out.println("Aquesta clau no existeix");
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    public static void eliminarLlibres(int id) {
        Scanner scanner = new Scanner(System.in);
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            
            boolean existeix = false;
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
                int id_llibre = llibre.getInt("id");
    
                if (id == id_llibre) {
                    existeix = true;
                    String titol = llibre.getString("titol");
                    System.out.println("Vols eliminar el Llibre " + titol + "?");
                    String confirmacio = scanner.nextLine();  

                    if (confirmacio.equalsIgnoreCase("si")) {
                        System.out.println("Llibre eliminat correctament.");
                        jsonArray.remove(i);
                        guardarJSON(jsonArray, filepath);
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
    
    public static void LlistarLlibres() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
    
                mostrarInformacio(llibre);
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
    
                String titol = llibre.getString("titol");
                if (titol.contains(titolFiltar)) {
                    titolTrobat = true;
    
                    mostrarInformacio(llibre);}
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
                        mostrarInformacio(llibre);    
                    
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
    public static void LlistarLlibresPrestec() {
    try {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray llibresArray = new JSONArray(content);

        String prestecsContent = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
        JSONArray prestecsArray = new JSONArray(prestecsContent);

        String usuarisContent = new String(Files.readAllBytes(Paths.get(filepathUsers)));
        JSONArray usuarisArray = new JSONArray(usuarisContent);

        for (int p = 0; p < prestecsArray.length(); p++) {
            JSONObject prestec = prestecsArray.getJSONObject(p);
            int idLlibrePrestec = prestec.getInt("idLlibre");
            int idUsuari = prestec.getInt("idUsuari");

            JSONObject llibre = null;
            for (int l = 0; l < llibresArray.length(); l++) {
                JSONObject tempLlibre = llibresArray.getJSONObject(l);
                if (tempLlibre.getInt("id") == idLlibrePrestec) {
                    llibre = tempLlibre;
                    break;
                }
            }

            JSONObject usuari = null;
            for (int u = 0; u < usuarisArray.length(); u++) {
                JSONObject tempUsuari = usuarisArray.getJSONObject(u);
                if (tempUsuari.getInt("id") == idUsuari) {
                    usuari = tempUsuari;
                    break;
                }
            }

            if (llibre != null && usuari != null) {
                String titol = llibre.getString("titol");
                JSONArray autorsArray = llibre.getJSONArray("autors");
                ArrayList<String> autors = new ArrayList<>();
                for (int a = 0; a < autorsArray.length(); a++) {
                    autors.add(autorsArray.getString(a));
                }

                String nom = usuari.getString("nom");
                String cognom = usuari.getString("cognom");

                String dataPrestec = prestec.getString("dataPrestec");
                String dataDevolucio = prestec.getString("dataDevolucio");

                System.out.println("·······Informació del llibre prestat·······");
                System.out.println("Propietari: " + nom + " " + cognom);
                System.out.println("Títol: " + titol);
                System.out.println("Autors: " + String.join(", ", autors));
                System.out.println("Data de préstec: " + dataPrestec);
                System.out.println("Data de devolució: " + dataDevolucio);
                System.out.println("···································");
            }
        }
    } catch (IOException e) {
        System.out.println("Error al llegir els arxius: " + e.getMessage());
    } catch (JSONException e) {
        System.out.println("Error al processar el JSON: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("S'ha produït un error inesperat: " + e.getMessage());
    }
}

}    