package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet pour créer un événement (réservée aux organisateurs).
 * GET  /events/create → Affiche le formulaire
 * POST /events/create → Crée l'événement
 */
@WebServlet(name = "eventCreateServlet", value = "/events/create")
public class EventCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Vérifier que c'est un organisateur
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        // 2. Afficher le formulaire
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Créer un événement</title></head><body>");
        out.println("<h1>Créer un nouvel événement</h1>");
        out.println("<p>Connecté en tant que : <b>" + user.getNom() + "</b> (Organisateur)</p>");
        out.println("<a href='" + req.getContextPath() + "/events'>Retour à la liste</a>");
        out.println(" | <a href='" + req.getContextPath() + "/events/my'>Mes événements</a>");
        out.println("<hr/>");

        // Message d'erreur si présent (stocké en session)
        String error = (String) req.getSession().getAttribute("error");
        if (error != null) {
            out.println("<p style='color:red;'><b>Erreur : </b>" + error + "</p>");
            req.getSession().removeAttribute("error"); // Supprimer après affichage
        }

        // Formulaire de création
        out.println("<form method='post' action='" + req.getContextPath() + "/events/create'>");
        out.println("<table>");
        out.println("<tr><td>Titre *</td><td><input type='text' name='titre' required size='40'/></td></tr>");
        out.println("<tr><td>Description</td><td><textarea name='description' rows='3' cols='40'></textarea></td></tr>");
        out.println("<tr><td>Date et heure *</td><td><input type='datetime-local' name='dateEvenement' required/></td></tr>");
        out.println("<tr><td>Lieu *</td><td><input type='text' name='lieu' required size='40'/></td></tr>");
        out.println("<tr><td>Nombre de places *</td><td><input type='number' name='nbPlaces' min='1' value='50' required/></td></tr>");
        out.println("<tr><td>Prix (€) *</td><td><input type='number' name='prix' min='0' step='0.01' value='10.00' required/></td></tr>");
        out.println("</table>");
        out.println("<br/><button type='submit'>Créer l'événement</button>");
        out.println("</form>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Vérifier que c'est un organisateur
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            // 2. Récupérer les paramètres du formulaire
            String titre = req.getParameter("titre");
            String description = req.getParameter("description");
            String dateStr = req.getParameter("dateEvenement");
            String lieu = req.getParameter("lieu");
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
            BigDecimal prix = new BigDecimal(req.getParameter("prix"));

            // 3. Convertir la date
            LocalDateTime dateEvenement = LocalDateTime.parse(dateStr);

            // 4. Validation simple
            if (titre == null || titre.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Le titre est obligatoire");
                resp.sendRedirect(req.getContextPath() + "/events/create");
                return;
            }
            if (dateEvenement.isBefore(LocalDateTime.now())) {
                req.getSession().setAttribute("error", "La date doit être dans le futur");
                resp.sendRedirect(req.getContextPath() + "/events/create");
                return;
            }

            // 5. Créer l'événement via le service
            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.creerEvenement(
                    (Organisateur) user,
                    titre.trim(),
                    description != null ? description.trim() : "",
                    dateEvenement,
                    lieu.trim(),
                    nbPlaces,
                    prix
            );

            // 6. Rediriger vers "Mes événements" avec message de succès
            req.getSession().setAttribute("success", "Événement créé avec succès !");
            resp.sendRedirect(req.getContextPath() + "/events/my");

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/events/create");
        }
    }
}
