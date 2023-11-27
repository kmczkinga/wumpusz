package hu.nye.progtech.wumplusz.repository.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.MapVO;
import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.repository.GameRepository;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;

/**
 * Komponens, amely betölti, elmenti a játékot az adatbázisba.
 */
public class JdbcGameRepository implements GameRepository<UserData> {

    static final String INSERT_STATEMENT = "INSERT INTO GAME_SAVES (username, wins) VALUES (?, ?);";
    static final String DELETE_STATEMENT = "DELETE FROM GAME_SAVES WHERE username = ?;";
    static final String SELECT_STATEMENT = "SELECT * FROM GAME_SAVES WHERE username = ?;";

    static final String SELECT_ALL_STATEMENT = "SELECT * FROM GAME_SAVES";

    private Connection connection;

    private GameStore gameStore;

    public JdbcGameRepository(Connection connection, GameStore gameStore) {
        this.connection = connection;
        this.gameStore = gameStore;
    }

    /**
     * Elmenti az adott játékot.
     */
    @Override
    public void save() {
        try {
            deleteCurrentSave();
            insertNewSave();
        } catch (SQLException e) {
            System.out.println("Hiba az adatbázisba mentés közben: " + e);
        }
        System.out.println("Sikeresen elmentetted a játékot!");
    }

    /**
     * Betölti az adott játékot.
     */
    @Override
    public UserData load(String username) throws NoNameThrowable {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STATEMENT)
            ) {
            preparedStatement.setString(1, gameStore.getUserData().getUsername());
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (null == resultSet || !resultSet.next()) {
                System.out.println("Nincs ilyen elmentett név! Adj meg egy újat");
                throw new NoNameThrowable();
            }

            String loadedUsername = resultSet.getString("username");
            Integer wins = resultSet.getInt("wins");
            return new UserData(loadedUsername, wins);
        } catch (SQLException e) {
            throw new RuntimeException("Hiba a beolvasás közben: " + e);
        }
    }

    public List<UserData> readAllUsers() {
        List<UserData> result = new ArrayList<>();
        try(Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(SELECT_ALL_STATEMENT)) {
            UserData userData;
            while (rs.next()) {
                userData = new UserData(rs.getString("username"), rs.getInt("wins"));
                result.add(userData);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Hiba a beolvasás közben: " + e);
        }
        return result;
    }

    private void deleteCurrentSave() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT)) {
            preparedStatement.setString(1, gameStore.getUserData().getUsername());
            preparedStatement.executeUpdate();
        }
    }

    private void insertNewSave() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT)) {
            preparedStatement.setString(1, gameStore.getUserData().getUsername());
            preparedStatement.setInt(2, gameStore.getUserData().getWins());
            preparedStatement.executeUpdate();
        }
    }
}
