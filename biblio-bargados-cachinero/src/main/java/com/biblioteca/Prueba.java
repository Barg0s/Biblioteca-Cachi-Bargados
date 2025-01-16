package com.biblioteca;


public class Prueba {

    public static void main(String[] args) {

    //LLIBRES
    Llibres.afegirLlibres("La sombra del viento", new String[]{"Carlos Ruiz Zafón"});
    Llibres.afegirLlibres("El señor de los anillos", new String[]{"J.R.R. Tolkien"});
    Llibres.afegirLlibres("El arte de la guerra", new String[]{"Sun Tzu", "James Clavell"});
    Llibres.LlistarLlibres();
    Llibres.eliminarLlibres(2);
    Llibres.LlistarLlibres();
    //USUARIS
    usuaris.afegirUsuaris("David", "Bargados", 123456789);
    usuaris.afegirUsuaris("Marc", "Cachinero", 123456788);
    usuaris.LlistarUsuaris();
    usuaris.eliminarUsuaris(2);
    usuaris.LlistarUsuaris();
    


    //PRESTECS
    Prestecs.afegirPrestec("El arte de la guerra", "David", "Bargados");
    Prestecs.llistarPrestec();
    Prestecs.LlistarPrestecActius();
    Prestecs.modificarPrestecs(1, "devolucio", "2024-01-15");
    Prestecs.llistarPrestec();
    Prestecs.LlistarPrestecForaTermini();
        
    }
}