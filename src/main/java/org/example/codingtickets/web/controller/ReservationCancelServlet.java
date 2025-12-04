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

            resp.sendRedirect("history?success=Reservation annulee avec succes");

        } catch (AnnulationTardiveException e) {
            String message = "Impossible d'annuler : l'événement est trop proche (moins de 24h).";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            resp.sendRedirect("history?error=" + encodedMessage);

        } catch (Exception e) {
            e.fillInStackTrace();
            resp.sendRedirect("history?error=" + URLEncoder.encode("Erreur technique lors de l'annulation.", StandardCharsets.UTF_8));
        }
    }
}