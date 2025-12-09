package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;

@WebServlet(name = "eventDeleteServlet", value = "/events/delete")
public class EventDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            long id = Long.parseLong(req.getParameter("id"));

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");

            // Note : Dans une vraie app, on vérifierait ici que l'événement appartient bien à cet organisateur !
            service.supprimerEvenement((Organisateur) user, id);

            // 4. Redirection
            req.getSession().setAttribute("success", "Événement supprimé avec succès.");
            resp.sendRedirect(req.getContextPath() + "/events/my");

        } catch (Exception e) {
            e.fillInStackTrace();
            resp.sendRedirect(req.getContextPath() + "/events/my");
        }
    }
}