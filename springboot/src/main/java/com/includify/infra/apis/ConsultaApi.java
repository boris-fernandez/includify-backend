package com.includify.infra.apis;

import com.includify.infra.apis.dto.*;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsultaApi {

    private static URI URL_API = URI.create("https://includify-backend.onrender.com/");

    private Gson gson;

    private HttpClient client;

    public ConsultaApi(){
        this.gson = new Gson();
        this.client = HttpClient.newBuilder().build();
    }


    private <T extends JsonValidacion> T consumirApiPost(String jsonBody, Class<T> responseClass, String endpoint){
        URI requestUri = URL_API.resolve(endpoint);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson()
                    .fromJson(response.body(), responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " Python");
        }
    }

    private <T extends JsonValidacion> T consumirApiGet(Class<T> responseClass, String endpoint){
        URI requestUri = URL_API.resolve(endpoint);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al consumir la API. Código de estado: " + response.statusCode());
            }

            return new Gson()
                    .fromJson(response.body(), responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()+ " Python");
        }
    }


    public VideoDTO videos(String anuncio){
        String jsonBody = gson.toJson(anuncio);
        return consumirApiPost(jsonBody, VideoDTO.class, "generateVideo");
        
    }

    public CvDTO cv(EnviarCandidatoDTO enviarCandidato){
        String jsonBody = gson.toJson(enviarCandidato);
        return consumirApiPost(jsonBody, CvDTO.class, "generatePdf");
    }

    public MatchDTO match(int idUsuario){
        String uri = "match?pk_usuario=" + idUsuario;
        return consumirApiGet(MatchDTO.class, uri);
    }
}
