package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Evenement;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servlet pour afficher les événements d'un organisateur.
 * GET /events/my → Liste des événements créés par l'organisateur connecté
 */
@WebServlet(name = "myEventsServlet", value = "/events/my")
public class MyEventsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Vérifier que c'est un organisateur
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        // 2. Récupérer ses événements
        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Evenement> mesEvenements = service.listerEvenementsOrganisateur((Organisateur) user);

        // 3. Afficher la page
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        out.println("<html><head><title>Mes événements</title></head><body>");
        out.println("<h1>Mes événements</h1>");
        out.println("<p>Connecté en tant que : <b>" + user.getNom() + "</b> (Organisateur)</p>");
        out.println("<a href='" + req.getContextPath() + "/events'>Tous les événements</a>");
        out.println(" | <a href='" + req.getContextPath() + "/events/create'>Créer un événement</a>");
        out.println(" | <a href='" + req.getContextPath() + "/logout'>Déconnexion</a>");
        out.println("<hr/>");

        // Message de succès si présent (stocké en session)
        String success = (String) req.getSession().getAttribute("success");
        if (success != null) {
            out.println("<p style='color:green; border:1px solid green; padding:10px;'>" + success + "</p>");
            req.getSession().removeAttribute("success"); // Supprimer après affichage
        }

        // 4. Afficher le tableau des événements
        if (mesEvenements.isEmpty()) {
            out.println("<p>Vous n'avez créé aucun événement.</p>");
            out.println("<p><a href='" + req.getContextPath() + "/events/create'>Créer mon premier événement</a></p>");
        } else {
            out.println("<p>Vous avez <b>" + mesEvenements.size() + "</b> événement(s).</p>");
            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr style='background-color:#f0f0f0;'>");
            out.println("<th>Date</th>");
            out.println("<th>Titre</th>");
            out.println("<th>Lieu</th>");
            out.println("<th>Places</th>");
            out.println("<th>Prix</th>");
            out.println("</tr>");

            for (Evenement ev : mesEvenements) {
                out.println("<tr>");
                out.println("<td>" + ev.getDateEvenement().format(formatter) + "</td>");
                out.println("<td><strong>" + ev.getTitre() + "</strong><br/><i>" + ev.getDescription() + "</i></td>");
                out.println("<td>" + ev.getLieu() + "</td>");

                // Affichage places restantes / totales
                String placesInfo = ev.getNbPlacesRestantes() + " / " + ev.getNbPlacesTotales();
                if (ev.getNbPlacesRestantes() == 0) {
                    placesInfo = "<span style='color:red;'>COMPLET (0/" + ev.getNbPlacesTotales() + ")</span>";
                } else if (ev.getNbPlacesRestantes() < ev.getNbPlacesTotales()) {
                    int reservees = ev.getNbPlacesTotales() - ev.getNbPlacesRestantes();
                    placesInfo = ev.getNbPlacesRestantes() + " / " + ev.getNbPlacesTotales() + " (" + reservees + " réservée(s))";
                }
                out.println("<td>" + placesInfo + "</td>");

                out.println("<td>" + ev.getPrixBase() + " €</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</body></html>");
    }
}
