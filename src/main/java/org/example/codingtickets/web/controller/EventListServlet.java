package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Evenement;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "eventListServlet", value = "/events")
public class EventListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Evenement> evenements = service.listerEvenements();
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // Petit formatteur pour rendre la date jolie (ex: 04/12/2025 14:30)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        out.println("<html><body>");
        out.println("<h1>Liste des événements</h1>");

        if (user != null) {
            out.println("<p>Connecté en tant que : <b>" + user.getNom() + "</b> (" + user.getEmail() + ")");
            out.println(" | <a href='reservations/history'>Mes réservations</a>");
            out.println(" | <a href='logout'>Déconnexion</a></p>");
        } else {
            out.println("<p><a href='login'>Se connecter</a></p>");
        }

        out.println("<table border='1' cellpadding='5'><tr>");
        out.println("<th>Date</th>"); // Nouvelle colonne
        out.println("<th>Titre</th>");
        out.println("<th>Lieu</th>");
        out.println("<th>Places</th>");
        out.println("<th>Prix</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        for (Evenement ev : evenements) {
            out.println("<tr>");
            out.println("<td>" + ev.getDateEvenement().format(formatter) + "</td>");

            out.println("<td><strong>" + ev.getTitre() + "</strong><br><i>" + ev.getDescription() + "</i></td>");
            out.println("<td>" + ev.getLieu() + "</td>");

            // Gestion visuelle des places (rouge si complet)
            String placesInfo = ev.getNbPlacesRestantes() + "/" + ev.getNbPlacesTotales();
            if (ev.getNbPlacesRestantes() == 0) {
                placesInfo = "<span style='color:red'>COMPLET</span>";
            }
            out.println("<td>" + placesInfo + "</td>");

            out.println("<td>" + ev.getPrixBase() + " €</td>");

            out.println("<td>");
            if (ev.getNbPlacesRestantes() > 0 && user != null) {
                out.println("<form action='reservations/create' method='post'>");
                out.println("<input type='hidden' name='eventId' value='" + ev.getId() + "'>");
                out.println("Qté: <input type='number' name='nbPlaces' value='1' min='1' max='" + ev.getNbPlacesRestantes() + "' style='width:40px'>");
                out.println("<button type='submit'>Réserver</button>");
                out.println("</form>");
            } else if (user == null) {
                out.println("<i>Connectez-vous</i>");
            } else {
                out.println("Indisponible");
            }
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
}