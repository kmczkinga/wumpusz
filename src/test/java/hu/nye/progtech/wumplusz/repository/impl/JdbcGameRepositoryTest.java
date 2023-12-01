package hu.nye.progtech.wumplusz.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.List;

import hu.nye.progtech.wumplusz.model.GameStore;
import hu.nye.progtech.wumplusz.model.UserData;
import hu.nye.progtech.wumplusz.service.throwable.NoNameThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class JdbcGameRepositoryTest {

    private static final String TEST_USERNAME = "testuser";
    private static final Integer WINS = 1;

    private static final String TEST_USERNAME_2 = "testuser2";
    private static final Integer WINS_2 = 2;

    private static final String SUCCESSFUL_SAVE = "Sikeresen elmentetted a játékot!\n";
    private static final String SAVE_FAILED = "Hiba az adatbázisba mentés közben: java.sql.SQLException\n";

    private static final String RUNTIME_EXCEPTION_MESSAGE = "Hiba a beolvasás " +
            "közben: java.sql.SQLException";

    private static final String NO_NAME = "Nincs ilyen elmentett név! Adj meg egy újat\n";
    private JdbcGameRepository underTest;

    private Connection connection;

    private GameStore gameStore;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        connection = Mockito.mock(Connection.class);
        gameStore = Mockito.mock(GameStore.class);
        underTest = new JdbcGameRepository(connection, gameStore);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testSaveShouldDeletePreviousSaveAndSaveMapToJdbcWhenThereIsNoException() throws SQLException {
        // given
        UserData userData = new UserData(TEST_USERNAME, WINS);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcGameRepository.DELETE_STATEMENT)).thenReturn(preparedStatement);
        given(gameStore.getUserData()).willReturn(userData)
                .willReturn(userData)
                .willReturn(userData)
                .willReturn(userData);

        PreparedStatement preparedStatement2 = Mockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcGameRepository.INSERT_STATEMENT))
                .thenReturn(preparedStatement2);

        // when
        underTest.save();

        // then
        assertEquals(SUCCESSFUL_SAVE, outputStreamCaptor.toString());
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.DELETE_STATEMENT);
        Mockito.verify(preparedStatement).setString(1, TEST_USERNAME);
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(preparedStatement).close();
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.INSERT_STATEMENT);
        Mockito.verify(preparedStatement2).setString(1, TEST_USERNAME);
        Mockito.verify(preparedStatement2).setInt(2, WINS);
        Mockito.verify(preparedStatement2).executeUpdate();
        Mockito.verify(preparedStatement2).close();
        Mockito.verifyNoMoreInteractions(connection, preparedStatement, preparedStatement2);
    }

    @Test
    public void testSaveShouldDoNothingWhenThereIsAnSqlException() throws SQLException {
        // Given
        when(connection.prepareStatement(JdbcGameRepository.DELETE_STATEMENT)).thenThrow(new SQLException());

        // When
        underTest.save();

        // Then
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.DELETE_STATEMENT);
        Mockito.verifyNoMoreInteractions(connection);
        assertEquals(SAVE_FAILED, outputStreamCaptor.toString());
    }

    @Test
    public void testLoadShouldLoadMapFromDb() throws SQLException, NoNameThrowable {
        // given
        UserData userData = new UserData(TEST_USERNAME, WINS);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(connection.prepareStatement(JdbcGameRepository.SELECT_STATEMENT)).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        given(gameStore.getUserData()).willReturn(userData);
        given(resultSet.next()).willReturn(true);
        given(resultSet.getString("username")).willReturn(TEST_USERNAME);
        given(resultSet.getInt("wins")).willReturn(WINS);

        // when
        UserData result = underTest.load(TEST_USERNAME);

        // then
        assertEquals(result, userData);
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.SELECT_STATEMENT);
        Mockito.verify(preparedStatement).setString(1, TEST_USERNAME);
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(preparedStatement).getResultSet();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getString("username");
        Mockito.verify(resultSet).getInt("wins");
        Mockito.verify(preparedStatement).close();
        Mockito.verifyNoMoreInteractions(connection, preparedStatement);
    }

    @Test
    public void testLoadShouldThrowRuntimeExceptionWhenThereIsAnSqlException() throws SQLException {
        // Given
        when(connection.prepareStatement(JdbcGameRepository.SELECT_STATEMENT)).thenThrow(new SQLException());

        // When
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> underTest.load(TEST_USERNAME), RUNTIME_EXCEPTION_MESSAGE);

        // Then
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.SELECT_STATEMENT);
        Mockito.verifyNoMoreInteractions(connection);
        assertTrue(runtimeException.getMessage().equals(RUNTIME_EXCEPTION_MESSAGE));
    }

    @Test
    public void testLoadShouldThrowNoNameThrowableWhenThereAreNoNamesInTable() throws SQLException, NoNameThrowable {
        // given
        UserData userData = new UserData(TEST_USERNAME, WINS);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(connection.prepareStatement(JdbcGameRepository.SELECT_STATEMENT)).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        given(gameStore.getUserData()).willReturn(userData);
        given(resultSet.next()).willReturn(false);

        // when
        NoNameThrowable noNameThrowable = assertThrows(NoNameThrowable.class, () -> underTest.load(TEST_USERNAME), "");

        // then
        assertEquals(NO_NAME, outputStreamCaptor.toString());
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.SELECT_STATEMENT);
        Mockito.verify(preparedStatement).setString(1, TEST_USERNAME);
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(preparedStatement).getResultSet();
        Mockito.verify(resultSet).next();
        Mockito.verify(preparedStatement).close();
        Mockito.verifyNoMoreInteractions(connection, preparedStatement);
        assertNull(noNameThrowable.getMessage());
    }

    @Test
    public void testLoadShouldThrowNoNameThrowableWhenThereAreNoNamesInTableWithNullResultSet() throws SQLException, NoNameThrowable {
        // given
        UserData userData = new UserData(TEST_USERNAME, WINS);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(connection.prepareStatement(JdbcGameRepository.SELECT_STATEMENT)).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(null);
        given(gameStore.getUserData()).willReturn(userData);

        // when
        NoNameThrowable noNameThrowable = assertThrows(NoNameThrowable.class, () -> underTest.load(TEST_USERNAME), "");

        // then
        assertEquals(NO_NAME, outputStreamCaptor.toString());
        Mockito.verify(connection).prepareStatement(JdbcGameRepository.SELECT_STATEMENT);
        Mockito.verify(preparedStatement).setString(1, TEST_USERNAME);
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(preparedStatement).getResultSet();
        Mockito.verify(preparedStatement).close();
        Mockito.verifyNoMoreInteractions(connection, preparedStatement);
        assertNull(noNameThrowable.getMessage());
    }

    @Test
    public void testReadAllUsersShouldReturnWithAllUsers() throws SQLException {
        // Given
        List<UserData> userDatas = List.of(
                new UserData(TEST_USERNAME, WINS),
                new UserData(TEST_USERNAME_2, WINS_2)
        );
        Statement statement = Mockito.mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(statement.executeQuery(JdbcGameRepository.SELECT_ALL_STATEMENT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).
                thenReturn(true)
                .thenReturn(false);
        when(resultSet.getString("username"))
                .thenReturn(TEST_USERNAME)
                .thenReturn(TEST_USERNAME_2);
        when(resultSet.getInt("wins"))
                .thenReturn(WINS)
                .thenReturn(WINS_2);

        // When
        List<UserData> result = underTest.readAllUsers();

        // Then
        assertEquals(result, userDatas);
        Mockito.verify(connection).createStatement();
        Mockito.verify(statement).executeQuery(JdbcGameRepository.SELECT_ALL_STATEMENT);
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getString("username");
        Mockito.verify(resultSet, times(2)).getInt("wins");
        Mockito.verifyNoMoreInteractions(connection);

    }

    @Test
    public void testReadAllUsersShouldThrowRuntimeExceptionWhenThereIsAnSqlException() throws SQLException {
        // Given
        when(connection.createStatement()).thenThrow(new SQLException());

        // When
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> underTest.readAllUsers(), RUNTIME_EXCEPTION_MESSAGE);

        // Then
        Mockito.verify(connection).createStatement();
        Mockito.verifyNoMoreInteractions(connection);
        assertTrue(runtimeException.getMessage().equals(RUNTIME_EXCEPTION_MESSAGE));
    }
}
