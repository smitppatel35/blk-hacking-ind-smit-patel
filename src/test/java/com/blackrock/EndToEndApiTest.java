package com.blackrock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Full End-to-End API Test Suite for BlackRock Challenge
 * Tests:
 *  - parse
 *  - validator
 *  - filter (Q,P,K)
 *  - returns (NPS + Index)
 *  - performance API
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndApiTest {

    @LocalServerPort
    private int port;

    private WebTestClient webClient = WebTestClient.bindToServer().build();  // Will now be injected successfully

    private String base(String endpoint) {
        return "http://localhost:" + port + "/blackrock/challenge/v1/" + endpoint;
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    // ---------------------------------------------------------
    // 1. Parse API
    // ---------------------------------------------------------
    @Test
    void testParse() {
        String req = """
        [
          {"date": "2023-10-12 20:15:00", "amount": 250}
        ]
        """;

        webClient.post()
                .uri(base("transactions:parse"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].ceiling").isEqualTo(300)
                .jsonPath("$[0].remanent").isEqualTo(50);
    }

    // ---------------------------------------------------------
    // 2. Validator
    // ---------------------------------------------------------
    @Test
    void testValidator() {
        String req = """
                {
                   "wage": 50000,
                   "transactions": [
                     {
                       "date": "2023-02-28 15:49:20",
                       "amount": 375.0,
                       "ceiling": 400.0,
                       "remanent": 25.0
                     },
                     {
                       "date": "2023-07-01 21:59:00",
                       "amount": 620.0,
                       "ceiling": 700.0,
                       "remanent": 80.0
                     },
                     {
                       "date": "2023-10-12 20:15:30",
                       "amount": 250.0,
                       "ceiling": 300.0,
                       "remanent": 50.0
                     },
                     {
                       "date": "2023-12-17 08:09:45",
                       "amount": 480.0,
                       "ceiling": 500.0,
                       "remanent": 20.0
                     }
                   ]
                 }
        """;

        webClient.post()
                .uri(base("transactions:validator"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.valid.length()").isEqualTo(4)
                .jsonPath("$.invalid.length()").isEqualTo(0);
    }

    // ---------------------------------------------------------
    // 3. Filter (Q, P, K)
    // ---------------------------------------------------------
    @Test
    void testFilter() {
        String req = """
                {
                   "wage": 50000,
                   "transactions": [
                     {
                       "date": "2023-02-28 15:49:20",
                       "amount": 375.0,
                       "ceiling": 400.0,
                       "remanent": 25.0
                     },
                     {
                       "date": "2023-07-01 21:59:00",
                       "amount": 620.0,
                       "ceiling": 700.0,
                       "remanent": 80.0
                     },
                     {
                       "date": "2023-10-12 20:15:30",
                       "amount": 250.0,
                       "ceiling": 300.0,
                       "remanent": 50.0
                     },
                     {
                       "date": "2023-12-17 08:09:45",
                       "amount": 480.0,
                       "ceiling": 500.0,
                       "remanent": 20.0
                     }
                   ],
                   "q": [
                     {
                       "fixed": 0,
                       "start": "2023-07-01 00:00:00",
                       "end": "2023-07-31 23:59:00"
                     }
                   ],
                   "p": [
                     {
                       "extra": 25,
                       "start": "2023-10-01 08:00:00",
                       "end": "2023-12-31 23:59:00"
                     }
                   ],
                   "k": [
                     {
                       "start": "2023-01-01 00:00:00",
                       "end": "2023-12-31 23:59:59"
                     },
                     {
                       "start": "2023-03-01 00:00:00",
                       "end": "2023-11-31 23:59:59"
                     }
                   ]
                 }
        """;

        webClient.post()
                .uri(base("transactions:filter"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].profit").isEqualTo(0.0);
    }

    // ---------------------------------------------------------
    // 4. Returns (NPS)
    // ---------------------------------------------------------
    @Test
    void testReturnsNps() {
        String req = """
                {
                   "age": 29,
                   "wage": 50000,
                   "inflation": 5.5,
                   "transactions": [
                     {
                       "date": "2023-02-28 15:49:20",
                       "amount": 375.0,
                       "ceiling": 400.0,
                       "remanent": 25.0
                     },
                     {
                       "date": "2023-07-01 21:59:00",
                       "amount": 620.0,
                       "ceiling": 700.0,
                       "remanent": 80.0
                     },
                     {
                       "date": "2023-10-12 20:15:30",
                       "amount": 250.0,
                       "ceiling": 300.0,
                       "remanent": 50.0
                     },
                     {
                       "date": "2023-12-17 08:09:45",
                       "amount": 480.0,
                       "ceiling": 500.0,
                       "remanent": 20.0
                     }
                   ],
                   "q": [
                     {
                       "fixed": 0,
                       "start": "2023-07-01 00:00:00",
                       "end": "2023-07-31 23:59:00"
                     }
                   ],
                   "p": [
                     {
                       "extra": 25,
                       "start": "2023-10-01 08:00:00",
                       "end": "2023-12-31 23:59:00"
                     }
                   ],
                   "k": [
                     {
                       "start": "2023-01-01 00:00:00",
                       "end": "2023-12-31 23:59:59"
                     },
                     {
                       "start": "2023-03-01 00:00:00",
                       "end": "2023-11-31 23:59:59"
                     }
                   ]
                 }
        """;

        webClient.post()
                .uri(base("returns:nps"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.savingsByDates[0].profit")
                .value(v -> assertThat(((Number) v).doubleValue()).isGreaterThan(0));
    }

    // ---------------------------------------------------------
    // 5. Returns (Index)
    // ---------------------------------------------------------
    @Test
    void testReturnsIndex() {
        String req = """
                {
                   "age": 29,
                   "wage": 50000,
                   "inflation": 5.5,
                   "transactions": [
                     {
                       "date": "2023-02-28 15:49:20",
                       "amount": 375.0,
                       "ceiling": 400.0,
                       "remanent": 25.0
                     },
                     {
                       "date": "2023-07-01 21:59:00",
                       "amount": 620.0,
                       "ceiling": 700.0,
                       "remanent": 80.0
                     },
                     {
                       "date": "2023-10-12 20:15:30",
                       "amount": 250.0,
                       "ceiling": 300.0,
                       "remanent": 50.0
                     },
                     {
                       "date": "2023-12-17 08:09:45",
                       "amount": 480.0,
                       "ceiling": 500.0,
                       "remanent": 20.0
                     }
                   ],
                   "q": [
                     {
                       "fixed": 0,
                       "start": "2023-07-01 00:00:00",
                       "end": "2023-07-31 23:59:00"
                     }
                   ],
                   "p": [
                     {
                       "extra": 25,
                       "start": "2023-10-01 08:00:00",
                       "end": "2023-12-31 23:59:00"
                     }
                   ],
                   "k": [
                     {
                       "start": "2023-01-01 00:00:00",
                       "end": "2023-12-31 23:59:59"
                     },
                     {
                       "start": "2023-03-01 00:00:00",
                       "end": "2023-11-31 23:59:59"
                     }
                   ]
                 }
        """;

        webClient.post()
                .uri(base("returns:index"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.savingsByDates[0].profit")
                .value(v -> assertThat(((Number) v).doubleValue()).isGreaterThan(0));
    }

    // ---------------------------------------------------------
    // 6. Performance
    // ---------------------------------------------------------
    @Test
    void testPerformance() {
        webClient.get()
                .uri(base("performance"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.memory").exists()
                .jsonPath("$.threads").exists();
    }
}