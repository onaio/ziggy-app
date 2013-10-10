package org.ei.ziggy.event;

public interface Listener<T> {
    void onEvent(T data);
}
