package uy.edu.pa.central.persistence;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Delegating DataSource that swallows login timeout operations unsupported by some Tomcat pools.
 */
final class SafeDataSource implements DataSource {
    private final DataSource delegate;

    SafeDataSource(DataSource delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            return delegate.getConnection(username, password);
        } catch (UnsupportedOperationException ex) {
            return delegate.getConnection();
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        try {
            delegate.setLogWriter(out);
        } catch (UnsupportedOperationException ex) {
            // noop for pools that do not support changing the log writer at runtime
        }
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        try {
            delegate.setLoginTimeout(seconds);
        } catch (UnsupportedOperationException ex) {
            // noop for pools that do not support login timeout configuration
        }
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        try {
            return delegate.getLoginTimeout();
        } catch (UnsupportedOperationException ex) {
            return 0;
        }
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}
