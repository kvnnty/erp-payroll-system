package rw.gov.erp.v1.services.auth.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rw.gov.erp.v1.dtos.requests.auth.LoginRequestDto;
import rw.gov.erp.v1.dtos.responses.auth.AuthResponseDTO;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.enums.employee.EmployeeStatus;
import rw.gov.erp.v1.exceptions.BadRequestException;
import rw.gov.erp.v1.security.jwt.JwtTokenService;
import rw.gov.erp.v1.services.auth.AuthService;
import rw.gov.erp.v1.services.employee.EmployeeService;
import rw.gov.erp.v1.utils.mappers.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final EmployeeService userService;
  private final JwtTokenService jwtTokenService;

  @Override
  public AuthResponseDTO login(LoginRequestDto requestDTO) {
    Authentication authentication = authentication(requestDTO);

    if (!authentication.isAuthenticated()) {
      throw new BadRequestException("Couldn't authenticate request");
    }

    Employee user = userService.getCurrentUser();

    if (user.getStatus().equals(EmployeeStatus.DISABLED)) {
      throw new BadRequestException("Account is disabled please contact system admin for support.");
    }

    return AuthResponseDTO.builder()
        .user(UserMapper.toDto(user))
        .token(generateJwtToken(authentication))
        .build();

  }

  private Authentication authentication(LoginRequestDto requestDTO) {
    try {
      UsernamePasswordAuthenticationToken authenticationRequest = new UsernamePasswordAuthenticationToken(
          requestDTO.getEmail(), requestDTO.getPassword());
      Authentication authentication = authenticationManager.authenticate(authenticationRequest);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return authentication;

    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Incorrect email or password");
    }
  }

  private String generateJwtToken(Authentication authentication) {
    return jwtTokenService.generateToken(authentication);
  }

}
