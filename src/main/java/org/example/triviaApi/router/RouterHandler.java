package org.example.triviaApi.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dogApi.controllers.DogsController;
import org.example.triviaApi.controller.TriviaController;

import java.io.IOException;
import java.io.OutputStream;

public class RouterHandler implements HttpHandler {

    private final TriviaController triviaController = new TriviaController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        if (path.startsWith("/trivia")) {
            triviaController.handle(exchange);
            return;
        }

        String response = "404 - Ruta no encontrada";
        exchange.sendResponseHeaders(404, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
