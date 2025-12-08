package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servlet pour créer un événement (réservée aux organisateurs).
 * GET  /events/create → Affiche le formulaire
 * POST /events/create → Crée l'événement
 */
@WebServlet(name = "eventCreateServlet", value = "/events/create")
public class EventCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/jsp/event-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            String titre = req.getParameter("titre");
            String description = req.getParameter("description");
            String dateStr = req.getParameter("dateEvenement");
            String lieu = req.getParameter("lieu");
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
            BigDecimal prix = new BigDecimal(req.getParameter("prix"));

            LocalDateTime dateEvenement = LocalDateTime.parse(dateStr);

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

            req.getSession().setAttribute("success", "Événement créé avec succès !");
            resp.sendRedirect(req.getContextPath() + "/events/my?success=Event cree");

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/events/create");
        }
    }
}
