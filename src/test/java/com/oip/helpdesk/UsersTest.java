package com.oip.helpdesk;


import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oip.helpdesk.repository.UserRepository;
import com.oip.helpdesk.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsersTest {


		@InjectMocks
		UserService userService;
		@Mock
		UserRepository userRepository;

	    private static final long ID = 1;
	    @Test
		public void findByIdTest() {
			//userService.findById(ID);
			//verify(userRepository);
           // success();
		}


}
