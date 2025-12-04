package org.example.codingtickets.web.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import org.example.codingtickets.service.TicketService;

@WebListener
public class ServletContextListener implements jakarta.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Instancier le service
        TicketService ticketService = new TicketService();

        // 2. Le stocker dans le contexte applicatif (accessible par toutes les servlets)
        sce.getServletContext().setAttribute("ticketService", ticketService);

        System.out.println("CodingTickets : TicketService initialisé et stocké dans le contexte.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CodingTickets : Contexte detruit.");
    }
}