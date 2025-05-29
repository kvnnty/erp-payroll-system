package rw.gov.erp.v1.utils.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperConfig {
  private static final ModelMapper modelMapper = new ModelMapper();

  static {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  }

  public static <S, D> D map(S source, Class<D> destinationType) {
    return modelMapper.map(source, destinationType);
  }
}
