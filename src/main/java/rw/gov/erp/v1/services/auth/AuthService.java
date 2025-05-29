package rw.gov.erp.v1.services.auth;

import rw.gov.erp.v1.dtos.requests.auth.LoginRequestDto;
import rw.gov.erp.v1.dtos.responses.auth.AuthResponseDTO;

public interface AuthService {
  AuthResponseDTO login(LoginRequestDto requestDTO);
}
