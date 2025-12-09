package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.codingtickets.model.Evenement;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Servlet pour modifier un événement (réservée aux organisateurs).
 * GET  /events/edit?id=X → Affiche le formulaire pré-rempli
 * POST /events/edit      → Enregistre les modifications
 */
@WebServlet(name = "eventEditServlet", value = "/events/edit")
public class EventEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/events/my");
            return;
        }

        try {
            long eventId = Long.parseLong(idParam);
            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            Evenement event = service.trouverEvenementParId(eventId);

            if (event == null) {
                req.getSession().setAttribute("error", "Événement introuvable");
                resp.sendRedirect(req.getContextPath() + "/events/my");
                return;
            }

            // Vérifier que l'organisateur est le propriétaire
            if (!event.getOrganisateur().getId().equals(user.getId())) {
                req.getSession().setAttribute("error", "Vous n'êtes pas autorisé à modifier cet événement");
                resp.sendRedirect(req.getContextPath() + "/events/my");
                return;
            }

            req.setAttribute("event", event);
            req.setAttribute("editMode", true);
            req.getRequestDispatcher("/WEB-INF/jsp/event-form.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/events/my");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            long eventId = Long.parseLong(req.getParameter("eventId"));
            String titre = req.getParameter("titre");
            String description = req.getParameter("description");
            String dateStr = req.getParameter("dateEvenement");
            String lieu = req.getParameter("lieu");
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
            BigDecimal prix = new BigDecimal(req.getParameter("prix"));

            LocalDateTime dateEvenement = LocalDateTime.parse(dateStr);

            // Validation : titre obligatoire
            if (titre == null || titre.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Le titre est obligatoire");
                resp.sendRedirect(req.getContextPath() + "/events/edit?id=" + eventId);
                return;
            }

            // Validation : date dans le futur
            if (dateEvenement.isBefore(LocalDateTime.now())) {
                req.getSession().setAttribute("error", "La date doit être dans le futur");
                resp.sendRedirect(req.getContextPath() + "/events/edit?id=" + eventId);
                return;
            }

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.modifierEvenement(
                    (Organisateur) user,
                    eventId,
                    titre.trim(),
                    description != null ? description.trim() : "",
                    dateEvenement,
                    lieu.trim(),
                    nbPlaces,
                    prix
            );

            req.getSession().setAttribute("success", "Événement modifié avec succès !");
            resp.sendRedirect(req.getContextPath() + "/events/my?success=modified");

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
            String eventId = req.getParameter("eventId");
            if (eventId != null) {
                resp.sendRedirect(req.getContextPath() + "/events/edit?id=" + eventId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/events/my");
            }
        }
    }
}
