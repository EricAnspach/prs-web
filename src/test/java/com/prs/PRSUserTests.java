package com.prs;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.user.User;
import com.prs.business.user.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PRSUserTests {
	@Autowired
	private UserRepository userRepo;
	
	@Test
	public void getAllUsersAndCheckNotNull() {
		// get all users
		Iterable<User> users = userRepo.findAll();
		assertNotNull(users);
	}
}
