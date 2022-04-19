package edu.co.sergio.arboleda.smartparkingapi.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public JwtUserDetailsService(
			UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException {

		edu.co.sergio.arboleda.smartparkingapi.repository.entity.User user = userRepository.findUserByUsername(name)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + name));

		assert user != null;
		userRepository.saveAndFlush(user);

		return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true,
				true, Collections.emptyList());
	}

}
