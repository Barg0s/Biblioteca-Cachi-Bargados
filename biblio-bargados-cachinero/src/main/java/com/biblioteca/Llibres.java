package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Llibres {

    // Definició de les rutes dels arxius JSON
    public static final String filepath = "JSON/llibres.json";
    public static final String filepathPrestecs = "JSON/prestecs.json";
    public static final String filepathUsers = "JSON/usuaris.json";

    /**
     * Mètode per guardar el contingut d'un JSONArray en un arxiu JSON.
     *
     * @param jsonArray El JSONArray a guardar
     * @param filepath  Ruta de l'arxiu JSON
     */
    public static void guardarJSON(JSONArray jsonArray, String filepath) {
        try {
            Files.write(Paths.get(filepath), jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mètode per mostrar la informació d'un llibre.
     *
     * @param llibre JSONObject que representa el llibre
     */
    public static void mostrarInformacio(JSONObject llibre) {
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

    /**
     * Mètode per buscar un llibre per la seva ID.
     *
     * @param jsonArray El JSONArray que conté els llibres
     * @param id        La ID del llibre a buscar
     * @return El llibre com a JSONObject si es troba, o null si no existeix
     */
    public static JSONObject buscarLlibrePerId(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject llibre = jsonArray.getJSONObject(i);
            if (llibre.getInt("id") == id) {
                return llibre;
            }
        }
        return null;  // Si no es troba el llibre, retorna null
    }

    /**
     * Mètode per comprovar si un llibre amb un títol donat ja existeix.
     *
     * @param NouTitol El títol del nou llibre a comprovar
     * @param llista    La llista de llibres
     * @throws IllegalArgumentException Si el títol ja existeix
     */
    public static void comprovarLlibre(String NouTitol, JSONArray llista) throws IllegalArgumentException {
        for (int i = 0; i < llista.length(); i++) {
            JSONObject llibre = llista.getJSONObject(i);
            String titol = llibre.getString("titol");

            // Si el títol ja existeix, llança una excepció
            if (NouTitol.equals(titol)) {
                throw new IllegalArgumentException("El titol ja existeix.");
            }
        }
    }

    /**
     * Mètode per afegir un llibre a la llista de llibres.
     *
     * @param titol  El títol del llibre
     * @param autors Els autors del llibre
     */
    public static void afegirLlibres(String titol, String[] autors) {
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
            comprovarLlibre(titol, jsonArray); 
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
            for (String autor : autors) {
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

    /**
     * Mètode per modificar el títol d'un llibre.
     *
     * @param llibre    El llibre a modificar
     * @param llibres   La llista de llibres
     * @param nouTitol El nou títol a establir
     */
    private static void modificarTitol(JSONObject llibre, JSONArray llibres, String nouTitol) {
        if (nouTitol.equals(llibre.getString("titol"))) {
            System.out.println("El títol no pot ser el mateix.");
            return;
        }
        llibre.put("titol", nouTitol); 
        guardarJSON(llibres, filepath);  
        System.out.println("Títol modificat correctament.");
    }

    /**
     * Mètode per modificar els autors d'un llibre.
     *
     * @param llibre   El llibre a modificar
     * @param llibres  La llista de llibres
     * @param nouValor El nou valor dels autors a afegir
     */
    private static void modificarAutors(JSONObject llibre, JSONArray llibres, String nouValor, String opcio) {
        JSONArray autors = llibre.getJSONArray("autors");
        String[] nousAutors = nouValor.split(",");
        JSONArray autorsActualitzats = new JSONArray();
    
        boolean autorAfegit = false;
        
        switch (opcio) {
            case "afegir":
            case "1":
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
                break;
                
            case "actualitzar":
            case "2":
                for (String nouAutor : nousAutors) {
                    nouAutor = nouAutor.trim();
                    if (!nouAutor.isEmpty()) {
                        autorsActualitzats.put(nouAutor); 
                }
    
                if (autorsActualitzats.length() > 0) {
                    llibre.put("autors", autorsActualitzats);  
                    guardarJSON(llibres, filepath);  
                    System.out.println("Autors modificats correctament.");
                } else {
                    System.out.println("No s'ha afegit cap autor vàlid per actualitzar.");
                }
                break;}
                
            default:
                System.out.println("Opció no vàlida.");
                return;
        }
    

        if (autorAfegit) {
            guardarJSON(llibres, filepath);
            System.out.println("Autors afegits correctament.");
        } else {
            System.out.println("L'autor ja existeix.");
        }
    }
    
    /**
     * Mètode per modificar un llibre en funció d'una clau i un nou valor.
     *
     * @param id       La ID del llibre a modificar
     * @param clau     La clau a modificar (pot ser "titol" o "autors")
     * @param nouValor El nou valor per la clau especificada
     */
    public static void modificarLlibres(int id, String clau, String nouValor) {
        Scanner scanner = new Scanner(System.in);
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

                    System.out.println("Que vols fer?(actualitzar/afegir)");
                    String opcioAutors = scanner.nextLine();
                    modificarAutors(llibre, jsonArray, nouValor,opcioAutors);    
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

    /**
     * Mètode per eliminar un llibre per la seva ID.
     *
     * @param id La ID del llibre a eliminar
     */
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

    /**
     * Mètode per llistar tots els llibres.
     */
    public static void LlistarLlibres() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);

            // Mostrem la informació de tots els llibres
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject llibre = jsonArray.getJSONObject(i);
                mostrarInformacio(llibre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode per llistar llibres per títol.
     *
     * @param titolFiltar El títol (o part d'ell) per filtrar
     */
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
                    mostrarInformacio(llibre);
                }
            }

            if (!titolTrobat) {
                System.out.println("El títol no existeix.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode per llistar llibres per autor.
     *
     * @param autorFiltrat L'autor per filtrar
     */
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

    /**
     * Mètode per llistar llibres que estan en préstec.
     */
    public static void LlistarLlibresPrestec() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray llibresArray = new JSONArray(content);

            String prestecsContent = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(prestecsContent);

            String usuarisContent = new String(Files.readAllBytes(Paths.get(filepathUsers)));
            JSONArray usuarisArray = new JSONArray(usuarisContent);

            for (int p = 0; p < prestecsArray.length(); p++) {
                JSONObject prestat = prestecsArray.getJSONObject(p);
                int id_llibre = prestat.getInt("idLlibre");
                String idUsuari = prestat.getString("idUsuari");

                for (int i = 0; i < llibresArray.length(); i++) {
                    JSONObject llibre = llibresArray.getJSONObject(i);
                    if (id_llibre == llibre.getInt("id")) {
                        for (int j = 0; j < usuarisArray.length(); j++) {
                            JSONObject usuari = usuarisArray.getJSONObject(j);
                            if (idUsuari.equals(usuari.getString("id"))) {
                                System.out.println("Llibre: " + llibre.getString("titol"));
                                System.out.println("Usuari: " + usuari.getString("nom"));
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
