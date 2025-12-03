package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;

@WebServlet(name = "reservationCreateServlet", value = "/reservations/create")
public class ReservationCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long eventId = Long.parseLong(req.getParameter("eventId"));
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));

            Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

            // Vérifier que c'est bien un client (si tu veux être strict)
            if (!(user instanceof Client)) {
                resp.sendError(403, "Seuls les clients peuvent réserver.");
                return;
            }

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.reserver((Client) user, eventId, nbPlaces);

            // Redirection succès
            resp.sendRedirect(req.getContextPath() + "/reservations/history");

        } catch (Exception e) {
            // En cas d'erreur (ex: plus de places), on peut renvoyer une erreur simple pour l'instant
            resp.sendError(400, "Erreur de réservation : " + e.getMessage());
        }
    }
}