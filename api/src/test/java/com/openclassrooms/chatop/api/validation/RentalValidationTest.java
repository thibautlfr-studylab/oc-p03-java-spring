package com.openclassrooms.chatop.api.validation;

import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation tests for Rental Request DTOs.
 * Tests Jakarta Bean Validation annotations.
 */
class RentalValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private MockMultipartFile validImageFile;

    @BeforeAll
    static void setUpValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidatorFactory() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        validImageFile = new MockMultipartFile(
                "picture",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Nested
    @DisplayName("CreateRentalRequest Validation")
    class CreateRentalRequestValidation {

        @Test
        @DisplayName("Should pass validation with all valid fields")
        void shouldPassWithValidData() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Charming seaside apartment",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Beautiful apartment with ocean view"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail when name is blank")
        void shouldFailWhenNameIsBlank() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
        }

        @Test
        @DisplayName("Should fail when name is null")
        void shouldFailWhenNameIsNull() {
            CreateRentalRequest request = new CreateRentalRequest(
                    null,
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
        }

        @Test
        @DisplayName("Should fail when name exceeds 255 characters")
        void shouldFailWhenNameTooLong() {
            String longName = "a".repeat(256);
            CreateRentalRequest request = new CreateRentalRequest(
                    longName,
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Name must not exceed 255 characters");
        }

        @Test
        @DisplayName("Should fail when surface is null")
        void shouldFailWhenSurfaceIsNull() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    null,
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Surface is required");
        }

        @Test
        @DisplayName("Should fail when surface is zero")
        void shouldFailWhenSurfaceIsZero() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.ZERO,
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Surface must be positive");
        }

        @Test
        @DisplayName("Should fail when surface is negative")
        void shouldFailWhenSurfaceIsNegative() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(-10.5),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Surface must be positive");
        }

        @Test
        @DisplayName("Should fail when price is null")
        void shouldFailWhenPriceIsNull() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    null,
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Price is required");
        }

        @Test
        @DisplayName("Should fail when price is zero")
        void shouldFailWhenPriceIsZero() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.ZERO,
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
        }

        @Test
        @DisplayName("Should fail when price is negative")
        void shouldFailWhenPriceIsNegative() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(-50.00),
                    validImageFile,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
        }

        @Test
        @DisplayName("Should fail when picture is null")
        void shouldFailWhenPictureIsNull() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    null,
                    "Description"
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Picture is required");
        }

        @Test
        @DisplayName("Should fail when description exceeds 2000 characters")
        void shouldFailWhenDescriptionTooLong() {
            String longDescription = "a".repeat(2001);
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    longDescription
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Description must not exceed 2000 characters");
        }

        @Test
        @DisplayName("Should pass when description is null")
        void shouldPassWhenDescriptionIsNull() {
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    null
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass when description is exactly 2000 characters")
        void shouldPassWhenDescriptionIs2000Chars() {
            String maxDescription = "a".repeat(2000);
            CreateRentalRequest request = new CreateRentalRequest(
                    "Valid Name",
                    BigDecimal.valueOf(65.50),
                    BigDecimal.valueOf(150.00),
                    validImageFile,
                    maxDescription
            );

            Set<ConstraintViolation<CreateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("UpdateRentalRequest Validation")
    class UpdateRentalRequestValidation {

        @Test
        @DisplayName("Should pass validation when all fields are null (partial update)")
        void shouldPassWhenAllFieldsNull() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    null,
                    null,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassWithValidData() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Updated Name",
                    BigDecimal.valueOf(70.00),
                    BigDecimal.valueOf(175.00),
                    validImageFile,
                    "Updated description"
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail when name exceeds 255 characters")
        void shouldFailWhenNameTooLong() {
            String longName = "a".repeat(256);
            UpdateRentalRequest request = new UpdateRentalRequest(
                    longName,
                    null,
                    null,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Name must not exceed 255 characters");
        }

        @Test
        @DisplayName("Should fail when surface is zero")
        void shouldFailWhenSurfaceIsZero() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    BigDecimal.ZERO,
                    null,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Surface must be positive");
        }

        @Test
        @DisplayName("Should fail when surface is negative")
        void shouldFailWhenSurfaceIsNegative() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    BigDecimal.valueOf(-10.5),
                    null,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Surface must be positive");
        }

        @Test
        @DisplayName("Should fail when price is zero")
        void shouldFailWhenPriceIsZero() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    null,
                    BigDecimal.ZERO,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
        }

        @Test
        @DisplayName("Should fail when price is negative")
        void shouldFailWhenPriceIsNegative() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    null,
                    BigDecimal.valueOf(-50.00),
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
        }

        @Test
        @DisplayName("Should fail when description exceeds 2000 characters")
        void shouldFailWhenDescriptionTooLong() {
            String longDescription = "a".repeat(2001);
            UpdateRentalRequest request = new UpdateRentalRequest(
                    null,
                    null,
                    null,
                    null,
                    longDescription
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("Description must not exceed 2000 characters");
        }

        @Test
        @DisplayName("Should pass when only one field is provided")
        void shouldPassWithPartialUpdate() {
            UpdateRentalRequest request = new UpdateRentalRequest(
                    "Just updating name",
                    null,
                    null,
                    null,
                    null
            );

            Set<ConstraintViolation<UpdateRentalRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }
}
