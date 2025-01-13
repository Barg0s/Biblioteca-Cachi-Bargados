package com.biblioteca;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.err.println("Gestió de bilioteca");
            System.out.println("1. Llibres");
            System.err.println("2. Usuaris");
            System.out.println("3. Préstecs");
            System.out.println("0. Sortir");
            System.out.print("Escull una opció: ");
            String option = scanner.nextLine().toLowerCase();
            switch (option) {
                case "llibres":
                case "1":
                    menus.clearScreen();
                    menus.menuLlibres(scanner);
                    break;
                case "usuaris":
                case "2":
                    menus.clearScreen();
                    menus.menUsuaris(scanner);
                    break;
                case "prestecs":
                case "3":
                    menus.clearScreen();
                    menus.menuPrestecs(scanner);
                    break;
                case "sortir":
                case "0":
                    System.out.println("Sortint del programa...");
                    scanner.close(); 
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna-ho a intentar.");
            }
        }
    }
}
