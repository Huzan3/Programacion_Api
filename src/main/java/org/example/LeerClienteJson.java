package org.example;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeerClienteJson {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una opción:");
        System.out.println("1. Leer archivo JSON" + "\n 2. Crear un array y convertirlo en JSON");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                try {
                    Gson gson = new Gson();
                    JsonArray clientes = gson.fromJson(
                            new InputStreamReader(
                                    LeerClienteJson.class.getResourceAsStream("/cliente.json")),
                            JsonArray.class);

                    System.out.println(clientes);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:

//                    String[] [] []  datosClientes = {
//                            {{"1", "Juan", "Pérez"},
//                            {"2", "María", "Gómez"},
//                            {"3", "Luis", "Rodríguez"},
//                            {"4", "Ana", "López"}},
//
//                            {{"5", "Carlos", "Sánchez"},
//                            {"6", "Laura", "Martínez"},
//                            {"7", "Jorge", "Hernández"},
//                            {"8", "Sofía", "Ramírez"}}
//                    };

//                String[][][][] datosClientes = {
//                        {
//                            {
//                                {
//                                    {"1", "Juan", "Pérez"},
//                                    {"2", "María", "Gómez"},
//                                    {"3", "Luis", "Rodríguez"},
//                                    {"4", "Ana", "López"}
//                                },
//                                {
//                                    {"5", "Carlos", "Sánchez"},
//                                    {"6", "Laura", "Martínez"},
//                                    {"7", "Jorge", "Hernández"},
//                                    {"8", "Sofía", "Ramírez"}
//                                }
//                            }
//                        }
//                };
//                    datosClientes[1][2][1][3];
//
//                     datosClientes[0][1];
//
//                } catch (Exception e) {
//
//                }
//                break;
//
//            default:
                System.out.println("Opción no válida.");
        }
    }
}