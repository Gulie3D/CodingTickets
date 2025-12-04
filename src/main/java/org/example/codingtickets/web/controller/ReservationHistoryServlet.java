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

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Mes Réservations</h1>");
        out.println("<a href='" + req.getContextPath() + "/events'>Retour aux événements</a><hr/>");

        String error = req.getParameter("error");
        if (error != null) {
            out.println("<div style='color:red; border:1px solid red; padding:10px; margin-bottom:10px;'>");
            out.println("<strong>Erreur : </strong>" + error);
            out.println("</div>");
        }

        String success = req.getParameter("success");
        if (success != null) {
            out.println("<div style='color:green; border:1px solid green; padding:10px; margin-bottom:10px;'>");
            out.println(success);
            out.println("</div>");
        }

        if (reservations.isEmpty()) {
            out.println("<p>Vous n'avez aucune réservation.</p>");
        } else {
            out.println("<ul>");
            for (Reservation r : reservations) {
                out.println("<li>");
                out.println("<strong>" + r.getEvenement().getTitre() + "</strong> le " + r.getEvenement().getDateEvenement());
                out.println("<br>Places : " + r.getNbPlaces() + " | Statut : " + r.getStatut());

                if (r.getStatut().toString().equals("CONFIRMEE")) {
                    out.println("<form action='cancel' method='post' style='display:inline; margin-left:10px;'>");
                    out.println("<input type='hidden' name='reservationId' value='" + r.getId() + "'>");
                    out.println("<button type='submit' style='cursor:pointer; color:red;'>Annuler</button>");
                    out.println("</form>");
                }
                out.println("</li><br>");
            }
            out.println("</ul>");
        }
        out.println("</body></html>");
    }
}