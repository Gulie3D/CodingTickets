package org.example.codingtickets.web.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import org.example.codingtickets.service.TicketService;

@WebListener
public class ServletContextListener implements jakarta.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TicketService ticketService = new TicketService();

        sce.getServletContext().setAttribute("ticketService", ticketService);

        System.out.println("CodingTickets : TicketService initialisé et stocké dans le contexte.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CodingTickets : Contexte detruit.");
    }
}