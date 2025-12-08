package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Reservation;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "reservationHistory", value = "/reservations/history")
public class ReservationHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

        if (!(user instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Reservation> reservations = service.listerReservationsClient((Client) user);

        req.setAttribute("reservations", reservations);

        req.getRequestDispatcher("/WEB-INF/jsp/reservations.jsp").forward(req, resp);
    }
}