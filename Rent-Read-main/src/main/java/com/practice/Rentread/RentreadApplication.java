package com.practice.Rentread;

import com.practice.Rentread.Entities.Book;
import com.practice.Rentread.Entities.Role;
import com.practice.Rentread.Entities.User;
import com.practice.Rentread.Repository.BookRepository;
import com.practice.Rentread.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RentreadApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(RentreadApplication.class, args);
	}

	public void run(String... args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);

		if(adminAccount == null){
			User user = new User();
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setEmail("admin@gmail.com");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}

		Book book = new Book();
		book.setAvailabilityStatus(true);
		book.setTitle("Book 1");
		book.setGenre("action");
		book.setAuthor("Arthur");
		bookRepository.save(book);
	}

}
