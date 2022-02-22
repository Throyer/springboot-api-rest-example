package com.github.throyer.common.springboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.throyer.common.springboot.utils.Constants.RATE_LIMIT.MAX_REQUESTS_PER_MINUTE;
import static java.lang.String.valueOf;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = MOCK)
@DirtiesContext(classMode = BEFORE_CLASS)
public class RateLimitTests {
    @Autowired
    private MockMvc api;

    @Test
    @DisplayName("Deve retornar TOO_MANY_REQUESTS quando a quantidade de requests passar do limite.")
    public void should_return_TOO_MANY_REQUESTS_when_number_of_requests_exceeds_the_limit() throws Exception {
        var request = get("/api")
                .header(CONTENT_TYPE, APPLICATION_JSON);

        for (int index = MAX_REQUESTS_PER_MINUTE; index > 0; index--) {
            api.perform(request)
                    .andExpect(header().string("X-Rate-Limit-Remaining", valueOf(index - 1)))
                    .andExpect(status().isOk());
        }

        api.perform(request)
                .andExpect(status().isTooManyRequests());
    }
}
