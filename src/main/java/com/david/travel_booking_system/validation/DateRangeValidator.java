package com.david.travel_booking_system.validation;

import com.david.travel_booking_system.dto.createRequest.BookingCreateRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            var checkInDateDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), "checkInDate");
            var checkOutDateDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), "checkOutDate");

            // Check if property descriptors exist
            if (checkInDateDescriptor == null || checkOutDateDescriptor == null) {
                return false; // Invalid if fields are not present
            }

            // Retrieve startDate and endDate values
            LocalDate startDate = (LocalDate) checkInDateDescriptor.getReadMethod().invoke(obj);
            LocalDate endDate = (LocalDate) checkOutDateDescriptor.getReadMethod().invoke(obj);

            // Allow null dates if optional, or validate
            if (startDate == null || endDate == null) {
                return false;
            }

            return startDate.isBefore(endDate);
        } catch (Exception e) {
            return false;
        }
    }
}
