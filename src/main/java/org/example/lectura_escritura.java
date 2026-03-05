package org.example;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class lectura_escritura {

    private static final String RUTA = "src/main/resources/productos.json";

    public static void main(String[] args) {

        try {
            Gson gson = new Gson();

            String[][] datosProductos = {
                    {"P-001", "Smartphone Galaxy S24", "Telefonia", "Samsung", "999.99", "15", "5", "15/03/2024", "Samsung Electronics", "8806092229876", "24 meses"},
                    {"P-002", "Laptop ThinkPad X1 Carbon", "Computadoras", "Lenovo", "1499.99", "3", "4", "10/03/2024", "Lenovo Group", "0193331681234", "36 meses"},
                    {"P-003", "Auriculares Noise Cancelling WH-1000XM5", "Audio", "Sony", "349.99", "25", "10", "18/03/2024", "Sony Corporation", "4905524931248", "12 meses"},
                    {"P-004", "Monitor Gaming 27\" 4K", "Monitores", "ASUS", "599.99", "0", "3", "05/03/2024", "ASUS Tek", "4712900751234", "24 meses"},
                    {"P-005", "Tablet iPad Air 5th Gen", "Tablets", "Apple", "749.99", "8", "6", "20/03/2024", "Apple Inc.", "1901993221234", "12 meses"}
            };

            FileWriter writer = new FileWriter(RUTA);
            gson.toJson(datosProductos, writer);
            writer.close();

            FileReader reader = new FileReader(RUTA);
            String[][] productosLeidos = gson.fromJson(reader, String[][].class);
            reader.close();

            for (int i = 0; i < productosLeidos.length; i++) {
                System.out.println("ID: " + productosLeidos[i][0]);
                System.out.println("Nombre: " + productosLeidos[i][1]);
                System.out.println("Categoría: " + productosLeidos[i][2]);
                System.out.println("Marca: " + productosLeidos[i][3]);
                System.out.println("Precio: $" + productosLeidos[i][4]);
                System.out.println("Stock: " + productosLeidos[i][5] + " unidades");
                System.out.println("Stock Mínimo: " + productosLeidos[i][6] + " unidades");
                System.out.println("Fecha de Última Compra: " + productosLeidos[i][7]);
                System.out.println("Proveedor: " + productosLeidos[i][8]);
                System.out.println("Código de Barras: " + productosLeidos[i][9]);
                System.out.println("Garantía: " + productosLeidos[i][10]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
