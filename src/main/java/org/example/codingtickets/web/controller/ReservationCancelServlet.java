package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.exception.AnnulationTardiveException; // N'oublie pas l'import !
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "reservationCancelServlet",value = "/reservations/cancel")
public class ReservationCancelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("reservationId");
            if (idParam == null || idParam.isEmpty()) {
                resp.sendRedirect("history");
                return;
            }
            long resId = Long.parseLong(idParam);

            Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.annulerReservation(resId, (Client) user);

            String msg = URLEncoder.encode("Réservation annulée avec succès.", StandardCharsets.UTF_8);
            resp.sendRedirect("history?success=" + msg);

        } catch (Exception e) {
            String msg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect("history?error=" + msg);
        }
    }
}