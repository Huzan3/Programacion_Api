package org.example.httpPokemon;

import org.example.httpPokemon.clases.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== MENÚ POKÉAPI =====");
            System.out.println("1. Obtener los Pokémon");
            System.out.println("2. Obtener métodos de encuentro");
            System.out.println("3. Obtener idiomas");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int option;
            try {
                option = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Ingrese un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    try {
                        HttpClientPokemon http = new HttpClientPokemon();
                        for (Pokemon p : http.getPokemons()) {
                            System.out.println("Nombre: " + p.name + " | URL: " + p.url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        HttpClientPokemon http = new HttpClientPokemon();
                        System.out.println("\n--- Métodos de Encuentro ---");
                        for (EncounterMethod em : http.getEncounterMethods()) {
                            System.out.println("\nID: " + em.id);
                            System.out.println("Nombre: " + em.name);
                            System.out.println("Orden: " + em.order);
                            System.out.println("Traducciones:");
                            for (EncounterName n : em.names) {
                                System.out.println("  [" + n.language.name + "] " + n.name);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    try {
                        HttpClientPokemon http = new HttpClientPokemon();
                        System.out.println("\n--- Idiomas disponibles ---");
                        System.out.println("Total: " + http.getLanguageCount());
                        for (Language lang : http.getLanguages()) {
                            System.out.println("Nombre: " + lang.name + " | URL: " + lang.url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }
}