package me.emiljimenez21.virtualshop.database;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.debug.Debugger;

import java.sql.*;

public abstract class DatabaseAdapter {
    /**
     * The established connection, or null if none
     */
    protected Connection connection;

    // --------------------------------------------------------------------
    // Connecting
    // --------------------------------------------------------------------

    public void reconnect() {}

    /**
     * Attempts to connect using last known credentials. Fails gracefully if those are not provided
     * i.e. connect function was never called
     */
    private final void connectUsingLastCredentials() {
        reconnect();
    }

    // --------------------------------------------------------------------
    // Disconnecting
    // --------------------------------------------------------------------

    /**
     * Attempts to close the connection, if not null
     */
    public final void close() {
        if (connection != null)
            synchronized (connection) {
                try {
                    connection.close();

                } catch (final SQLException e) {
                    Common.error(e, "Error closing connection!");
                }
            }
    }

    // --------------------------------------------------------------------
    // Querying
    // --------------------------------------------------------------------

    /**
     * Attempts to execute a new update query
     *
     * Make sure you called connect() first otherwise an error will be thrown
     *
     * @param sql
     */
    protected final void update(String sql) {
        checkEstablished();

        synchronized (connection) {
            if (!isConnected())
                connectUsingLastCredentials();

            Valid.checkBoolean(!sql.contains("{table}"), "Table not set! Either use connect() method that specifies it or call addVariable(table, 'yourtablename') in your constructor!");

            Debugger.debug("mysql", "Updating MySQL with: " + sql);

            try {
                final Statement statement = connection.createStatement();

                statement.executeUpdate(sql);
                statement.close();

            } catch (final SQLException e) {
                Common.error(e, "Error on updating MySQL with: " + sql);
            }
        }
    }

    /**
     * Attempts to execute a new query
     *
     * Make sure you called connect() first otherwise an error will be thrown
     *
     * @param sql
     * @return
     */
    protected final ResultSet query(String sql) {
        checkEstablished();

        synchronized (connection) {
            if (!isConnected())
                connectUsingLastCredentials();

            Debugger.debug("mysql", "Querying MySQL with: " + sql);

            try {
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery(sql);

                return resultSet;

            } catch (final SQLException e) {
                Common.error(e, "Error on querying MySQL with: " + sql);
            }
        }

        return null;
    }

    /**
     * Attempts to return a prepared statement
     *
     * Make sure you called connect() first otherwise an error will be thrown
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    protected final java.sql.PreparedStatement prepareStatement(String sql) throws SQLException {
        checkEstablished();

        synchronized (connection) {
            if (!isConnected())
                connectUsingLastCredentials();

            Debugger.debug("mysql", "Preparing statement: " + sql);

            return connection.prepareStatement(sql);
        }
    }

    /**
     * Is the connection established, open and valid?
     * Performs a blocking ping request to the database
     *
     * @return whether the connection driver was set
     */
    protected final boolean isConnected() {
        if (!isLoaded())
            return false;

        synchronized (connection) {
            try {
                return connection != null && !connection.isClosed() && connection.isValid(0);

            } catch (final SQLException ex) {
                return false;
            }
        }
    }

    // --------------------------------------------------------------------
    // Non-blocking checking
    // --------------------------------------------------------------------

    /**
     * Checks if the connect() function was called
     */
    private final void checkEstablished() {
        Valid.checkBoolean(isLoaded(), "Connection was never established");
    }

    /**
     * Return true if the connect function was called so that the driver was loaded
     *
     * @return
     */
    public final boolean isLoaded() {
        return connection != null;
    }
}
