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

    /**
     * Mostra la informació d'un préstec.
     * 
     * @param prestec El préstec que es vol mostrar.
     */
    public static void mostrarInformacio(JSONObject prestec) {
        System.out.println("·············Informacio del prestec·············");
        System.out.println("ID Préstec: " + prestec.getInt("id"));
        System.out.println("ID Usuari: " + prestec.getInt("idUsuari"));
        System.out.println("ID Llibre: " + prestec.getInt("idLlibre"));
        System.out.println("Data Préstec: " + prestec.getString("dataPrestec"));
        System.out.println("Data Devolució: " + prestec.getString("dataDevolucio"));
        System.out.println("················································");
        System.out.println();
    }

    /**
     * Comprova si el format de la data és correcte (YYYY-MM-DD).
     * 
     * @param data La data a comprovar.
     * @param llista Llista de préstecs o usuaris per comprovar.
     * @throws IllegalArgumentException Si el format de la data no és correcte.
     */
    public static void comprobarData(String data, JSONArray llista) throws IllegalArgumentException {
        for (int i = 0; i < llista.length(); i++) {
            JSONObject objecte = llista.getJSONObject(i);
            String dataPrestec = objecte.getString("dataPrestec");
            String[] dataSeparada = data.split("-");
            if (dataSeparada[0].length() < 4 || dataSeparada[1].length() < 2 || dataSeparada[2].length() < 2) {
                throw new IllegalArgumentException("el format de la data no es correcte (YYYY-MM-DD)");
            }else if (data.compareTo(dataPrestec) < 0 ){
                throw new IllegalArgumentException("La data de devolucio no pot ser menor a la del prestec");

            }
        }
    }

    /**
     * Cerca un llibre per títol en un array JSON.
     * 
     * @param jsonArray L'array JSON de llibres.
     * @param titol El títol del llibre a cercar.
     * @return El llibre en format JSONObject si es troba, null en cas contrari.
     */
    public static JSONObject buscarLlibrePerNom(JSONArray jsonArray, String titol) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject llibre = jsonArray.getJSONObject(i);
            if (llibre.getString("titol").equals(titol)) {
                return llibre;
            }
        }
        return null;
    }

    /**
     * Cerca un usuari per nom i cognom en un array JSON.
     * 
     * @param jsonArray L'array JSON d'usuaris.
     * @param nom El nom de l'usuari.
     * @param cognom El cognom de l'usuari.
     * @return L'usuari en format JSONObject si es troba, null en cas contrari.
     */
    public static JSONObject buscarUsuariPerNom(JSONArray jsonArray, String nom, String cognom) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject usuari = jsonArray.getJSONObject(i);
            if (usuari.getString("nom").equals(nom) && usuari.getString("cognom").equals(cognom)) {
                return usuari;
            }
        }
        return null;
    }

    /**
     * Cerca un préstec per ID en un array JSON.
     * 
     * @param jsonArray L'array JSON de préstecs.
     * @param id L'ID del préstec a cercar.
     * @return El préstec en format JSONObject si es troba, null en cas contrari.
     */
    public static JSONObject buscarPrestecPerID(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject prestec = jsonArray.getJSONObject(i);
            if (prestec.getInt("id") == id) {
                return prestec;
            }
        }
        return null;
    }

    /**
     * Guarda un array JSON a un fitxer.
     * 
     * @param jsonArray L'array JSON a guardar.
     * @param filepath La ruta del fitxer.
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
     * Afegir un préstec a la llista de préstecs.
     * 
     * @param titol El títol del llibre a prestar.
     * @param nom El nom de l'usuari que sol·licita el préstec.
     * @param cognom El cognom de l'usuari que sol·licita el préstec.
     */
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
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
                int idPrestecLlibre = prestec.getInt("idLlibre");

                if (idLlibre == idPrestecLlibre) {
                    System.out.println("El llibre ja està prestat.");
                    return;
                }
            }

            int idUsuari = usuari.getInt("id");
            int countPrestecs = 0;
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
                if (prestec.getInt("idUsuari") == idUsuari) {
                    countPrestecs++;
                }
            }

            if (countPrestecs >= 4) {
                System.out.println("L'usuari ja té 4 llibres prestats. No pot llogar més.");
                return;
            }

            usuari.put("prestecsActius", countPrestecs + 1);

            guardarJSON(usuarisArray, filepathUsers);

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

    /**
     * Modificar la data de devolució d'un préstec.
     * 
     * @param id L'ID del préstec a modificar.
     * @param clau La clau que es vol modificar (només devolucio és vàlida).
     * @param nouValor El nou valor per la data de devolució.
     */
    public static void modificarPrestecs(int id, String clau, String nouValor) {

        if (nouValor.isEmpty()){
            System.err.println("Error: El valor no pot estar buit");
        }
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

    /**
     * Eliminar un préstec per ID.
     * 
     * @param id L'ID del préstec a eliminar.
     */
    public static void eliminarPrestec(int id) {
        Scanner scanner = new Scanner(System.in);

        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);

            boolean existeix = false;

            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
                int prestecId = prestec.getInt("id");

                if (id == prestecId) {
                    existeix = true;
                    int idUsuari = prestec.getInt("idUsuari");

                    System.out.println("Vols eliminar el préstec?");
                    String confirmacio = scanner.nextLine();
                    if (confirmacio.equalsIgnoreCase("si")) {
                        System.out.println("Préstec eliminat correctament.");
                        prestecsArray.remove(i);
                        guardarJSON(prestecsArray, filepathPrestecs);

                        String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepathUsers)));
                        JSONArray usuarisArray = new JSONArray(contentUsuaris);

                        for (int j = 0; j < usuarisArray.length(); j++) {
                            JSONObject usuari = usuarisArray.getJSONObject(j);
                            int idUsuariEnArchivo = usuari.getInt("id");
                            if (idUsuari == idUsuariEnArchivo) {
                                int prestecsActius = usuari.getInt("prestecsActius");
                                usuari.put("prestecsActius", prestecsActius - 1);
                                guardarJSON(usuarisArray, filepathUsers);
                                break;
                            }
                        }

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
     * Llistar tots els préstecs.
     */
    public static void llistarPrestec() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);

                mostrarInformacio(prestec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Llistar tots els préstecs d'un usuari per ID.
     * 
     * @param id L'ID de l'usuari.
     */
    public static void llistarPrestecUsuari(int id) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject prestec = jsonArray.getJSONObject(i);

                int idUsuari = prestec.getInt("idUsuari");
                if (id == idUsuari) {
                    mostrarInformacio(prestec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Llistar els préstecs actius.
     */
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
                            mostrarInformacio(prestec);
                        }
                    } else {
                        System.err.println("Préstec amb format incorrecte: " + i);
                    }
                } catch (Exception e) {
                    System.err.println("Error" + i + ": " + e.getMessage());
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

    /**
     * Llistar els préstecs fora de termini.
     */
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
                            mostrarInformacio(prestec);
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
