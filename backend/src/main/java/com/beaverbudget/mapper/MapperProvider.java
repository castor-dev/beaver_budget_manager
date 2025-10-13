package com.beaverbudget.mapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MapperProvider {

  private final Map<String, GenericMapper<?, ?>> mapperRegistry = new ConcurrentHashMap<>();

  public MapperProvider(List<GenericMapper<?, ?>> mappers) {
    for (GenericMapper<?, ?> mapper : mappers) {
      String key = key(mapper.getSourceType(), mapper.getTargetType());
      mapperRegistry.put(key, mapper);
    }
  }

  @SuppressWarnings("unchecked")
  public <S, T> T map(S source, Class<T> targetType) {
    if (source == null) return null;

    GenericMapper<S, T> mapper =
        (GenericMapper<S, T>) mapperRegistry.get(key(source.getClass(), targetType));

    if (mapper == null) {
      throw new IllegalArgumentException(
          "No mapper found for "
              + source.getClass().getSimpleName()
              + " → "
              + targetType.getSimpleName());
    }

    return mapper.map(source);
  }

  private String key(Class<?> source, Class<?> target) {
    return source.getName() + "->" + target.getName();
  }
}
