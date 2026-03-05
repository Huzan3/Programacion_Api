package org.example.httpPokemon.clases;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpClientPokemon {

    // URLs de PokeAPI
    private final String url = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=1350";
    private final String urlEncounter = "https://pokeapi.co/api/v2/encounter-method/";
    private final String urlLanguage = "https://pokeapi.co/api/v2/language/";

    // Cliente HTTP y Gson
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // Método genérico GET
    private String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    // Pokémon
    public List<Pokemon> getPokemons() throws IOException, InterruptedException {
        String json = get(url);
        PokemonResponse data = gson.fromJson(json, PokemonResponse.class);
        return data.results;
    }

    // Encounter Methods
    public List<EncounterMethod> getEncounterMethods() throws IOException, InterruptedException {
        String json = get(urlEncounter);
        EncounterMethodListResponse listData = gson.fromJson(json, EncounterMethodListResponse.class);

        List<EncounterMethod> detailedList = new ArrayList<>();
        for (EncounterMethodRef ref : listData.results) {
            String detailJson = get(ref.url);
            EncounterMethod detail = gson.fromJson(detailJson, EncounterMethod.class);
            detailedList.add(detail);
        }

        return detailedList;
    }

    // Languages
    public List<Language> getLanguages() throws IOException, InterruptedException {
        String json = get(urlLanguage);
        LanguageListResponse data = gson.fromJson(json, LanguageListResponse.class);
        return data.results;
    }

    public int getLanguageCount() throws IOException, InterruptedException {
        String json = get(urlLanguage);
        LanguageListResponse data = gson.fromJson(json, LanguageListResponse.class);
        return data.count;
    }
}