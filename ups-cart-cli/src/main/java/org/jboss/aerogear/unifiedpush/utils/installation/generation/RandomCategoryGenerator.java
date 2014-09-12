/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.aerogear.unifiedpush.utils.installation.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Categories are random UUID strings.
 *
 * @see UUID
 *
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class RandomCategoryGenerator implements CategoryGenerator {

    /**
     * Generate list of categories of specified size.
     *
     * @param count count of categories to generate
     * @return empty list if {@code count < 1}
     */
    @Override
    public List<String> generateCategories(int count) {
        List<String> categories = new ArrayList<String>();

        if (count < 1) {
            return categories;
        }

        for (int i = 0; i < count; i++) {
            categories.add(UUID.randomUUID().toString());
        }

        return categories;
    }
}