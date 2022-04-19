package edu.co.sergio.arboleda.smartparkingapi.delegate.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.co.sergio.arboleda.smartparkingapi.config.security.JwtTokenUtil;
import edu.co.sergio.arboleda.smartparkingapi.config.security.JwtUserDetailsService;
import edu.co.sergio.arboleda.smartparkingapi.delegate.UserDelegate;
import edu.co.sergio.arboleda.smartparkingapi.enums.UserType;
import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.UserRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.User;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;
import edu.co.sergio.arboleda.smartparkingapi.rest.request.LoginRequest;
import edu.co.sergio.arboleda.smartparkingapi.rest.response.LoginResponse;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Component
public class UserDelegateImpl implements UserDelegate {

	private final UserRepository userRepository;
	private final ClientRepository clientRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final JwtUserDetailsService userDetailsService;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserDelegateImpl(UserRepository userRepository,
							ClientRepository clientRepository,
							AuthenticationManager authenticationManager,
							JwtTokenUtil jwtTokenUtil,
							JwtUserDetailsService userDetailsService,
							ModelMapper modelMapper,
							PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.clientRepository = clientRepository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void create(UserApi userApi) throws GenericException {
		Optional<User> exist = userRepository.findUserByUsername(userApi.getUsername());
		if (exist.isPresent()) {
			throw new GenericException("El usuario ya existe", "USERNAME_ALREADY_EXISTS");
		}
		userApi.setPassword(passwordEncoder.encode(userApi.getPassword()));
		User user = modelMapper.map(userApi, User.class);
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void create(Client client) {
		User user = User.newBuilder()
				.withUsername(client.getEmail())
				.withPassword(passwordEncoder.encode(client.getDocumentNumber()))
				.withEnabled(Boolean.TRUE)
				.withUserType(UserType.CLIENT.getCode())
				.build();
		client.setUser(user);
		userRepository.save(user);
		clientRepository.save(client);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) throws GenericException {
		authenticate(loginRequest.getUsername(), loginRequest.getPassword());
		UserDetails userDetails = userDetailsService
				.loadUserByUsername(loginRequest.getUsername());
		String token = jwtTokenUtil.generateToken(userDetails);
		User user = userRepository.findUserByUsername(loginRequest.getUsername()).orElse(null);
		assert user != null;
		user.setPassword(null);
		return new LoginResponse(token, modelMapper.map(user, UserApi.class));
	}

	private void authenticate(String username, String password) throws GenericException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new GenericException("USER_DISABLED", "ERROR");
		} catch (BadCredentialsException | UsernameNotFoundException e) {
			throw new GenericException("INVALID_CREDENTIALS", "ERROR");
		}
	}

}
