package com.biblioteca;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

public class usuaris {

    public static final String filepath = "JSON/usuaris.json"; 
    public static final String filepathPrestecs = "JSON/prestecs.json"; 

    /**
     * Mostra la informació d'un usuari.
     * 
     * @param usuari JSONobject que representa un usuari.
     */
    public static void mostrarInformacio(JSONObject usuari){
        System.out.println("··········Informació de l'usuari··········");
        System.out.println("ID: " + usuari.getInt("id"));
        System.out.println("Nom: " + usuari.getString("nom"));
        System.out.println("Cognom: " + usuari.getString("cognom"));
        System.out.println("Telefon: " + usuari.getInt("telefon"));
        System.out.println("··········································");  
        System.out.println();
    }

    /**
     * Desa un JSONArray d'usuaris al fitxer.
     * 
     * @param jsonArray El JSONArray amb les dades dels usuaris.
     * @param filepath La ruta del fitxer on es guarden les dades.
     */
    public static void guardarJSON(JSONArray jsonArray, String filepath) {
        try (PrintWriter out = new PrintWriter(filepath)) {
            out.write(jsonArray.toString(4));
            out.flush();
        } catch (Exception e) {
            System.out.println("No s'ha pogut desar el fitxer: " + e.getMessage());
        }
    }

    /**
     * Cerca un usuari per la seva ID dins d'un JSONArray d'usuaris.
     * 
     * @param jsonArray El JSONArray amb els usuaris.
     * @param id L'ID de l'usuari que es vol cercar.
     * @return L'objecte JSON de l'usuari si es troba, sinó, retorna null.
     */
    public static JSONObject buscarUsuariPerId(JSONArray jsonArray, int id) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getInt("id") == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * Comprova si un número de telèfon ja existeix a la llista d'usuaris,
     * i si és vàlid (té 9 dígits).
     * 
     * @param num El número de telèfon a verificar.
     * @param llista El JSONArray amb els usuaris existents.
     * @throws IllegalArgumentException Si el telèfon ja existeix o no és vàlid.
     */
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

    /**
     * Afegix un nou usuari a la llista d'usuaris al fitxer JSON.
     * 
     * @param nom El nom del nou usuari.
     * @param cognom El cognom del nou usuari.
     * @param telefon El número de telèfon del nou usuari.
     */
    public static void afegirUsuaris(String nom, String cognom, int telefon) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            comprobarTelefon(telefon, jsonArray);
    
            int id = 1;
            if (jsonArray.length() > 0) {
                JSONObject ultimoUsuari = jsonArray.getJSONObject(jsonArray.length() - 1);
                int ultimaId = ultimoUsuari.getInt("id");
                id = ultimaId + 1; 
            }
    
            JSONObject usuari = new JSONObject();
            usuari.put("id", id);
            usuari.put("nom", nom);
            usuari.put("cognom", cognom);
            usuari.put("telefon", telefon);
            usuari.put("prestecsActius", 0); 
    
            jsonArray.put(usuari);
            guardarJSON(jsonArray, filepath);
    
            System.out.println("Usuari afegit correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
    /**
     * Modifica un atribut específic d'un usuari identificat per la seva ID.
     * 
     * @param id L'ID de l'usuari a modificar.
     * @param clau La clau de l'atribut que es vol modificar.
     * @param nouValor El nou valor per a la clau especificada.
     */
    public static void modificarUsuaris(int id, String clau, String nouValor) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
    
            JSONObject user = buscarUsuariPerId(jsonArray, id);
            if (user == null) {
                System.out.println("Aquesta ID no existeix.");
                return;
            }
            switch (clau) {
                case "telefon":
                        try {
                            int nouTelefon = Integer.parseInt(nouValor); 
                            comprobarTelefon(nouTelefon, jsonArray); 
                            user.put(clau, nouTelefon);
                        } catch (NumberFormatException e) {
                            System.out.println("El valor del telèfon ha de ser un número.");
                            return;
                        } 
                    break;
                case "nom":
                case "cognom":
                    user.put(clau, nouValor);
                    break;
                case "id":
                    System.out.println("no es pot modificar l'id");
                    break;
                default:
                    System.out.println("Aquesta clau no existeix");
                    break;
            }

            guardarJSON(jsonArray, filepath);
            System.out.println("Usuari modificat correctament.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al accedir al fitxer: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un usuari identificat per la seva ID després de demanar confirmació.
     * 
     * @param id L'ID de l'usuari que es vol eliminar.
     */
    public static void eliminarUsuaris(int id) {
        Scanner scanner = new Scanner(System.in);
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
    
                    System.out.println("Vols eliminar a l'usuari " + nom + " " + cognom + "?");
                    String confirmacio = scanner.nextLine();  
                    if (confirmacio.equalsIgnoreCase("si")) {
                        System.out.println("Usuari eliminat correctament.");
                        jsonArray.remove(i); 
                        guardarJSON(jsonArray, filepath); 
                        return;
                    } else if (confirmacio.equalsIgnoreCase("no")) {
                        System.out.println("Acció cancel·lada.");
                        return;
                    } else {
                        System.out.println("Resposta no vàlida. Acció cancel·lada.");
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
     * Llista tots els usuaris emmagatzemats al fitxer JSON.
     */
    public static void LlistarUsuaris(){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject usuari = jsonArray.getJSONObject(i);
                mostrarInformacio(usuari);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Llista els usuaris que tenen préstecs actius.
     */
    public static void LlistarUsuarisActiu() {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
    
            String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray usuarisArray = new JSONArray(contentUsuaris);
    
            LocalDate today = LocalDate.now();
            boolean usuarisActiusTrobats = false;
    
            System.out.println("Usuaris amb préstec actiu:");
    
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
    
                if (prestec.has("dataPrestec") && prestec.has("dataDevolucio")) {
                    LocalDate dataPrestec = LocalDate.parse(prestec.getString("dataPrestec"));
                    LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
    
                    if (!today.isBefore(dataPrestec) && !today.isAfter(dataDevolucio)) {
                        int idUsuari = prestec.getInt("idUsuari");
    
                        for (int j = 0; j < usuarisArray.length(); j++) {
                            JSONObject usuari = usuarisArray.getJSONObject(j);
                            if (usuari.getInt("id") == idUsuari) {
                                mostrarInformacio(usuari);
                                usuarisActiusTrobats = true;
                                break;
                            }
                        }
                    }
                } else {
                    System.err.println("Préstec amb format incorrecte a l'índex: " + i);
                }
            }
    
            if (!usuarisActiusTrobats) {
                System.out.println("No n'hi ha usuaris amb préstecs actius.");
            }
        } catch (IOException e) {
            System.err.println("Error al llegir els fitxers: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error al processar el JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperat: " + e.getMessage());
        }
    }

    /**
     * Llista els usuaris que tenen préstecs fora de termini.
     */
    public static void LlistarUsuarisForaTermini() {
        try {
            String contentPrestecs = new String(Files.readAllBytes(Paths.get(filepathPrestecs)));
            JSONArray prestecsArray = new JSONArray(contentPrestecs);
    
            String contentUsuaris = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray usuarisArray = new JSONArray(contentUsuaris);
    
            LocalDate today = LocalDate.now();
            System.out.println("Usuaris amb préstecs fora de termini:");
    
            boolean hasForaTermini = false;
    
            for (int i = 0; i < prestecsArray.length(); i++) {
                JSONObject prestec = prestecsArray.getJSONObject(i);
    
                try {
                    LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
    
                    if (today.isAfter(dataDevolucio)) {
                        int idUsuari = prestec.getInt("idUsuari");
                        boolean usuariTrobat = false;
    
                        for (int j = 0; j < usuarisArray.length(); j++) {
                            JSONObject usuari = usuarisArray.getJSONObject(j);
    
                            if (usuari.getInt("id") == idUsuari) {
                                mostrarInformacio(usuari);
                                usuariTrobat = true;
                                hasForaTermini = true;
                                break;
                            }
                        }
    
                        if (!usuariTrobat) {
                            System.err.println("Usuari amb ID " + idUsuari + " no trobat.");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error en un préstec: " + e.getMessage());
                }
            }
    
            if (!hasForaTermini) {
                System.out.println("No n'hi ha usuaris amb préstecs fora de termini.");
            }
        } catch (IOException e) {
            System.err.println("Error al llegir els fitxers: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error en el format JSON: " + e.getMessage());
        }
    }
}
