package org.example.codingtickets.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*") // Filtre toutes les URL
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // URL demandée
        String path = req.getServletPath();

        // On laisse passer /login, /logout et les ressources statiques (si besoin)
        if (path.equals("/login") || path.startsWith("/css")) {
            chain.doFilter(request, response);
            return;
        }

        // Vérification session
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Utilisateur connecté -> OK
            chain.doFilter(request, response);
        } else {
            // Pas connecté -> Redirection login
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}