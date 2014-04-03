/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.jdbc;

import com.jcabi.aspects.Loggable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Outcome of ResultSet.
 *
 * <p>The following convenience implementations are provided:
 * <ul>
 *  <li>{@link Outcome#NOT_EMPTY} to check that at least one result row is
 *      returned.
 *  <li>{@link Outcome#VOID} for when you wish to disregard the result.
 *  <li>{@link Outcome#UPDATE_COUNT} to check the number of updated rows.
 * </ul>
 * @param <T> Type of expected result
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 */
public interface Outcome<T> {

    /**
     * Returns {@code TRUE} if at least one SQL record found in
     * {@link ResultSet}.
     *
     * <p>The outcome returns the value of {@link ResultSet#next()} and throws
     * {@link SQLException} in case of a problem.
     */
    Outcome<Boolean> NOT_EMPTY = new Outcome<Boolean>() {
        @Override
        @Loggable(Loggable.DEBUG)
        public Boolean handle(final ResultSet rset, final Statement stmt)
            throws SQLException {
            return rset.next();
        }
    };

    /**
     * Outcome that does nothing (and always returns {@code null}).
     *
     * <p>Useful when you're not interested in the result:
     *
     * <pre> new JdbcSession(source)
     *   .sql("INSERT INTO foo (name) VALUES (?)")
     *   .set("Jeff Lebowski")
     *   .insert(Outcome.VOID);</pre>
     */
    Outcome<Void> VOID = new Outcome<Void>() {
        @Override
        @Loggable(Loggable.DEBUG)
        public Void handle(final ResultSet rset, final Statement stmt) {
            return null;
        }
    };

    /**
     * Outcome that returns the number of updated rows.
     *
     * <p>Use it when you need to determine the number of rows updated:
     *
     * <pre> Integer count = new JdbcSession(source)
     *   .sql("UPDATE employee SET salary = 35000 WHERE department = ?")
     *   .set("Finance")
     *   .update(UpdateCountOutcome.INSTANCE);</pre>
     */
    Outcome<Integer> UPDATE_COUNT = new Outcome<Integer>() {
        @Override
        @Loggable(Loggable.DEBUG)
        public Integer handle(final ResultSet rset, final Statement stmt)
            throws SQLException {
            return stmt.getUpdateCount();
        }
    };

    /**
     * Process the result set and return some value.
     * @param rset The result set to process
     * @param stmt The statement used in the run
     * @return The result
     * @throws SQLException If something goes wrong inside
     */
    T handle(ResultSet rset, Statement stmt) throws SQLException;
}
