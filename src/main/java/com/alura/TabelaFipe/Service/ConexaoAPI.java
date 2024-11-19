package com.alura.TabelaFipe.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConexaoAPI {
    public String obterDados(String enedereco) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(enedereco)).build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        String json = response.body();
        return json;
    }
}
