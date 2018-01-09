/*
 * Copyright 2015 Time Warner Cable, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twcable.jackalope.impl.sling;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.xss.XSSFilter;
import org.apache.sling.xss.impl.XSSFilterImpl;

import java.lang.reflect.Method;

/**
 * Extension of Sling XSS API.
 */
public class XSSAPIImpl extends org.apache.sling.xss.impl.XSSAPIImpl {
    public XSSAPIImpl(SlingRepository repository) {
        try {
            XSSFilter filter = new XSSFilterImpl();
            Method bindResourceResolverFactory = filter.getClass().getDeclaredMethod("bindResourceResolverFactory", ResourceResolverFactory.class);
            bindResourceResolverFactory.setAccessible(true);
            bindResourceResolverFactory.invoke(filter, new SimpleResourceResolverFactory(repository));

            Method activate = filter.getClass().getDeclaredMethod("activate");
            activate.setAccessible(true);
            activate.invoke(filter);

            Method bindXssFilter = org.apache.sling.xss.impl.XSSAPIImpl.class.getDeclaredMethod("bindXssFilter", XSSFilter.class);
            bindXssFilter.setAccessible(true);
            bindXssFilter.invoke(this, filter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
