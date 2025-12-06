package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "reservationCreateServlet", value = "/reservations/create")
public class ReservationCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Pour gérer les accents correctement dans la réponse HTML
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        try {
            long eventId = Long.parseLong(req.getParameter("eventId"));
            int nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));

            Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");

            if (!(user instanceof Client)) {
                afficherErreur(resp, "Seuls les clients peuvent réserver.");
                return;
            }

            TicketService service = (TicketService) getServletContext().getAttribute("ticketService");

            // Si cette ligne échoue (ex: pas assez de place), on saute dans le CATCH
            service.reserver((Client) user, eventId, nbPlaces);

            // Succès : redirection vers l'historique
            resp.sendRedirect(req.getContextPath() + "/reservations/history");

        } catch (Exception e) {
            // C'est ici qu'on capture ton exception "PlacesInsuffisantesException"
            // Au lieu d'une erreur technique, on affiche une page HTML propre
            afficherErreur(resp, e.getMessage());
        }
    }

    // Petite méthode utilitaire pour générer le HTML d'erreur
    private void afficherErreur(HttpServletResponse resp, String message) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Erreur de réservation</title></head>");
        out.println("<body style='font-family: Arial; text-align: center; margin-top: 50px;'>");

        out.println("<h1 style='color: red;'>Oups ! Impossible de réserver</h1>");
        out.println("<p style='font-size: 18px;'>" + message + "</p>");

        // Un bouton pour revenir en arrière
        out.println("<a href='javascript:history.back()' style='padding: 10px 20px; background: #333; color: white; text-decoration: none; border-radius: 5px;'>Retour</a>");

        out.println("</body>");
        out.println("</html>");
    }
}