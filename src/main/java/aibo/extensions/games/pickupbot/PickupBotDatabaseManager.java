package aibo.extensions.games.pickupbot;

import database.SQLiteProvider;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private String lockedPlayersTableName = "pickupbot_locked";
    private String lockedPlayersGameProfileField = "game_profile";
    private String lockedPlayersUnlockDateField = "unlock_datetime_stamp";

    private String gameProfilesTableName = "pickupbot_game_profiles";
    private String gameProfilesHostFieldName = "host";
    private String gameProfilesGameProfileFieldName = "game_profile";

    public PickupBotDatabaseManager() {
        this.syncTables();
    }

    public void syncTables() {
        this.createLockedPlayersTable();
        this.createGameProfilesTable();
    }

    private void createLockedPlayersTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s integer)",
                this.lockedPlayersTableName, this.lockedPlayersGameProfileField, this.lockedPlayersUnlockDateField);

        try {
            SQLiteProvider.executeStatement(query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create locked players table: " + e.getMessage());
        }
    }

    private void createGameProfilesTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s text)",
                this.gameProfilesTableName, this.gameProfilesHostFieldName, this.gameProfilesGameProfileFieldName);

        try {
            SQLiteProvider.executeStatement(query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create game profiles table: " + e.getMessage());
        }
    }

    public boolean isPlayerLocked(String gameProfile) {
        if (gameProfile != null && !gameProfile.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as lockedPlayersCount FROM %s WHERE %s=?",
                    this.lockedPlayersTableName, this.lockedPlayersGameProfileField);
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = SQLiteProvider.executePreparedStatement(preparedStatement, true);

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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = SQLiteProvider.executePreparedStatement(preparedStatement, true);

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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setLong(2, timeStampInMillis);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setLong(2, 0);                // locked forever

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);

                CachedRowSet rowSet = SQLiteProvider.executePreparedStatement(preparedStatement, true);

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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);

                CachedRowSet rowSet = SQLiteProvider.executePreparedStatement(preparedStatement, true);

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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);

                CachedRowSet rowSet = SQLiteProvider.executePreparedStatement(preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        String gameProfile = rowSet.getString("gameProfile");

                        return gameProfile;
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);
                preparedStatement.setString(2, host);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, gameProfile);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, newGameProfile);
                preparedStatement.setString(2, oldGameProfile);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
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
            PreparedStatement preparedStatement = SQLiteProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);
                preparedStatement.setString(2, gameProfile);

                SQLiteProvider.executePreparedStatement(preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add game profile (game_profile=%s): %s",
                        gameProfile, e.getMessage()));
            }
        }
    }
}
