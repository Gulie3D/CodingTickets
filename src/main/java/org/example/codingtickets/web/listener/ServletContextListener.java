package org.example.codingtickets.web.listener;

import org.example.codingtickets.service.TicketService;

public class ServletContextListener {
    private static final TicketService ticketService = new TicketService();

    public static TicketService getTicketService() {
        return ticketService;
        }
    }
