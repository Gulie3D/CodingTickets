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
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        List<Evenement> mesEvenements = service.listerEvenementsOrganisateur((Organisateur) user);

        req.setAttribute("myEvents", mesEvenements);

        req.getRequestDispatcher("/WEB-INF/jsp/my-events.jsp").forward(req, resp);
    }
}
