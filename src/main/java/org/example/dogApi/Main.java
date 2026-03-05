package org.example.dogApi;

import com.sun.net.httpserver.HttpServer;
import org.example.dogApi.router.RouterHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RouterHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor iniciado en http://localhost:8080");
        System.out.println("Endpoints disponibles:");
        System.out.println("  http://localhost:8080/dogs/All             -> Todas las razas");
        System.out.println("  http://localhost:8080/dogs/sinSub          -> Razas SIN subrazas");
        System.out.println("  http://localhost:8080/dogs/conSub          -> Razas CON subrazas");
        System.out.println("  http://localhost:8080/dogs/random          -> 1 imagen random");
        System.out.println("  http://localhost:8080/dogs/randomFive       -> 5 imagenes random");
        System.out.println("  http://localhost:8080/dogs/random/{n}      -> N imagenes random");
        System.out.println("  http://localhost:8080/dogs/breed/{breed}   -> Imagen random de la raza {breed}");
        System.out.println("  http://localhost:8080/dogs/breed/{breed1}/{breed2}/{n}-> N imagenes de dos razas distintas");
        System.out.println("  http://localhost:8080/dogs/breed/{breed1}/{breed2}/{...}/{breedN}/{n} -> N imagenes de N razas distintas");

    }
}
