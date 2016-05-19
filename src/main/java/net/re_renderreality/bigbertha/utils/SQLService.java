package net.re_renderreality.bigbertha.utils;

import java.sql.SQLException;
import java.util.Optional;

import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;

/**
 * This service provides the basics for an abstraction over SQL connections.
 *
 * <p>Implementations of this service are expected to be thread-safe.</p>
 */
@ThreadSafe
public interface SQLService {

    /**
     * Returns a data source for the provided JDBC connection string or an alias
     *
     * <p>A jdbc connection url is expected to be of the form:
     * jdbc:&lt;engine&gt;://[&lt;username&gt;[:&lt;password&gt;]@]&lt;host&gt;/&lt;database&gt;
     * or an alias (available aliases are known only by the service provider)</p>
     *
     * @param jdbcConnection The jdbc url or connection alias
     * @return A data source providing connections to the given URL.
     * @throws SQLException if a connection to the given database could not be established
     */
    DataSource getDataSource(String jdbcConnection) throws SQLException;

    /**
     * Returns a possible connection URL for a given alias.
     *
     * @param alias The alias to check
     * @return The connection url as a String if it exists,
     *          or {@link Optional#empty()}
     */
    Optional<String> getConnectionUrlFromAlias(String alias);
}

