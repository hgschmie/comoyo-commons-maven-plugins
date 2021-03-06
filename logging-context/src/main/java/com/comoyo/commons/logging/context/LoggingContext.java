/**
 * Copyright (C) 2014 Telenor Digital AS
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

package com.comoyo.commons.logging.context;

import java.io.Closeable;
import java.util.Map;

/**
 * A class for maintaining per-thread logging context/application
 * state.  This state can be used to amend log messages with
 * structured information about the entities involved in the operation
 * triggering the log statement.  Similar to
 * <a href="https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/MDC.html"><code>org.apache.log4j.MDC</code></a> (log4j 1.2),
 * <a href="https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/ThreadContext.html"><code>org.apache.logging.log4j.ThreadContext</code></a> (log4j 2.x) and
 * <a href="http://www.slf4j.org/api/org/slf4j/MDC.html"><code>org.slf4j.MDC</code></a> (slf4j)
 * while being logging framework agnostic.  ({@link LoggingContext}
 * can be set to use different underpinning context store frameworks.
 * It is intended to facilitate adapters for other MDC frameworks,
 * although such adapters are not implemented at the time of writing.)
 * </p>
 * {@link LoggingContext} Leverages try-with-resources to enable
 * automatic unwinding of state information. Usage example: <pre>
 *
 * try (final LoggingContext.Scope context = LoggingContext.openContext()) {
 *     context.addField("key", "value");
 *     ...
 * }
 * </pre>
 */
public class LoggingContext
{
    public interface Scope
        extends Closeable
    {
        /**
         * Set a [key, value] pair for this context.  If the context was
         * obtained using {@link #currentContext} when no opened context
         * was in scope, does nothing.  If either key or value is null,
         * does nothing.
         */
        void addField(final String key, final String value);

        @Override
        void close();
    }

    private static LoggingContextFactory factory = new BasicLoggingContextFactory();

    private LoggingContext()
    {
    }

    /**
     * Get the actual {@link LoggingContextFactory} currently in use.
     *
     * @return current factory
     */
    public static LoggingContextFactory getFactory()
    {
        return factory;
    }

    /**
     * Set the {@link LoggingContextFactory} to use.
     *
     * @param newFactory logging context factory to use.
     */
    public static void setFactory(final LoggingContextFactory newFactory)
    {
        factory = newFactory;
    }

    /**
     * Create a new logging context.  If a context is already
     * established, inherits field values from existing context.
     * Fields added inside the scope if this context instance will be
     * abandoned when it is closed.
     *
     * @return logging context scope
     */
    public static Scope openContext()
    {
        return factory.openContext();
    }

    /**
     * Access existing logging context.  Updates to the context will
     * be kept when this context instance is closed.
     *
     * @return logging context scope
     */
    public static Scope currentContext()
    {
        return factory.currentContext();
    }

    /**
     * Get the populated fields for the current context.  If no
     * context has been established using {@link #openContext},
     * returns {@code null}.
     * @return map of [key, value] pairs for the current context.
     */
    public static Map<String, String> getContext()
    {
        return factory.getContext();
    }

    /**
     * Get the populated fields for the last opened context.  If no
     * context has been established using {@link #openContext},
     * returns {@code null}.  This differs from {@link #getContext} in
     * that it may return context for a scope that has been abandoned.
     * This function is primarily useful when logging an event at a
     * different level then where it occurred, e.g an exception.
     *
     * @return map of [key, value] pairs for the last entered context.
     */
    public static Map<String, String> getLastEnteredContext()
    {
        return factory.getLastEnteredContext();
    }
}
