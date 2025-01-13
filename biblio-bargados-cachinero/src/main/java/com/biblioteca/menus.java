package com.biblioteca;

import java.util.ArrayList;
import java.util.Scanner;

public class menus {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void menuPrincipal(Scanner scanner) {
        while (true) {
            System.out.println("Menú principal");
            System.out.println("1. Gestió de llibres");
            System.out.println("2. Gestió d'usuaris");
            System.out.println("3. Gestió de préstecs");
            System.out.println("0. Sortir");
            System.out.print("Escull una opció: ");

            String option = scanner.nextLine().toLowerCase();
            clearScreen();

            switch (option) {
                case "1":
                case "llibres":
                    menuLlibres(scanner);
                    break;
                case "2":
                case "usuaris":
                    menUsuaris(scanner);
                    break;
                case "3":
                case "préstecs":
                    menuPrestecs(scanner);
                    break;
                case "0":
                case "sortir":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    public static void menuLlibres(Scanner scanner) {
        while (true) {
            System.out.println("Gestió de llibres");
            System.out.println("1. Afegir");
            System.out.println("2. Modificar");
            System.out.println("3. Eliminar");
            System.out.println("4. Llistar");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Escull una opció: ");

            String option = scanner.nextLine().toLowerCase();
            clearScreen();

            switch (option) {
                case "1":
                case "afegir":
                    System.out.print("Escriu el titol: ");
                    String titol = scanner.nextLine();
                    System.out.print("Escriu els autors separats per comes: ");
                    String autor = scanner.nextLine();
                    String[] autors = autor.split(",");
                    Llibres.afegirLlibres(titol, autors);
                    break;
                case "2":
                System.out.print("Escriu l'ID del llibre a modificar: ");
                if (scanner.hasNextInt()) {
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Llibres.modificarLlibres(id, scanner);
                } else {
                    System.out.println("Entrada no vàlida. L'ID ha de ser numèric.");
                    scanner.nextLine();
                }
                break;
                case "3":
                case "eliminar":
                    System.out.println("Funció per eliminar llibres encara no implementada.");
                    break;
                case "4":
                case "llistar":
                    menuLlistarllibres(scanner);
                    break;
                case "0":
                case "tornar":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    public static void menUsuaris(Scanner scanner) {
        while (true) {
            System.out.println("Gestió d'usuaris");
            System.out.println("1. Afegir");
            System.out.println("2. Modificar");
            System.out.println("3. Eliminar");
            System.out.println("4. Llistar");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Escull una opció: ");

            String option = scanner.nextLine().toLowerCase();
            clearScreen();

            switch (option) {
                case "1":
                case "afegir":
                    System.out.print("Escriu el nom del usuari: ");
                    String nom = scanner.nextLine();

                    System.out.print("Escriu el cognom del usuari: ");
                    String cognom = scanner.nextLine();

                    System.out.print("Escriu el numero de telefon: ");
                    if (scanner.hasNextInt()) {
                        int telefon = scanner.nextInt();
                        scanner.nextLine();
                        usuaris.afegirUsuaris(nom, cognom, telefon);
                    } else {
                        System.out.println("Entrada no vàlida. El número de telèfon ha de ser numèric.");
                        scanner.nextLine(); 
                    }
                    break;
                case "2":
                case "modificar":
                    System.out.print("Escriu l'ID del usuari a modificar: ");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        usuaris.modificarUsuaris(id, scanner);
                    } else {
                        System.out.println("Entrada no vàlida. L'ID ha de ser numèric.");
                        scanner.nextLine();
                    }
                    break;
                case "3":
                case "eliminar":
                    System.out.print("Escriu l'ID del usuari a eliminar: ");
                    if (scanner.hasNextInt()) {
                        int idEliminar = scanner.nextInt();
                        scanner.nextLine();
                        usuaris.eliminarUsuaris(idEliminar, scanner);
                    } else {
                        System.out.println("Entrada no vàlida. L'ID ha de ser numèric.");
                        scanner.nextLine();
                    }
                    break;
                case "4":
                case "llistar":
                    System.out.println("Funció per llistar usuaris encara no implementada.");
                    break;
                case "0":
                case "tornar":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    public static void menuPrestecs(Scanner scanner) {
        while (true) {
            System.out.println("Gestió de préstecs");
            System.out.println("1. Afegir");
            System.out.println("2. Modificar");
            System.out.println("3. Eliminar");
            System.out.println("4. Llistar");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Escull una opció: ");

            String option = scanner.nextLine().toLowerCase();
            clearScreen();

            switch (option) {
                case "1":
                case "afegir":
                    System.out.println("Funció per afegir préstecs encara no implementada.");
                    break;
                case "2":
                case "modificar":
                    System.out.println("Funció per modificar préstecs encara no implementada.");
                    break;
                case "3":
                case "eliminar":
                    System.out.println("Funció per eliminar préstecs encara no implementada.");
                    break;
                case "4":
                case "llistar":
                    
                    break;
                case "0":
                case "tornar":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    public static void menuLlistarllibres(Scanner scanner) {
        while (true) {
            System.out.println("Llistar llibres");
            System.out.println("1. Tots");
            System.out.println("2. En préstec");
            System.out.println("3. Per autor");
            System.out.println("4. Cercar títol");
            System.out.println("0. Tornar al menú de llibres");
            System.out.print("Escull una opció: ");

            String option = scanner.nextLine().toLowerCase();
            clearScreen();

            switch (option) {
                case "1":
                case "tots":
                    Llibres.LlistarLlibres();
                    break;
                case "2":
                case "en préstec":
                    System.out.println("Funció per llistar llibres en préstec encara no implementada.");
                    break;
                case "3":
                case "per autor":
                    System.out.println("Funció per llistar llibres per autor encara no implementada.");
                    break;
                case "4":
                case "cercar títol":
                    System.out.println("Funció per cercar títol encara no implementada.");
                    break;
                case "0":
                case "tornar":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }
}
