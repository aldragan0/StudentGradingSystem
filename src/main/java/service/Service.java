package service;

import java.util.Collection;

public interface Service<T> {
    Collection<T> getAll();
}
