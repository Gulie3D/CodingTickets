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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet(name = "eventEditServlet", value = "/events/edit")
public class EventEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/events/my");
            return;
        }

        long id = Long.parseLong(idStr);
        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        Evenement ev = service.trouverEvenementParId(id);

        if (ev == null || !ev.getOrganisateur().getId().equals(user.getId())) {
            resp.sendRedirect(req.getContextPath() + "/events/my");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Modifier l'événement</title></head><body>");
        out.println("<h1>Modifier : " + ev.getTitre() + "</h1>");
        out.println("<p>Connecté en tant que : <b>" + user.getNom() + "</b> (Organisateur)</p>");
        out.println("<a href='" + req.getContextPath() + "/events/my'>Annuler</a>");
        out.println("<hr/>");

        String error = (String) req.getSession().getAttribute("error");
        if (error != null) {
            out.println("<p style='color:red;'><b>Erreur : </b>" + error + "</p>");
            req.getSession().removeAttribute("error");
        }

        // Formulaire pré-rempli
        out.println("<form method='post' action='" + req.getContextPath() + "/events/edit'>");
        out.println("<input type='hidden' name='id' value='" + ev.getId() + "'/>");

        out.println("<table>");
        out.println("<tr><td>Titre *</td><td><input type='text' name='titre' value='" + ev.getTitre() + "' required size='40'/></td></tr>");
        out.println("<tr><td>Description</td><td><textarea name='description' rows='3' cols='40'>" + ev.getDescription() + "</textarea></td></tr>");
        out.println("<tr><td>Date et heure *</td><td><input type='datetime-local' name='dateEvenement' value='" + ev.getDateEvenement() + "' required/></td></tr>");
        out.println("<tr><td>Lieu *</td><td><input type='text' name='lieu' value='" + ev.getLieu() + "' required size='40'/></td></tr>");
        out.println("<tr><td>Nombre de places *</td><td><input type='number' name='nbPlaces' value='" + ev.getNbPlacesTotales() + "' min='1' required/></td></tr>");
        out.println("<tr><td>Prix (€) *</td><td><input type='number' name='prix' value='" + ev.getPrixBase() + "' min='0' step='0.01' required/></td></tr>");
        out.println("</table>");
        out.println("<br/><button type='submit'>Enregistrer les modifications</button>");
        out.println("</form>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (!(user instanceof Organisateur)) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return;
        }

        try {
            long id = Long.parseLong(req.getParameter("id"));
            String titre = req.getParameter("titre");
            String description = req.getParameter("description");
            LocalDateTime date = LocalDateTime.parse(req.getParameter("dateEvenement"));
            String lieu = req.getParameter("lieu");
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
            BigDecimal prix = new BigDecimal(req.getParameter("prix"));

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
            service.modifierEvenement((Organisateur) user, id, titre, description, date, lieu, nbPlaces, prix);

            req.getSession().setAttribute("success", "Événement modifié avec succès !");
            resp.sendRedirect(req.getContextPath() + "/events/my");

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
            String id = req.getParameter("id");
            resp.sendRedirect(req.getContextPath() + "/events/edit?id=" + id);
        }
    }
}