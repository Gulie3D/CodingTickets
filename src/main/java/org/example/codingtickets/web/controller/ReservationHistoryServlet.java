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

@WebServlet(name = "reservationHistoryServlet", value = "/reservations/history")
public class ReservationHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Client)) {
            resp.getWriter().println("Accès réservé aux clients.");
            return;
        }

        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Reservation> reservations = service.listerReservationsClient((Client) user);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Mes Réservations</h1>");
        out.println("<a href='../events'>Retour aux événements</a>");

        out.println("<ul>");
        for (Reservation r : reservations) {
            out.println("<li>");
            out.println("Événement : " + r.getEvenement().getTitre() + " | ");
            out.println("Places : " + r.getNbPlaces() + " | ");
            out.println("Statut : " + r.getStatut());

            if (r.getStatut().toString().equals("CONFIRMEE")) {
                out.println("<form action='cancel' method='post' style='display:inline'>");
                out.println("<input type='hidden' name='reservationId' value='" + r.getId() + "'>");
                out.println("<button type='submit'>Annuler</button>");
                out.println("</form>");
            }
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("</body></html>");
    }
}