package com.example.szakdoga;

import com.example.szakdoga.exception.EmailIsExistsException;
import com.example.szakdoga.exception.PlayerSearchNotFoundException;
import com.example.szakdoga.exception.UsernameIsExistsException;
import com.example.szakdoga.model.*;
import com.example.szakdoga.model.request.ScoutRequest;
import com.example.szakdoga.repository.*;
import com.example.szakdoga.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ScoutRepository scoutRepository;

    @Mock
    private PlayerAdRepository playerAdRepository;

    @Mock
    private FilesRepository filesRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void testGetUserByUsername_ExistingUser() {
        // Arrange
        String username = "KeleD";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void testGetUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.getUserByUsername(username)
        );
        assertEquals("Felhasználó nem található: " + username, exception.getMessage());
    }

    @Test
    public void testGetAllPlayersWithUsernames() {
        // Arrange
        List<PlayerInfo> playerInfos = new ArrayList<>();
        Player player = new Player();
        Player player2 = new Player();
        playerInfos.add(new PlayerInfo(player, "KeleD"));
        playerInfos.add(new PlayerInfo(player2, "Rudy69"));
        when(playerRepository.findAllPlayersWithUsernames()).thenReturn(playerInfos);

        // Act
        List<PlayerInfo> result = userService.getAllPlayersWithUsernames();

        // Assert
        assertEquals(2, result.size());
        assertEquals("KeleD", result.get(0).getUsername());
        assertEquals(player, result.get(0).getPlayer());
        assertEquals("Rudy69", result.get(1).getUsername());
        assertEquals(player2, result.get(1).getPlayer());
    }

    @Test
    public void testGetAllScoutsWithUsernames() {
        // Arrange
        List<ScoutInfo> scoutInfos = new ArrayList<>();
        Scout scout = new Scout();
        Scout scout2 = new Scout();
        scoutInfos.add(new ScoutInfo(scout, "Ristvan98"));
        scoutInfos.add(new ScoutInfo(scout2, "Hajas66"));
        when(scoutRepository.findAllScoutsWithUsernames()).thenReturn(scoutInfos);

        // Act
        List<ScoutInfo> result = userService.getAllScoutsWithUsernames();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ristvan98", result.get(0).getUsername());
        assertEquals(scout, result.get(0).getScout());
        assertEquals("Hajas66", result.get(1).getUsername());
        assertEquals(scout2, result.get(1).getScout());
    }

    @Test
    public void testFindPlayersBySearchTerm_Successful() {
        // Arrange
        String searchTerm = "searchTerm";
        List<Player> players = new ArrayList<>();
        players.add(new Player());
        when(playerRepository.findByMultipleFields(searchTerm)).thenReturn(players);

        // Act
        List<Player> result = userService.findPlayersBySearchTerm(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(players.size(), result.size());
        verify(playerRepository, times(1)).findByMultipleFields(searchTerm);
    }

    @Test
    public void testFindPlayersBySearchTerm_NoPlayersFound() {
        // Arrange
        String searchTerm = "searchTerm";
        when(playerRepository.findByMultipleFields(searchTerm)).thenReturn(new ArrayList<>());

        // Act & Assert
        PlayerSearchNotFoundException exception = assertThrows(
                PlayerSearchNotFoundException.class,
                () -> userService.findPlayersBySearchTerm(searchTerm)
        );
        assertEquals("Nincsen találat erre a keresésre. " + searchTerm, exception.getMessage());
        verify(playerRepository, times(1)).findByMultipleFields(searchTerm);
    }

    @Test
    public void testFindScoutsBySearchTerm_Successful() {
        // Arrange
        String searchTerm = "searchTerm";
        List<Scout> scouts = new ArrayList<>();
        scouts.add(new Scout());
        when(scoutRepository.findByMultipleFields(searchTerm)).thenReturn(scouts);

        // Act
        List<Scout> result = userService.findScoutsBySearchTerm(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(scouts.size(), result.size());
        verify(scoutRepository, times(1)).findByMultipleFields(searchTerm);
    }

    @Test
    public void testFindScoutsBySearchTerm_NoScoutsFound() {
        // Arrange
        String searchTerm = "searchTerm";
        when(scoutRepository.findByMultipleFields(searchTerm)).thenReturn(new ArrayList<>());

        // Act & Assert
        PlayerSearchNotFoundException exception = assertThrows(
                PlayerSearchNotFoundException.class,
                () -> userService.findScoutsBySearchTerm(searchTerm)
        );
        assertEquals("Nincsen találat erre a keresésre. " + searchTerm, exception.getMessage());
        verify(scoutRepository, times(1)).findByMultipleFields(searchTerm);
    }

    private List<User> connectedUsers;

    @BeforeEach
    void setUp() {
        connectedUsers = new ArrayList<>();
    }

    @Test
    public void testConnectUser_Successful() throws Exception {
        // Arrange
        String username = "Ristvan98";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        userService.connectUser(username);

        // Assert
        assertTrue(userService.getConnectedUsers().contains(user));
    }

    @Test
    public void testConnectUser_UserNotFound() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> userService.connectUser(username));
        assertEquals("Felhasználó nem található", exception.getMessage());
        assertTrue(connectedUsers.isEmpty());
    }

    /*@Test
    public void testGetConnectedUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        connectedUsers.add(user1);
        connectedUsers.add(user2);

        // Act
        List<User> result = userService.getConnectedUsers();

        // Assert
        assertEquals(connectedUsers.size(), result.size());
        assertEquals(connectedUsers, result);
    }*/

    /*@Test
    public void testCalculateAverageAdCount() {
        // Arrange
        double expectedAverage = 5.0;
        Object[] averageArray = { expectedAverage };
        List<Object[]> resultList = new ArrayList<>();
        resultList.add(averageArray);
        when(playerRepository.calculateAverageAdCount()).thenReturn(resultList);

        // Act
        Object average = userService.calculateAverageAdCount();

        // Assert
        assertEquals(expectedAverage, ((Object[]) average)[0]);
    }*/

    @Test
    public void testGetAveragePercentageBySport() {
        // Arrange
        List<Object[]> expectedList = new ArrayList<>();
        when(playerRepository.calculateAveragePercentageBySport()).thenReturn(expectedList);

        // Act
        List<Object[]> result = userService.getAveragePercentageBySport();

        // Assert
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetAveragePercentageBySportScout() {
        // Arrange
        List<Object[]> expectedList = new ArrayList<>();
        when(scoutRepository.calculateAveragePercentageBySport()).thenReturn(expectedList);

        // Act
        List<Object[]> result = userService.getAveragePercentageBySportScout();

        // Assert
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetTopPlayerBySport() {
        // Arrange
        List<Object[]> expectedList = new ArrayList<>();
        when(playerRepository.getTopPlayerBySport()).thenReturn(expectedList);

        // Act
        List<Object[]> result = userService.getTopPlayerBySport();

        // Assert
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetTopScoutBySport() {
        // Arrange
        List<Object[]> expectedList = new ArrayList<>();
        when(scoutRepository.getTopScoutBySport()).thenReturn(expectedList);

        // Act
        List<Object[]> result = userService.getTopScoutBySport();

        // Assert
        assertEquals(expectedList, result);
    }

}
