package org.example.dogApi.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DogsController {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // Cache de razas (se carga desde la API, nunca a mano)
    private List<String> allBreedsCache = null;
    private List<String> noSubBreedsCache = null;
    private List<String> withSubBreedsCache = null;

    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        try {

            // 1. GET /dogs/All -> Lista normalizada de TODAS las razas
            if (path.equals("/dogs/All")) {
                String json = buildBreedsJson(getAllBreeds(), "razas");
                sendResponse(exchange, 200, json);
                return;
            }

            // 2. GET /dogs/noSub -> Solo razas SIN subrazas
            if (path.equals("/dogs/sinSub")) {
                String json = buildBreedsJson(getNoSubBreeds(), "razas_sin_subrazas");
                sendResponse(exchange, 200, json);
                return;
            }

            // 3. GET /dogs/conSub -> Solo razas CON subrazas
            if (path.equals("/dogs/conSub")) {
                String json = buildBreedsJson(getWithSubBreeds(), "razas_con_subrazas");
                sendResponse(exchange, 200, json);
                return;
            }

            // 4. GET /dogs/random -> Una imagen random
            if (path.equals("/dogs/random")) {
                String response = callApi("https://dog.ceo/api/breeds/image/random");
                JsonObject apiJson = gson.fromJson(response, JsonObject.class);
                String imageUrl = apiJson.get("message").getAsString();

                JsonObject result = new JsonObject();
                result.addProperty("imagen", imageUrl);
                sendResponse(exchange, 200, gson.toJson(result));
                return;
            }

            // 5. GET /dogs/randomFive -> Cinco imagenes random
            if (path.equals("/dogs/randomFive")) {
                JsonArray imagenes = new JsonArray();

                for (int i = 0; i < 5; i++) {
                    String response = callApi("https://dog.ceo/api/breeds/image/random");
                    JsonObject apiJson = gson.fromJson(response, JsonObject.class);

                    imagenes.add(apiJson.get("message").getAsString());
                }

                JsonObject result = new JsonObject();
                result.add("imagenes", imagenes);
                sendResponse(exchange, 200, gson.toJson(result));
                return;
            }


            // 6. GET /dogs/random/{n} -> N imagenes random
            if (path.startsWith("/dogs/random/")) {
                String[] parts = path.split("/");
                if (parts.length == 4) {
                    try {
                        int n = Integer.parseInt(parts[3]);
                        JsonArray imagenes = new JsonArray();

                        for (int i = 0; i < n; i++) {
                            String response = callApi("https://dog.ceo/api/breeds/image/random");
                            JsonObject apiJson = gson.fromJson(response, JsonObject.class);
                            imagenes.add(apiJson.get("message").getAsString());
                        }

                        JsonObject result = new JsonObject();
                        result.addProperty("cantidad", n);
                        result.add("imagenes", imagenes);
                        sendResponse(exchange, 200, gson.toJson(result));
                        return;

                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400,
                                "{\"status\":\"error\",\"message\":\"Número de imágenes no válido\"}");
                        return;
                    }
                }
            }

            // 7. GET /dogs/breed/{breed} -> Imagen random de la raza {breed}
            if (path.startsWith("/dogs/breed/")) {
                String[] part = path.split("/");
                if (part.length == 4) {
                    try {
                        String breed = part[3].trim();
                        String response = callApi("https://dog.ceo/api/breed/" + breed + "/images");
                        JsonObject apiJson = gson.fromJson(response, JsonObject.class);

                        if (apiJson.get("status").getAsString().equals("error")) {
                            sendResponse(exchange, 404,
                                    "{\"status\":\"error\",\"message\":\"Raza no encontrada: " + breed + "\"}");
                            return;
                        }
                        System.out.println(apiJson);
                        JsonArray imageUrl = apiJson.get("message").getAsJsonArray();
                        JsonObject result = new JsonObject();
                        result.addProperty("raza", breed);
                        result.add("imagen", imageUrl);
                        sendResponse(exchange, 200, gson.toJson(result));
                        return;

                    } catch (Exception e) {
                        sendResponse(exchange, 500,
                                "{\"status\":\"error\",\"message\":\"Error llamando a la API dogs: " + e.getMessage() + "\"}");
                        return;
                    }
                }
            }

            //8. GET /dogs/breed/{breed1}/{breed2}/{n} -> n imagenes de dos razas distintas
            if (path.startsWith("/dogs/breed/")) {
                String[] part = path.split("/");
                if (part.length == 6) {
                    try {
                        String breed1 = part[3].trim();
                        String breed2 = part[4].trim();
                        int n = Integer.parseInt(part[5]);


                        String response1 = callApi("https://dog.ceo/api/breed/" + breed1 + "/images/random/" + n);
                        String response2 = callApi("https://dog.ceo/api/breed/" + breed2 + "/images/random/" + n);
                        JsonObject apiJson1 = gson.fromJson(response1, JsonObject.class);
                        JsonObject apiJson2 = gson.fromJson(response2, JsonObject.class);

                        if (apiJson1.get("status").getAsString().equals("error") || apiJson2.get("status").getAsString().equals("error")) {
                            sendResponse(exchange, 404,
                                    "{\"status\":\"error\",\"message\":\"Una o ambas razas no encontradas: " + breed1 + ", " + breed2 + "\"}");
                            return;
                        }

                        JsonArray images1 = apiJson1.get("message").getAsJsonArray();
                        JsonArray images2 = apiJson2.get("message").getAsJsonArray();
                        JsonObject result = new JsonObject();
                        result.addProperty("raza1", breed1);
                        result.add("imagenes_raza1", images1);
                        result.addProperty("raza2", breed2);
                        result.add("imagenes_raza2", images2);
                        sendResponse(exchange, 200, gson.toJson(result));
                        return;

                    } catch (Exception e) {
                        sendResponse(exchange, 500,
                                "{\"status\":\"error\",\"message\":\"Error llamando a la API dogs: " + e.getMessage() + "\"}");
                        return;
                    }
                }
            }


            // 9. GET /dogs/breed/{breed1}/{breed2}/{...}/{breedN}/{n} -> n imagenes de N razas
            if (path.startsWith("/dogs/breed/")) {
                String[] part = path.split("/");
                if (part.length >= 6) {
                    try {
                        int n = Integer.parseInt(part[part.length - 1]);
                        JsonObject result = new JsonObject();
                        JsonArray allResults = new JsonArray();

                        for (int i = 3; i < part.length - 1; i++) {
                            String breed = part[i].trim();
                            String response = callApi("https://dog.ceo/api/breed/" + breed + "/images/random/" + n);
                            JsonObject apiJson = gson.fromJson(response, JsonObject.class);

                            if (apiJson.get("status").getAsString().equals("error")) {
                                sendResponse(exchange, 404,
                                        "{\"status\":\"error\",\"message\":\"Raza no encontrada: " + breed + "\"}");
                                return;
                            }

                            JsonObject breedResult = new JsonObject();
                            breedResult.addProperty("raza", breed);
                            breedResult.add("imagenes", apiJson.get("message").getAsJsonArray());
                            allResults.add(breedResult);
                        }

                        result.add("razas", allResults);
                        result.addProperty("cantidad_imagenes_por_raza", n);
                        sendResponse(exchange, 200, gson.toJson(result));
                        return;

                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400,
                                "{\"status\":\"error\",\"message\":\"Último parámetro debe ser un número\"}");
                        return;
                    } catch (Exception e) {
                        sendResponse(exchange, 500,
                                "{\"status\":\"error\",\"message\":\"Error llamando a la API dogs: " + e.getMessage() + "\"}");
                        return;
                    }
                }
            }

            sendResponse(exchange, 404, "{\"status\":\"error\",\"message\":\"Endpoint dogs no válido\"}");

        } catch (Exception e) {
            sendResponse(exchange, 500,
                    "{\"status\":\"error\",\"message\":\"Error llamando a la API dogs: " + e.getMessage() + "\"}");
        }
    }


    private List<String> getAllBreeds() throws Exception {
        if (allBreedsCache != null)
            return allBreedsCache;
        loadBreedsFromApi();
        return allBreedsCache;
    }

    private List<String> getNoSubBreeds() throws Exception {
        if (noSubBreedsCache != null)
            return noSubBreedsCache;
        loadBreedsFromApi();
        return noSubBreedsCache;
    }

    private List<String> getWithSubBreeds() throws Exception {
        if (withSubBreedsCache != null)
            return withSubBreedsCache;
        loadBreedsFromApi();
        return withSubBreedsCache;
    }

    /**
     * Carga las razas desde la API y las clasifica en las tres listas.
     * Todo sale del endpoint list/all, nunca se escriben razas a mano.
     */
    private void loadBreedsFromApi() throws Exception {
        String response = callApi("https://dog.ceo/api/breeds/list/all");
        JsonObject json = gson.fromJson(response, JsonObject.class);
        JsonObject message = json.getAsJsonObject("message");

        List<String> all = new ArrayList<>();
        List<String> noSub = new ArrayList<>();
        List<String> withSub = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : message.entrySet()) {
            String mainBreed = entry.getKey();
            JsonArray subBreeds = entry.getValue().getAsJsonArray();

            if (subBreeds.size() == 0) {
                all.add(mainBreed);
                noSub.add(mainBreed);
            } else {
                for (JsonElement sub : subBreeds) {
                    String fullName = mainBreed + "-" + sub.getAsString();
                    all.add(fullName);
                    withSub.add(fullName);
                }
            }
        }

        allBreedsCache = all;
        noSubBreedsCache = noSub;
        withSubBreedsCache = withSub;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private String buildBreedsJson(List<String> breeds, String key) {
        JsonArray breedsArray = new JsonArray();
        for (String breed : breeds) {
            JsonObject breedObj = new JsonObject();
            breedObj.addProperty("nombre", breed);
            breedsArray.add(breedObj);
        }

        JsonObject result = new JsonObject();
        result.add(key, breedsArray);
        return gson.toJson(result);
    }

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
