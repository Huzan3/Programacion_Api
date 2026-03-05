package org.example.triviaApi;

import com.sun.net.httpserver.HttpServer;
import org.example.triviaApi.router.RouterHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RouterHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor iniciado en http://localhost:8080");
        System.out.println("Endpoints disponibles:");
            System.out.println("  http://localhost:8080/trivia/questions/easy -> Obtener preguntas de trivia faciles");
            System.out.println("  http://localhost:8080/trivia/questions/medium -> Obtener preguntas de trivia medias");
            System.out.println("  http://localhost:8080/trivia/questions/hard -> Obtener preguntas de trivia dificiles");

    }
}
