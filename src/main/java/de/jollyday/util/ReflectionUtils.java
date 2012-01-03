/**
 * Copyright 2012 Sven Diedrichs en 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */
package de.jollyday.util;

/**
 * @author José Pedro Pereira - Linkare TI
 * 
 */
public final class ReflectionUtils {
    public static final Class<?> loadClass(String className) throws ClassNotFoundException {
	try {
	    return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
	} catch (Exception e) {
	    return Class.forName(className);
	}
    }
}
