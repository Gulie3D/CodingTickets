package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Servlet pour supprimer un événement (réservée aux organisateurs).
 * POST /events/delete → Supprime l'événement
 */
@WebServlet(name = "eventDeleteServlet", value = "/events/delete")
public class EventDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            String idParam = req.getParameter("eventId");
            if (idParam == null || idParam.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/events/my");
                return;
            }

            long eventId = Long.parseLong(idParam);
            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            
            service.supprimerEvenement((Organisateur) user, eventId);

            String msg = URLEncoder.encode("Événement supprimé avec succès.", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/events/my?success=" + msg);

        } catch (Exception e) {
            String msg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/events/my?error=" + msg);
        }
    }
}
