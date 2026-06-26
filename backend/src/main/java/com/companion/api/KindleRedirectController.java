package com.companion.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class KindleRedirectController {

    @Value("${companion.kindle.token}")
    private String kindleToken;

    @GetMapping("/k")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/sse/kindle?token=" + kindleToken);
    }
}
