package com.example.statemachine.machine;

import java.util.HashMap;
import java.util.Map;

public class StateContext<T> {

    private T stateObject;

    private final Map<String, Object> attributes = new HashMap<>();

    public StateContext() {}

    public StateContext(T stateObject) {
        this.stateObject = stateObject;
    }

    public StateContext<T> setStateObject(T stateObject) {
        this.stateObject = stateObject;
        return this;
    }

    public T getStateObject() {
        return this.stateObject;
    }

    public void setAttribute(String name, Object value) {
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            this.attributes.remove(name);
        }
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }
}
