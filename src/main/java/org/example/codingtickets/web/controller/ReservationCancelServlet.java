package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;

@WebServlet(name = "reservationCancelServlet", value = "/reservations/cancel")
public class ReservationCancelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long resId = Long.parseLong(req.getParameter("reservationId"));
            Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.annulerReservation(resId, (Client) user);

            resp.sendRedirect("history"); // Recharge la page historique
        } catch (Exception e) {
            resp.sendError(400, "Erreur annulation : " + e.getMessage());
        }
    }
}