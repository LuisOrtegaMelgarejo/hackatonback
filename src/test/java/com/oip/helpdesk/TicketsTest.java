package com.oip.helpdesk;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.oip.helpdesk.repository.TicketRepository;
import com.oip.helpdesk.repository.UserRepository;
import com.oip.helpdesk.service.TicketService;
import com.oip.helpdesk.service.UserService;

public class TicketsTest {

	@InjectMocks
	TicketService ticketService;
	@Mock
	TicketRepository ticketRepository;

    private static final long ID = 1;
    @Test
	public void findByIdTest() {
		//ticketService.findById(ID);
		//verify(ticketRepository);

	}

}
