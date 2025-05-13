package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            var checkInDateTimeDescriptor =
                    BeanUtils.getPropertyDescriptor(obj.getClass(), "plannedCheckInDateTime");
            var checkOutDateTimeDescriptor =
                    BeanUtils.getPropertyDescriptor(obj.getClass(), "plannedCheckOutDateTime");

            // Check if property descriptors exist
            if (checkInDateTimeDescriptor == null || checkOutDateTimeDescriptor == null) {
                return false; // Invalid if fields are not present
            }

            // Retrieve startDate and endDate values
            LocalDateTime startDateTime = (LocalDateTime) checkInDateTimeDescriptor.getReadMethod().invoke(obj);
            LocalDateTime endDateTime = (LocalDateTime) checkOutDateTimeDescriptor.getReadMethod().invoke(obj);

            // Allow null dates if optional, or validate
            if (startDateTime == null || endDateTime == null) {
                return false;
            }

            return startDateTime.isBefore(endDateTime);
        } catch (ReflectiveOperationException  e) {
            return false;
        }
    }
}
