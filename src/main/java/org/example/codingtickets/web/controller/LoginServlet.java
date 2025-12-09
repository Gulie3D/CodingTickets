package org.example.codingtickets.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.codingtickets.model.Utilisateur;
import org.example.codingtickets.service.TicketService;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Connexion</h1>");
        // Formulaire minimal
        resp.getWriter().println("<form method='post' action='login'>");
        resp.getWriter().println("Email: <input type='text' name='email'><br>");
        resp.getWriter().println("Password: <input type='password' name='password'><br>");
        resp.getWriter().println("<input type='submit' value='Se connecter'>");
        resp.getWriter().println("</form>");

        // Affichage message d'erreur si présent
        String error = (String) req.getAttribute("error");
        if (error != null) {
            resp.getWriter().println("<p style='color:red'>" + error + "</p>");
        }

        resp.getWriter().println("<hr/>");
        resp.getWriter().println("<h3>Comptes de Test</h3>");

        resp.getWriter().println("<p><b>Organisateurs :</b><br>");
        resp.getWriter().println("- bob_organisateur@coding.fr / bob123<br>");
        resp.getWriter().println("- marie_organisatrice@coding.fr / marie123</p>");

        resp.getWriter().println("<p><b>Clients :</b><br>");
        resp.getWriter().println("- alice_cliente@coding.fr / alice123<br>");
        resp.getWriter().println("- charlie_client@coding.fr / charlie123<br>");
        resp.getWriter().println("- diana_cliente@coding.fr / diana123</p>");

        resp.getWriter().println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Récupération du service depuis le contexte (mis par le Listener)
        TicketService service = (TicketService) getServletContext().getAttribute("ticketService");
        Utilisateur user = service.authentifier(email, password);

        if (user != null) {
            // Connexion réussie : on crée la session
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/events");
        } else {
            // Échec
            req.setAttribute("error", "Email ou mot de passe incorrect");
            doGet(req, resp); // On réaffiche le formulaire avec l'erreur
        }
    }
}
