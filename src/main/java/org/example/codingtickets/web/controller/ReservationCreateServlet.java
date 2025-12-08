package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "reservationCreateServlet", value = "/reservations/create")
public class ReservationCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long eventId = Long.parseLong(req.getParameter("eventId"));
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
            Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

            if (!(user instanceof Client)) {
                resp.sendRedirect(req.getContextPath() + "/events");
                return;
            }

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.reserver((Client) user, eventId, nbPlaces);

            resp.sendRedirect(req.getContextPath() + "/reservations/history?success=Reservation confirmee");

        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            req.setAttribute("events", service.listerEvenements());

            req.getRequestDispatcher("/WEB-INF/jsp/events.jsp").forward(req, resp);
        }
    }
}