package com.includify.infra.apis;

import com.includify.infra.apis.dto.*;
import com.nimbusds.jose.shaded.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaApi {

    private static URI URL_API = URI.create("http://localhost:5050/");

    private Gson gson;

    private HttpClient client;

    public ConsultaApi(){
        this.gson = new Gson();
        this.client = HttpClient.newHttpClient();
    }

    private <T> T manejarRespuesta(HttpRequest request, Class<T> responseClass) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al consumir la API. Código de estado: " + response.statusCode());
            }

            return new Gson().fromJson(response.body(), responseClass);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API: " + e.getMessage(), e);
        }
    }

    public <T extends JsonValidacion> T consumirApiPost(String jsonBody, Class<T> responseClass, String endpoint){
        URI requestUri = URL_API.resolve(endpoint);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return manejarRespuesta(request, responseClass);
    }

    public <T extends JsonValidacion> T consumirApiGet(Class<T> responseClass, String endpoint){
        URI requestUri = URL_API.resolve(endpoint);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .build();

        return manejarRespuesta(request, responseClass);
    }

    public VideoDTO videos(String textoOriginal){
        String jsonBody = gson.toJson(textoOriginal);
        return consumirApiPost(jsonBody, VideoDTO.class, "generateVideo");
        
    }

    public PdfDTO pdf(EnviarGenerarPdfDTO enviarGenerarPdfDTO){
        String jsonBody = enviarGenerarPdfDTO.generarJson();
        return consumirApiPost(jsonBody, PdfDTO.class, "generatePdf");
    }

    public MatchDTO match(){
        return consumirApiGet(MatchDTO.class, "match");
    }
}
