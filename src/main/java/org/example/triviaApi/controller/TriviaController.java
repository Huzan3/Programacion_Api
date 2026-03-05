package org.example.triviaApi.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TriviaController {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private List<String> allQuestions = null;


    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        try{
        // 1. Mostrar las preguntas de trivia faciles
        if (path.equals("/trivia/questions/easy")) {
            try {
                String response = callApi("https://opentdb.com/api.php?amount=10&difficulty=easy");
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"Error al obtener preguntas faciles\"}");
            }
        }

        // 2. Mostrar las preguntas de trivia medias
        if (path.equals("/trivia/questions/medium")) {
            try {
                String response = callApi("https://opentdb.com/api.php?amount=10&difficulty=medium");
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"Error al obtener preguntas medias\"}");
            }
        }

        // 3. Mostrar las preguntas de trivia dificiles
        if (path.equals("/trivia/questions/hard")) {
            try {
                String response = callApi("https://opentdb.com/api.php?amount=10&difficulty=hard");
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"Error al obtener preguntas dificiles\"}");
            }
        }

        sendResponse(exchange, 404, "{\"error\": \"Endpoint no encontrado\"}");

        }catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\": \"Error interno del servidor\"}");
        }


    }


    // ========== MÉTODOS AUXILIARES ==========


    private String callApi(String apiUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}