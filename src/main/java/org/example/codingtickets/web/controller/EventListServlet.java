package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Evenement;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "eventListServlet", value = "/events")
public class EventListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Evenement> evenements = service.listerEvenements();
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Liste des événements</h1>");
        out.println("<p>Bienvenue " + user.getNom() + " | <a href='reservations/history'>Mes réservations</a> | <a href='logout'>Déconnexion</a></p>");

        out.println("<table border='1'><tr><th>Titre</th><th>Places Restantes</th><th>Prix</th><th>Action</th></tr>");

        for (Evenement ev : evenements) {
            out.println("<tr>");
            out.println("<td>" + ev.getTitre() + "</td>");
            out.println("<td>" + ev.getNbPlacesRestantes() + "/" + ev.getNbPlacesTotales() + "</td>");
            out.println("<td>" + ev.getPrixBase() + " €</td>");
            out.println("<td>");
            // Formulaire simple pour réserver
            out.println("<form action='reservations/create' method='post'>");
            out.println("<input type='hidden' name='eventId' value='" + ev.getId() + "'>");
            out.println("Nb places: <input type='number' name='nbPlaces' value='1' min='1' style='width:50px'>");
            out.println("<button type='submit'>Réserver</button>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
}
