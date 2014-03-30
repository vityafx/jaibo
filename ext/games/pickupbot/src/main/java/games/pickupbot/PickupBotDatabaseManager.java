package games.pickupbot;

import org.jaibo.api.database.DatabaseProvider;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * PickupBot Database Manager
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public final class PickupBotDatabaseManager {
    private final ArrayList<PickupBotDatabaseManagerListener> listeners = new ArrayList<PickupBotDatabaseManagerListener>();

    private String databaseFileName = ExtensionObject.Configuration.get("database");

    private String lockedPlayersTableName = "locked_players";
    private String lockedPlayersGameProfileField = "game_profile";
    private String lockedPlayersUnlockDateField = "unlock_datetime_stamp";

    private String gameProfilesTableName = "game_profiles";
    private String gameProfilesHostFieldName = "host";
    private String gameProfilesGameProfileFieldName = "game_profile";

    private String tournamentsTableName = "tournaments";
    private String tournamentsGameProfileFieldName = "game_profile";
    private String tournamentsTournamentFieldName = "tournament";

    public PickupBotDatabaseManager() {
        this.syncTables();
    }

    public PickupBotDatabaseManager(PickupBotDatabaseManagerListener listener) {
        this();

        this.addListener(listener);
    }

    public void syncTables() {
        this.createLockedPlayersTable();
        this.createGameProfilesTable();
        this.createTournamentsTable();
    }

    public void addListener(PickupBotDatabaseManagerListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(PickupBotDatabaseManagerListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    private void notifyListenersProfileChanged(String oldProfile, String newProfile) {
        for (PickupBotDatabaseManagerListener listener : this.listeners) {
            listener.playerProfileChanged(oldProfile, newProfile);
        }
    }

    private void createLockedPlayersTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s integer)",
                this.lockedPlayersTableName, this.lockedPlayersGameProfileField, this.lockedPlayersUnlockDateField);

        try {
            DatabaseProvider.executeStatementWithDatabase(databaseFileName, query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create locked players table: " + e.getMessage());
        }
    }

    private void createGameProfilesTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s text)",
                this.gameProfilesTableName, this.gameProfilesHostFieldName, this.gameProfilesGameProfileFieldName);

        try {
            DatabaseProvider.executeStatementWithDatabase(databaseFileName, query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create game profiles table: " + e.getMessage());
        }
    }

    private void createTournamentsTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s text)",
                this.tournamentsTableName, this.tournamentsGameProfileFieldName, this.tournamentsTournamentFieldName);

        try {
            DatabaseProvider.executeStatementWithDatabase(databaseFileName, query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create tournaments table: " + e.getMessage());
        }
    }

    public boolean isPlayerRegisteredInTournament(String tournament, String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as registeredPlayersCount FROM %s WHERE %s=?",
                    this.tournamentsTableName, this.tournamentsGameProfileFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        return rowSet.getInt("registeredPlayersCount") == 1;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch locked players list: " + e.getMessage());
            }
        }

        return false;
    }

    public void deleteTournament(String tournament) {
        if (tournament != null && !tournament.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=?", this.tournamentsTableName,
                    this.tournamentsTournamentFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, tournament);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to delete tournament (tournament=%s): %s",
                        tournament, e.getMessage()));
            }
        }
    }

    public void removePlayerFromTournament(String tournament, String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=? and %s=?", this.tournamentsTableName,
                    this.tournamentsGameProfileFieldName, this.tournamentsTournamentFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setString(2, tournament);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove player from tournament (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public void addPlayerInTournament(String tournament, String gameProfile) {
        if (tournament != null && !tournament.isEmpty() && gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", this.tournamentsTableName,
                    this.tournamentsGameProfileFieldName, this.tournamentsTournamentFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setString(2, tournament);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add player in tournament (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public String[] getPlayersRegisteredInTournament(String tournament) {
        String[] playersStringList = null;

        if (tournament != null && !tournament.isEmpty()) {
            String query = String.format("SELECT %s as player FROM %s WHERE %s=?", this.tournamentsGameProfileFieldName,
                    this.tournamentsTableName, this.tournamentsTournamentFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, tournament);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    ArrayList<String> playersList = new ArrayList<String>();
                    playersStringList = new String[]{};

                    if (rowSet.next()) {
                        playersList.add(rowSet.getString("player"));
                    }

                    playersStringList = playersList.toArray(playersStringList);
                }
            } catch (SQLException e) {
                System.out.println(String.format("Failed to fetch players list from tournament (tournament=%s): %s",
                        tournament, e.getMessage()));
            }
        }

        return playersStringList;
    }


    public boolean isPlayerLocked(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as lockedPlayersCount FROM %s WHERE %s=?",
                    this.lockedPlayersTableName, this.lockedPlayersGameProfileField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        int lockedPlayersCount = rowSet.getInt("lockedPlayersCount");

                        return lockedPlayersCount == 1;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch locked players list: " + e.getMessage());
            }
        }

        return false;
    }

    public GregorianCalendar getPlayerLockedTime(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty() && this.isPlayerLocked(gameProfile)) {
            String query = String.format("SELECT %s as unlockDateTimeStamp FROM %s WHERE %s=?",
                    this.lockedPlayersUnlockDateField, this.lockedPlayersTableName, this.lockedPlayersGameProfileField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        long unlockedDateTimeStamp = rowSet.getLong("unlockDateTimeStamp");

                        GregorianCalendar unlockDate = new GregorianCalendar();

                        unlockDate.setTimeInMillis(unlockedDateTimeStamp);

                        return unlockDate;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch locked players list: " + e.getMessage());
            }
        }

        return null;
    }

    public void removeLockedPlayer(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=?", this.lockedPlayersTableName,
                    this.lockedPlayersGameProfileField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove locked player (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public void addLockedPlayer(String gameProfile, long timeStampInMillis) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", this.lockedPlayersTableName,
                    this.lockedPlayersGameProfileField, this.lockedPlayersUnlockDateField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setLong(2, timeStampInMillis);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add locked player (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public void addLockedPlayer(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", this.lockedPlayersTableName,
                    this.lockedPlayersGameProfileField, this.lockedPlayersUnlockDateField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setLong(2, 0);                // locked forever

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add locked player (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public boolean isGameProfileExistsForHost(String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as gameProfiles FROM %s WHERE %s=?",
                    this.gameProfilesTableName, this.gameProfilesHostFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, host);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        int gameProfilesCount = rowSet.getInt("gameProfiles");

                        return gameProfilesCount >= 1;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch game profiles list: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean isGameProfileExists(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as gameProfiles FROM %s WHERE %s=?",
                    this.gameProfilesTableName, this.gameProfilesGameProfileFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        int gameProfilesCount = rowSet.getInt("gameProfiles");

                        return gameProfilesCount >= 1;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch game profiles list: " + e.getMessage());
            }
        }

        return false;
    }

    public String getGameProfileForHost(String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("SELECT %s as gameProfile FROM %s WHERE %s=?",
                    this.gameProfilesGameProfileFieldName, this.gameProfilesTableName, this.gameProfilesHostFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, host);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName,
                        preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        return rowSet.getString("gameProfile");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch game profiles list: " + e.getMessage());
            }
        }

        return null;
    }

    public void removeHostBindingForGameProfile(String host, String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=? and %s=?", this.gameProfilesTableName,
                    this.gameProfilesGameProfileFieldName, this.gameProfilesHostFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setString(2, host);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove game profile (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public void removeAllHostBindingsForGameProfiles(String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=?", this.gameProfilesTableName,
                    this.gameProfilesHostFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, host);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove host binding for game profile (host=%s): %s",
                        host, e.getMessage()));
            }
        }
    }

    public void removeGameProfile(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=?", this.gameProfilesTableName,
                    this.gameProfilesGameProfileFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, gameProfile);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove game profile (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }

    public void changeGameProfile(String oldGameProfile, String newGameProfile) {
        if (oldGameProfile != null && !oldGameProfile.isEmpty()
                && newGameProfile != null && !newGameProfile.isEmpty()) {
            String query = String.format("UPDATE %s SET %s=? WHERE %s=?", this.gameProfilesTableName,
                    this.gameProfilesGameProfileFieldName, this.gameProfilesGameProfileFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, newGameProfile);
                preparedStatement.setString(2, oldGameProfile);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);

                this.notifyListenersProfileChanged(oldGameProfile, newGameProfile);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to change game profile (game_profile=' %s ' to ' %s '): %s",
                        oldGameProfile, newGameProfile, e.getMessage()));
            }
        }
    }

    public void addGameProfile(String host, String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", this.gameProfilesTableName,
                    this.gameProfilesHostFieldName, gameProfilesGameProfileFieldName);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatementWithDatabase(databaseFileName,
                    query);

            try {
                preparedStatement.setString(1, host);
                preparedStatement.setString(2, gameProfile);

                DatabaseProvider.executePreparedStatementWithDatabase(databaseFileName, preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add game profile (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }
}