package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    protected final RestTemplate rest;

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.rest = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> hit(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object[]> stats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", URLEncoder.encode(String.valueOf(start), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(String.valueOf(end), StandardCharsets.UTF_8),
                "unique", unique
        ));
        if (uris != null) {
            parameters.put("uris", uris);
            return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
    }

    protected ResponseEntity<Object> post(String path, EndpointHitDto endpointHitDto) {
        return makeAndSendRequest(HttpMethod.POST, path, null, endpointHitDto);
    }

    protected ResponseEntity<Object[]> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequestForStats(HttpMethod.GET, path, parameters, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                      String path,
                                                      @Nullable Map<String, Object> parameters,
                                                      @Nullable EndpointHitDto body) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private ResponseEntity<Object[]> makeAndSendRequestForStats(HttpMethod method,
                                                                String path,
                                                                @Nullable Map<String, Object> parameters,
                                                                @Nullable EndpointHitDto body) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object[]> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object[].class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object[].class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new byte[][]{e.getResponseBodyAsByteArray()});
        }
        return prepareGatewayResponseForStats(serverResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private static ResponseEntity<Object[]> prepareGatewayResponseForStats(ResponseEntity<Object[]> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}