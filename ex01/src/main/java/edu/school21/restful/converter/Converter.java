package edu.school21.restful.converter;

public interface Converter<T, S> {
    T convert(S source);
}
