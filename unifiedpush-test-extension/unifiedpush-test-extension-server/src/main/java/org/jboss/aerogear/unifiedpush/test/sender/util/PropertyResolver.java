/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.test.sender.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class PropertyResolver<T> {

    private final T defaultValue;
    private final String[] possibleProperties;

    private PropertyInstantiator<T> instantiator;
    private PropertyInputVerifier inputVerifier;
    private PropertyOutputVerifier<T> outputVerifier;

    @SuppressWarnings("unchecked")
    private PropertyResolver(T defaultValue, String... possibleProperties) {
        this((Class<T>) defaultValue.getClass(), defaultValue, possibleProperties);
    }

    private PropertyResolver(Class<T> propertyClass, String... possibleProperties) {
        this(propertyClass, null, possibleProperties);
    }

    private PropertyResolver(Class<T> propertyClass, T defaultValue, String... possibleProperties) {
        this.instantiator = new ReflectionPropertyInstantiator<T>(propertyClass);
        this.inputVerifier = new DefaultInputVerifier();
        this.outputVerifier = new DefaultOutputVerifier<T>();
        this.defaultValue = defaultValue;
        this.possibleProperties = possibleProperties;
    }

    public PropertyResolver<T> instantiateWith(PropertyInstantiator<T> instantiator) {
        this.instantiator = instantiator;
        return this;
    }

    public PropertyResolver<T> verifyInputWith(PropertyInputVerifier inputVerifier) {
        this.inputVerifier = inputVerifier;
        return this;
    }

    public PropertyResolver<T> verifyOutputWith(PropertyOutputVerifier<T> outputVerifier) {
        this.outputVerifier = outputVerifier;
        return this;
    }

    public T resolve() {
        return resolve(System.getProperties());
    }

    public T resolve(Properties properties) {
        for (String possibleProperty : possibleProperties) {
            if (!properties.containsKey(possibleProperty)) {
                continue;
            }

            String propertyValue = properties.getProperty(possibleProperty);
            if (!inputVerifier.verify(propertyValue)) {
                continue;
            }

            T propertyInstance = instantiator.instantiate(propertyValue);

            if (!outputVerifier.verify(propertyInstance)) {
                continue;
            }
            return propertyInstance;
        }

        return defaultValue;
    }

    public static <T> PropertyResolver<T> with(T defaultValue, String... possibleProperties) {
        return new PropertyResolver<T>(defaultValue, possibleProperties);
    }

    public static <T> PropertyResolver<T> with(Class<T> propertyClass, String... possibleProperties) {
        return new PropertyResolver<T>(propertyClass, possibleProperties);
    }

    public interface PropertyInstantiator<T> {
        T instantiate(String value);
    }

    public interface PropertyInputVerifier {
        boolean verify(String inputValue);
    }

    public interface PropertyOutputVerifier<T> {
        boolean verify(T outputValue);
    }

    private static class DefaultInputVerifier implements PropertyInputVerifier {
        @Override
        public boolean verify(String inputValue) {
            return inputValue != null && inputValue.length() > 0;
        }
    }

    private static class DefaultOutputVerifier<T> implements PropertyOutputVerifier<T> {
        @Override
        public boolean verify(T value) {
            return value != null;
        }
    }

    private static class ReflectionPropertyInstantiator<T> implements PropertyInstantiator<T> {

        private final Class<T> propertyClass;

        private ReflectionPropertyInstantiator(Class<T> propertyClass) {
            this.propertyClass = propertyClass;
        }

        @Override
        public T instantiate(String value) {
            try {
                Constructor<T> constructor = propertyClass.getDeclaredConstructor(String.class);
                return constructor.newInstance(value);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
