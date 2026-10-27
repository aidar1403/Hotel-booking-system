package org.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HotelHttpTest {
    @LocalServerPort
    private int port;

    private HttpResponse<String> get(String path) throws Exception {
        return HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void receivesTheValueFromEachUserRequest() throws Exception {
        for (String name : new String[]{"Anna", "Boris", "Анна & Иван"}) {
            var response = get("/api/hello?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            assertTrue(response.headers().firstValue("Content-Type").orElse("").contains("application/json"));
            assertTrue(response.body().contains("\"name\":\"" + name + "\""), response.body());
        }
    }

    @Test
    void servesTheUserFormAndHandlesMissingName() throws Exception {
        var page = get("/");
        assertEquals(200, page.statusCode());
        assertTrue(page.body().contains("name=\"name\""));
        var response = get("/api/hello");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"name\":\"Guest\""));
    }
}
