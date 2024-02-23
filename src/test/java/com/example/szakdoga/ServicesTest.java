package com.example.szakdoga;

import com.example.szakdoga.exception.*;
import com.example.szakdoga.model.*;
import com.example.szakdoga.model.dto.ReceiverAllDto;
import com.example.szakdoga.model.request.PlayerRequest;
import com.example.szakdoga.model.request.ScoutRequest;
import com.example.szakdoga.model.request.UpdatePlayerRequest;
import com.example.szakdoga.model.request.UpdateScoutRequest;
import com.example.szakdoga.repository.*;
import com.example.szakdoga.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicesTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private ScoutRepository scoutRepository;
    @Mock
    private PlayerAdRepository playerAdRepository;
    @Mock
    private ScoutAdRepository scoutAdRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private FilesRepository filesRepository;
    @Mock
    private SendMessageRepository sendMessageRepository;

    @InjectMocks
    private SendMessageService messageService;
    @InjectMocks
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private FilesService filesService;

    @InjectMocks
    private PlayerAdService playerAdService;

    @InjectMocks
    private ScoutAdService scoutAdService;

    private static final String USERNAME = "KeleD";
    private static final Integer AD_ID = 1;
    private static final String CONTENT = "Ad content";
    private static final String FILE_NAME = "testFile.jpg";
    private static final String UPLOAD_DIR = "src/adsPhotos/";
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


    @Test
    public void testUpdatePlayerProfile_Successful() throws Exception {
        // Arrange
        UpdatePlayerRequest request = new UpdatePlayerRequest();
        request.setUsername("username");
        request.setLast_name("NewLastName");
        request.setFirst_name("NewFirstName");
        request.setEmail("newemail@example.com");
        request.setLocation("NewLocation");
        request.setSport("NewSport");
        request.setAge(25);
        request.setPosition("NewPosition");

        User user = new User();
        Player player = new Player();
        user.setPlayer(player);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // Act
        User updatedUser = userService.updatePlayerProfile(request);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("NewLastName", user.getPlayer().getLast_name());
        assertEquals("NewFirstName", user.getPlayer().getFirst_name());
        assertEquals("newemail@example.com", user.getPlayer().getEmail());
        assertEquals("NewLocation", user.getPlayer().getLocation());
        assertEquals("NewSport", user.getPlayer().getSport());
        assertEquals(25, user.getPlayer().getAge());
        assertEquals("NewPosition", user.getPlayer().getPosition());
        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdatePlayerProfile_UserNotFound() {
        // Arrange
        UpdatePlayerRequest request = new UpdatePlayerRequest();
        request.setUsername("nonExistentUsername");

        when(userRepository.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.updatePlayerProfile(request));
        verify(userRepository, times(1)).findByUsername("nonExistentUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateScoutProfile_Successful() throws Exception {
        // Arrange
        UpdateScoutRequest request = new UpdateScoutRequest();
        request.setUsername("username");
        request.setLast_name("NewLastName");
        request.setFirst_name("NewFirstName");
        request.setEmail("newemail@example.com");
        request.setSport("NewSport");
        request.setTeam("NewTeam");

        User user = new User();
        Scout scout = new Scout();
        user.setScout(scout);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // Act
        User updatedUser = userService.updateScoutProfile(request);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("NewLastName", user.getScout().getLast_name());
        assertEquals("NewFirstName", user.getScout().getFirst_name());
        assertEquals("newemail@example.com", user.getScout().getEmail());
        assertEquals("NewSport", user.getScout().getSport());
        assertEquals("NewTeam", user.getScout().getTeam());
        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateScoutProfile_UserNotFound() {
        // Arrange
        UpdateScoutRequest request = new UpdateScoutRequest();
        request.setUsername("nonExistentUsername");

        when(userRepository.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.updateScoutProfile(request));
        verify(userRepository, times(1)).findByUsername("nonExistentUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterScout_Successful() {
        // Arrange
        ScoutRequest scoutRequest = new ScoutRequest("TesztJani", "12345678", "tesztj@gmail.com", "SCOUT", "soccer", "Jani", "Teszt", "Real Madrid");
        when(userRepository.findByUsername("TesztJani")).thenReturn(Optional.empty());
        when(scoutRepository.findByEmail("tesztj@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        // Act
        User result = userService.registerScout(scoutRequest);

        // Assert
        assertNotNull(result);
        assertEquals("TesztJani", result.getUsername());
        assertEquals(Roles.SCOUT, result.getRoles());
        verify(userRepository, times(1)).save(any(User.class));
        verify(scoutRepository, times(1)).save(any(Scout.class));
    }

    @Test
    public void testRegisterScout_UsernameExists() {
        // Arrange
        ScoutRequest scoutRequest = new ScoutRequest("Ristvan98", "12345678", "tesztj@gmail.com", "SCOUT", "soccer", "Jani", "Teszt", "Real Madrid");
        when(userRepository.findByUsername("Ristvan98")).thenReturn(Optional.of(new User()));

        // Act & Assert
        UsernameIsExistsException exception = assertThrows(UsernameIsExistsException.class, () -> userService.registerScout(scoutRequest));
        assertEquals("A felhasználó név, már létezik.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(scoutRepository, never()).save(any(Scout.class));
    }

    @Test
    public void testRegisterScout_EmailExists() {
        // Arrange
        ScoutRequest scoutRequest = new ScoutRequest("TesztJani", "12345678", "ristvan98@gmail.com", "soccer", "soccer", "Jani", "Teszt", "Real Madrid");
        when(userRepository.findByUsername("TesztJani")).thenReturn(Optional.empty());
        when(scoutRepository.findByEmail("ristvan98@gmail.com")).thenReturn(Optional.of(new Scout()));

        // Act & Assert
        EmailIsExistsException exception = assertThrows(EmailIsExistsException.class, () -> userService.registerScout(scoutRequest));
        assertEquals("Az email cím, már létezik.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(scoutRepository, never()).save(any(Scout.class));
    }
    @Test
    public void testRegisterPlayer_Successful() {
        // Arrange
        PlayerRequest playerRequest = new PlayerRequest("TesztPlayer", "12345678", "tesztplayer@gmail.com", "keeper", "PLAYER", "soccer", "Player", "Teszt", "Budapest", 25);
        when(userRepository.findByUsername("TesztPlayer")).thenReturn(Optional.empty());
        when(playerRepository.findByEmail("tesztplayer@gmail.com")).thenReturn(Optional.empty());

        // Act
        User result = userService.registerPlayer(playerRequest);

        // Assert
        assertNotNull(result);
        assertEquals("TesztPlayer", result.getUsername());
        assertEquals(Roles.PLAYER, result.getRoles());
        verify(userRepository, times(1)).save(any(User.class));
        verify(playerRepository, times(1)).save(any(Player.class));
    }
    @Test
    public void testRegisterPlayer_UsernameExists() {
        // Arrange
        PlayerRequest playerRequest = new PlayerRequest("KeleD", "12345678", "tesztplayer@gmail.com", "keeper", "PLAYER", "soccer", "Player", "Teszt", "Budapest", 25);
        when(userRepository.findByUsername("KeleD")).thenReturn(Optional.of(new User()));

        // Act & Assert
        UsernameIsExistsException exception = assertThrows(UsernameIsExistsException.class, () -> userService.registerPlayer(playerRequest));
        assertEquals("A felhasználó név, már létezik.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testRegisterPlayer_EmailExists() {
        // Arrange
        PlayerRequest playerRequest = new PlayerRequest("TesztPlayer", "12345678", "k.dominik2000@gmail.com", "keeper", "PLAYER", "soccer", "Player", "Teszt", "Budapest", 25);
        when(userRepository.findByUsername("TesztPlayer")).thenReturn(Optional.empty());
        when(playerRepository.findByEmail("k.dominik2000@gmail.com")).thenReturn(Optional.of(new Player()));

        // Act & Assert
        EmailIsExistsException exception = assertThrows(EmailIsExistsException.class, () -> userService.registerPlayer(playerRequest));
        assertEquals("Az email cím, már létezik.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testLoadUserByUsername_UserFound_Success() {
        // Arrange
        String username = "KeleD";
        User user = new User();
        user.setUsername(username);
        user.setPassword("12345678");
        user.setRoles(Roles.PLAYER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("12345678", userDetails.getPassword()); // Ez az érték csak példa, valódi alkalmazásban a jelszó titkosítva lenne
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Roles.PLAYER.name())));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound_ExceptionThrown() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
        assertEquals("A felhasználó nem található.", exception.getMessage());
    }

    /*
    @Test
    public void testHandleFileUpload_Successful() throws Exception {
        // Arrange
        String type = "profilpic";
        String format = "jpg";
        String username = "KeleD";
        String fileName = "IMG-0898.jpg";
        byte[] fileContent = Files.readAllBytes(Paths.get("src/profilePic/IMG-0898.jpg"));
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpg", fileContent);
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        File result = filesService.handleFileUpload(type, format, username, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(format, result.getFormat());
        assertEquals(type, result.getType());
        assertEquals(username, result.getUser().getUsername());

        // Verify file is saved
        verify(filesRepository, times(1)).save(any(File.class));

        // Verify file is written to disk
        Path filePath = Paths.get("src/profilePic/" + fileName);
        assertTrue(Files.exists(filePath));
        assertArrayEquals(fileContent, Files.readAllBytes(filePath));
    }*/

    /*
    @Test
    public void testHandleVideoFile_Successful() throws Exception {
        // Arrange
        String type = "video";
        String format = "mp4";
        String username = "KeleD";
        String fileName = "video.mp4";
        byte[] fileContent = "Test video content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "video/mp4", fileContent);
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        File result = filesService.handleVideoFile(type, format, username, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(format, result.getFormat());
        assertEquals(type, result.getType());
        assertEquals(username, result.getUser().getUsername());

        // Verify file is saved
        verify(filesRepository, times(1)).save(any(File.class));

        // Verify file is written to disk
        Path filePath = Paths.get("src/video/" + fileName);
        assertTrue(Files.exists(filePath));
        assertArrayEquals(fileContent, Files.readAllBytes(filePath));
    }*/

    @Test
    public void testHandleVideoFile_EmptyFile_ExceptionThrown() {
        // Arrange
        String type = "video";
        String format = "mp4";
        String username = "testUser";
        MockMultipartFile emptyFile = new MockMultipartFile("empty.mp4", new byte[0]);

        // Act & Assert
        FileUploadException exception = assertThrows(FileUploadException.class,
                () -> filesService.handleVideoFile(type, format, username, emptyFile));
        assertEquals("Üres maradt a videó feltöltésének helye.", exception.getMessage());
    }

    @Test
    public void testHandleFileUpload_EmptyFile_ExceptionThrown() {
        // Arrange
        String type = "type";
        String format = "jpg"; // Módosítottuk a formátumot JPG-re
        String username = "KeleD";
        MultipartFile emptyFile = new MockMultipartFile("empty.jpg", new byte[0]);

        // Act & Assert
        FileUploadException exception = assertThrows(FileUploadException.class,
                () -> filesService.handleFileUpload(type, format, username, emptyFile));
        assertEquals("Üresen maradt a fájl helye.", exception.getMessage());
    }

    /*
    @Test
    public void testHandlePdfUpload_Successful() throws Exception {
        // Arrange
        String type = "pdf";
        String format = "pdf";
        String username = "testUser";
        String fileName = "test.pdf";
        byte[] fileContent = "Test PDF content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "application/pdf", fileContent);
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        File result = filesService.handlePdfUpload(type, format, username, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(format, result.getFormat());
        assertEquals(type, result.getType());
        assertEquals(username, result.getUser().getUsername());

        // Verify file is saved
        verify(filesRepository, times(1)).save(any(File.class));

        // Verify file is written to disk
        Path filePath = Paths.get("src/pdf/" + fileName);
        assertTrue(Files.exists(filePath));
        assertArrayEquals(fileContent, Files.readAllBytes(filePath));
    }*/

    @Test
    public void testHandlePdfUpload_EmptyFile_ExceptionThrown() {
        // Arrange
        String type = "pdf";
        String format = "pdf";
        String username = "testUser";
        MockMultipartFile emptyFile = new MockMultipartFile("empty.pdf", new byte[0]);

        // Act & Assert
        FileUploadException exception = assertThrows(FileUploadException.class,
                () -> filesService.handlePdfUpload(type, format, username, emptyFile));
        assertEquals("Üresen maradt a pdf feltöltésének helye.", exception.getMessage());
    }

    /*
    @Test
    public void testDownloadPdf_Successful() throws IOException {
        // Arrange
        Long fileId = 1L;
        String fileName = "Kele Dominik CV.pdf";
        byte[] fileContent = "Test PDF content".getBytes();
        Path filePath = Paths.get("src/pdf/" + fileName);

        File file = new File();
        file.setFiles_id(Math.toIntExact(fileId));
        file.setFile_path(filePath.toString());

        when(filesRepository.findById(fileId)).thenReturn(Optional.of(file));
        when(Files.readAllBytes(filePath)).thenReturn(fileContent);

        // Act
        byte[] result = filesService.downloadPdf(fileId);

        // Assert
        assertNotNull(result);
        assertArrayEquals(fileContent, result);
    }*/

    @Test
    public void testDownloadPdf_FileNotFound_ExceptionThrown() {
        // Arrange
        Long fileId = 1L;

        when(filesRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        FileDownloadException exception = assertThrows(FileDownloadException.class,
                () -> filesService.downloadPdf(fileId));
        assertEquals("A fájl nem található", exception.getMessage());
    }

    /*
    @Test
    public void testDownloadVideo_Successful() throws IOException {
        // Arrange
        Long fileId = 1L;
        String fileName = "2min.mp4";
        byte[] fileContent = "Test video content".getBytes();
        Path filePath = Paths.get("src/video/" + fileName);

        File file = new File();
        file.setFiles_id(Math.toIntExact(fileId));
        file.setFile_path(filePath.toString());

        when(filesRepository.findById(fileId)).thenReturn(Optional.of(file));
        when(Files.readAllBytes(filePath)).thenReturn(fileContent);

        // Act
        byte[] result = filesService.downloadVideo(fileId);

        // Assert
        assertNotNull(result);
        assertArrayEquals(fileContent, result);
    }*/

    @Test
    public void testDownloadVideo_FileNotFound() {
        // Arrange
        Long fileId = 1L;

        when(filesRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileDownloadException.class, () -> filesService.downloadVideo(fileId));
    }

    /*
    @Test
    public void testGetProfilePic_ProfilePicExists() throws IOException {
        // Arrange
        String username = "KeleD";
        String filePath = "src/profilePic/IMG-0898.jpg";
        byte[] fileContent = "Test image content".getBytes();

        File file = new File();
        file.setFile_path(filePath);
        file.setType("profilpic");

        List<File> fileList = new ArrayList<>();
        fileList.add(file);

        User user = new User();
        user.setUsername(username);
        user.setFiles(fileList);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(Files.exists(Paths.get(filePath))).thenReturn(true);
        when(Files.readAllBytes(Paths.get(filePath))).thenReturn(fileContent);

        // Act
        byte[] result = filesService.getProfilePic(username);

        // Assert
        assertNotNull(result);
        assertArrayEquals(fileContent, result);
    }*/

    @Test
    public void testGetProfilePic_ProfilePicNotExists() {
        // Arrange
        String username = "testUser";

        File file = new File();
        file.setType("notprofilpic");

        List<File> fileList = new ArrayList<>();
        fileList.add(file);

        User user = new User();
        user.setUsername(username);
        user.setFiles(fileList);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        byte[] result = filesService.getProfilePic(username);

        // Assert
        assertNull(result);
    }

    @Test
    public void testUploadAds_Successful() throws Exception {
        // Arrange
        byte[] fileContent = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, "image/jpeg", fileContent);

        User user = new User();
        user.setId(1);

        Player player = new Player();
        player.setUser(user);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(playerRepository.findByUserId(user.getId())).thenReturn(Optional.of(player));

        // Act
        PlayerAd result = playerAdService.uploadAds(USERNAME, CONTENT, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(CONTENT, result.getContent());
        assertEquals(player, result.getPlayer());

        verify(playerAdRepository, times(1)).save(any(PlayerAd.class));

        Path filePath = Paths.get(UPLOAD_DIR + FILE_NAME);
        assertTrue(Files.exists(filePath));
    }
    /*
    @Test
    public void testUpdateAd_Successful(){
        // Arrange
        byte[] fileContent = "Updated file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, "image/jpeg", fileContent);

        PlayerAd existingAd = new PlayerAd();
        existingAd.setPlayerad_id(AD_ID);
        existingAd.setContent("Initial content");
        existingAd.setPhoto_path("src/uploads/initial.jpg");

        when(playerAdRepository.findById(AD_ID)).thenReturn(Optional.of(existingAd));

        // Act
        PlayerAd result = playerAdService.updateAd(AD_ID, CONTENT, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals(CONTENT, result.getContent());
        assertEquals("src/uploads/" + FILE_NAME, result.getPhoto_path());

        // Verify ad is saved
        verify(playerAdRepository, times(1)).save(existingAd);

        // Verify file is written to disk
        Path filePath = Paths.get(UPLOAD_DIR + FILE_NAME);
        assertTrue(Files.exists(filePath));
    }

    @Test()
    public void testUpdateAd_AdNotFound_ExceptionThrown() {
        // Arrange
        when(playerAdRepository.findById(AD_ID)).thenReturn(Optional.empty());

        // Act
        playerAdService.updateAd(AD_ID, CONTENT, null);
    }*/

    @Test
    public void testGetAllPlayerAds() {
        // Arrange
        List<PlayerAd> playerAds = new ArrayList<>();
        playerAds.add(new PlayerAd());
        playerAds.add(new PlayerAd());

        when(playerAdRepository.findAll()).thenReturn(playerAds);

        // Act
        List<PlayerAd> result = playerAdService.getAllPlayerAds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllScoutAds() {
        // Arrange
        List<ScoutAd> scoutAds = new ArrayList<>();
        scoutAds.add(new ScoutAd());
        scoutAds.add(new ScoutAd());

        when(scoutAdRepository.findAll()).thenReturn(scoutAds);

        // Act
        List<ScoutAd> result = scoutAdService.getAllScoutAds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllMessagesForReceiver() {
        // Arrange
        Integer receiverId = 1;
        SendMessage message1 = new SendMessage();
        message1.setMessage_id(1);
        message1.setMessage_content("Hello");
        message1.setTimestamp(LocalDateTime.now());

        SendMessage message2 = new SendMessage();
        message2.setMessage_id(2);
        message2.setMessage_content("Hi");
        message2.setTimestamp(LocalDateTime.now());

        // Inicializálj egy érvényes feladó felhasználót minden üzenethez
        User senderUser1 = new User();
        senderUser1.setId(101); // Példa id
        message1.setSenderUser(senderUser1);

        User senderUser2 = new User();
        senderUser2.setId(102); // Példa id
        message2.setSenderUser(senderUser2);

        List<SendMessage> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);

        when(sendMessageRepository.findAllLatestIndividualMessagesByReceiverUserId(receiverId)).thenReturn(messages);

        // Act
        List<ReceiverAllDto> result = messageService.getAllMessagesForReceiver(receiverId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0).getMessage_content());
        assertEquals("Hi", result.get(1).getMessage_content());
    }

    @Test
    public void testFormatTimestamp_Successful() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2022, 2, 22, 10, 30, 0); // Példa időbélyeg létrehozása
        String expectedFormattedTimestamp = "2022-02-22T10:30:00"; // Várt formázott időbélyeg

        // Act
        SendMessageService myService = new SendMessageService();
        String formattedTimestamp = myService.formatTimestamp(timestamp);

        // Assert
        assertEquals(expectedFormattedTimestamp, formattedTimestamp);
    }
}
