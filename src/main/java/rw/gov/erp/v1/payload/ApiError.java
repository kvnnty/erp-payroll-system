package rw.gov.erp.v1.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
  private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String details;
}
