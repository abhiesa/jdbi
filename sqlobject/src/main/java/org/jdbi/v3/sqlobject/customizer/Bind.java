/*
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
package org.jdbi.v3.sqlobject.customizer;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import org.jdbi.v3.sqlobject.internal.ParameterUtil;

/**
 * Binds the annotated argument as a named parameter, and as a positional parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@SqlStatementCustomizingAnnotation(Bind.Factory.class)
public @interface Bind
{
    String NO_VALUE = "";

    /**
     * The name to bind the argument to. If omitted, the name of the annotated parameter is used. It is an
     * error to omit the name when there is no parameter naming information in your class files.
     *
     * @return the name to which the argument will be bound.
     */
    String value() default NO_VALUE;

    class Factory implements SqlStatementCustomizerFactory {
        @Override
        public SqlStatementParameterCustomizer createForParameter(Annotation annotation,
                                                                  Class<?> sqlObjectType,
                                                                  Method method,
                                                                  Parameter param,
                                                                  int index,
                                                                  Type type) {
            Bind b = (Bind) annotation;
            String nameFromAnnotation = b == null ? NO_VALUE : b.value();
            final String name = ParameterUtil.getParameterName(b, nameFromAnnotation, param);

            return (stmt, arg) -> {
                stmt.bindByType(index, arg, type);
                stmt.bindByType(name, arg, type);
            };
        }
    }
}
