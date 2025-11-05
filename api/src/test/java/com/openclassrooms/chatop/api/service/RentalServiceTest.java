package com.openclassrooms.chatop.api.service;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.mapper.RentalMapper;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.implementations.RentalServiceImpl;
import com.openclassrooms.chatop.api.service.interfaces.IFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for RentalServiceImpl.
 * Tests business logic and data persistence operations.
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IFileStorageService fileStorageService;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private User testUser;
    private Rental testRental;
    private MultipartFile testImageFile;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("encoded_password");

        // Setup test rental
        testRental = new Rental();
        testRental.setId(1L);
        testRental.setName("Charming seaside apartment");
        testRental.setSurface(BigDecimal.valueOf(65.50));
        testRental.setPrice(BigDecimal.valueOf(150.00));
        testRental.setPicture("http://localhost:3001/api/uploads/test.jpg");
        testRental.setDescription("Beautiful apartment with ocean view");
        testRental.setOwner(testUser);
        testRental.setCreatedAt(LocalDateTime.now());
        testRental.setUpdatedAt(LocalDateTime.now());

        // Setup test image file
        testImageFile = new MockMultipartFile(
                "picture",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // Setup UserDetails mock - will be configured per test as needed
        lenient().when(userDetails.getUsername()).thenReturn("test@example.com");

        // Setup RentalMapper mock behaviors
        lenient().when(rentalMapper.toDto(any(Rental.class))).thenAnswer(invocation -> {
            Rental rental = invocation.getArgument(0);
            return new RentalDTO(
                    rental.getId(),
                    rental.getName(),
                    rental.getSurface(),
                    rental.getPrice(),
                    rental.getPicture(),
                    rental.getDescription(),
                    rental.getOwner() != null ? rental.getOwner().getId() : null,
                    rental.getCreatedAt(),
                    rental.getUpdatedAt()
            );
        });

        lenient().when(rentalMapper.toDtoList(any(List.class))).thenAnswer(invocation -> {
            List<Rental> rentals = invocation.getArgument(0);
            return rentals.stream()
                    .map(rental -> new RentalDTO(
                            rental.getId(),
                            rental.getName(),
                            rental.getSurface(),
                            rental.getPrice(),
                            rental.getPicture(),
                            rental.getDescription(),
                            rental.getOwner() != null ? rental.getOwner().getId() : null,
                            rental.getCreatedAt(),
                            rental.getUpdatedAt()
                    ))
                    .toList();
        });

        lenient().when(rentalMapper.toEntity(any(CreateRentalRequest.class))).thenAnswer(invocation -> {
            CreateRentalRequest request = invocation.getArgument(0);
            Rental rental = new Rental();
            rental.setName(request.name());
            rental.setSurface(request.surface());
            rental.setPrice(request.price());
            rental.setDescription(request.description());
            return rental;
        });

        lenient().doAnswer(invocation -> {
            UpdateRentalRequest request = invocation.getArgument(0);
            Rental rental = invocation.getArgument(1);
            if (request.name() != null) {
                rental.setName(request.name());
            }
            if (request.surface() != null) {
                rental.setSurface(request.surface());
            }
            if (request.price() != null) {
                rental.setPrice(request.price());
            }
            if (request.description() != null) {
                rental.setDescription(request.description());
            }
            return null;
        }).when(rentalMapper).updateFromRequest(any(UpdateRentalRequest.class), any(Rental.class));
    }

    @Nested
    @DisplayName("getAllRentals()")
    class GetAllRentals {

        @Test
        @DisplayName("Should return list of all rentals")
        void shouldReturnAllRentals() {
            // Given
            Rental rental2 = new Rental();
            rental2.setId(2L);
            rental2.setName("Mountain chalet");
            rental2.setSurface(BigDecimal.valueOf(80.00));
            rental2.setPrice(BigDecimal.valueOf(200.00));
            rental2.setOwner(testUser);

            when(rentalRepository.findAllWithOwner()).thenReturn(Arrays.asList(testRental, rental2));

            // When
            List<RentalDTO> result = rentalService.getAllRentals();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("Charming seaside apartment");
            assertThat(result.get(1).name()).isEqualTo("Mountain chalet");
            verify(rentalRepository, times(1)).findAllWithOwner();
        }

        @Test
        @DisplayName("Should return empty list when no rentals exist")
        void shouldReturnEmptyListWhenNoRentals() {
            // Given
            when(rentalRepository.findAllWithOwner()).thenReturn(List.of());

            // When
            List<RentalDTO> result = rentalService.getAllRentals();

            // Then
            assertThat(result).isEmpty();
            verify(rentalRepository, times(1)).findAllWithOwner();
        }
    }

    @Nested
    @DisplayName("getRentalById()")
    class GetRentalById {

        @Test
        @DisplayName("Should return rental when found")
        void shouldReturnRentalWhenFound() {
            // Given
            when(rentalRepository.findByIdWithOwner(1L)).thenReturn(Optional.of(testRental));

            // When
            Optional<RentalDTO> result = rentalService.getRentalById(1L);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(1L);
            assertThat(result.get().name()).isEqualTo("Charming seaside apartment");
            verify(rentalRepository, times(1)).findByIdWithOwner(1L);
        }

        @Test
        @DisplayName("Should return empty Optional when rental not found")
        void shouldReturnEmptyWhenNotFound() {
            // Given
            when(rentalRepository.findByIdWithOwner(999L)).thenReturn(Optional.empty());

            // When
            Optional<RentalDTO> result = rentalService.getRentalById(999L);

            // Then
            assertThat(result).isEmpty();
            verify(rentalRepository, times(1)).findByIdWithOwner(999L);
        }
    }

    @Nested
    @DisplayName("createRental()")
    class CreateRental {

        @Test
        @DisplayName("Should create rental successfully with valid data")
        void shouldCreateRentalSuccessfully() {
            // Given
            CreateRentalRequest request = new CreateRentalRequest(
                    "New Rental",
                    BigDecimal.valueOf(50.00),
                    BigDecimal.valueOf(100.00),
                    testImageFile,
                    "A nice place"
            );

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(fileStorageService.storeFile(testImageFile)).thenReturn("http://localhost:3001/api/uploads/new.jpg");
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
                Rental rental = invocation.getArgument(0);
                rental.setId(2L);
                rental.setCreatedAt(LocalDateTime.now());
                rental.setUpdatedAt(LocalDateTime.now());
                return rental;
            });

            // When
            RentalDTO result = rentalService.createRental(request, userDetails);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("New Rental");
            assertThat(result.surface()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
            assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
            assertThat(result.description()).isEqualTo("A nice place");
            assertThat(result.owner_id()).isEqualTo(1L);

            verify(userRepository, times(1)).findByEmail("test@example.com");
            verify(fileStorageService, times(1)).storeFile(testImageFile);
            verify(rentalRepository, times(1)).save(any(Rental.class));
        }

        @Test
        @DisplayName("Should create rental without description")
        void shouldCreateRentalWithoutDescription() {
            // Given
            CreateRentalRequest request = new CreateRentalRequest(
                    "New Rental",
                    BigDecimal.valueOf(50.00),
                    BigDecimal.valueOf(100.00),
                    testImageFile,
                    null
            );

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(fileStorageService.storeFile(testImageFile)).thenReturn("http://localhost:3001/api/uploads/new.jpg");
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
                Rental rental = invocation.getArgument(0);
                rental.setId(2L);
                rental.setCreatedAt(LocalDateTime.now());
                rental.setUpdatedAt(LocalDateTime.now());
                return rental;
            });

            // When
            RentalDTO result = rentalService.createRental(request, userDetails);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.description()).isNull();
            verify(rentalRepository, times(1)).save(any(Rental.class));
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            CreateRentalRequest request = new CreateRentalRequest(
                    "New Rental",
                    BigDecimal.valueOf(50.00),
                    BigDecimal.valueOf(100.00),
                    testImageFile,
                    "A nice place"
            );

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> rentalService.createRental(request, userDetails))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("User not found");

            verify(fileStorageService, never()).storeFile(any());
            verify(rentalRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should call file storage service to save image")
        void shouldCallFileStorageService() {
            // Given
            CreateRentalRequest request = new CreateRentalRequest(
                    "New Rental",
                    BigDecimal.valueOf(50.00),
                    BigDecimal.valueOf(100.00),
                    testImageFile,
                    "A nice place"
            );

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(fileStorageService.storeFile(testImageFile)).thenReturn("http://localhost:3001/api/uploads/new.jpg");
            when(rentalRepository.save(any(Rental.class))).thenReturn(testRental);

            // When
            rentalService.createRental(request, userDetails);

            // Then
            verify(fileStorageService, times(1)).storeFile(testImageFile);
        }
    }

    @Nested
    @DisplayName("updateRental()")
    class UpdateRental {

        @Test
        @DisplayName("Should update rental successfully with all fields")
        void shouldUpdateRentalSuccessfully() {
            // Given
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Updated Name",
                    BigDecimal.valueOf(70.00),
                    BigDecimal.valueOf(175.00),
                    testImageFile,
                    "Updated description"
            );

            when(rentalRepository.findById(1L)).thenReturn(Optional.of(testRental));
            when(fileStorageService.storeFile(testImageFile)).thenReturn("http://localhost:3001/api/uploads/updated.jpg");
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Optional<RentalDTO> result = rentalService.updateRental(1L, request);

            // Then
            assertThat(result).isPresent();
            verify(rentalRepository, times(1)).findById(1L);
            verify(fileStorageService, times(1)).storeFile(testImageFile);
            verify(rentalRepository, times(1)).save(any(Rental.class));
        }

        @Test
        @DisplayName("Should update rental with only some fields")
        void shouldUpdateRentalWithPartialData() {
            // Given
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Updated Name Only",
                    null,
                    null,
                    null,
                    null
            );

            when(rentalRepository.findById(1L)).thenReturn(Optional.of(testRental));
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Optional<RentalDTO> result = rentalService.updateRental(1L, request);

            // Then
            assertThat(result).isPresent();
            verify(rentalRepository, times(1)).findById(1L);
            verify(fileStorageService, never()).storeFile(any());
            verify(rentalRepository, times(1)).save(any(Rental.class));
        }

        @Test
        @DisplayName("Should not update picture when picture is null or empty")
        void shouldNotUpdatePictureWhenNotProvided() {
            // Given
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Updated Name",
                    null,
                    null,
                    null,
                    null
            );

            when(rentalRepository.findById(1L)).thenReturn(Optional.of(testRental));
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            rentalService.updateRental(1L, request);

            // Then
            verify(fileStorageService, never()).storeFile(any());
        }

        @Test
        @DisplayName("Should update picture when new picture is provided")
        void shouldUpdatePictureWhenProvided() {
            // Given
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    null,
                    null,
                    testImageFile,
                    null
            );

            when(rentalRepository.findById(1L)).thenReturn(Optional.of(testRental));
            when(fileStorageService.storeFile(testImageFile)).thenReturn("http://localhost:3001/api/uploads/new-picture.jpg");
            when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            rentalService.updateRental(1L, request);

            // Then
            verify(fileStorageService, times(1)).storeFile(testImageFile);
        }

        @Test
        @DisplayName("Should return empty Optional when rental not found")
        void shouldReturnEmptyWhenRentalNotFound() {
            // Given
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Updated Name",
                    BigDecimal.valueOf(70.00),
                    BigDecimal.valueOf(175.00),
                    null,
                    "Updated description"
            );

            when(rentalRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<RentalDTO> result = rentalService.updateRental(999L, request);

            // Then
            assertThat(result).isEmpty();
            verify(rentalRepository, times(1)).findById(999L);
            verify(fileStorageService, never()).storeFile(any());
            verify(rentalRepository, never()).save(any());
        }
    }
}
