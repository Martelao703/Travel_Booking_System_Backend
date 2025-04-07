package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.enumsAndSets.PropertyType;
import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.*;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

import java.util.List;

@Data
@AtLeastOneFieldSet
public class PropertyPatchRequestDTO {
    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<PropertyType> propertyType = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedSize(max = 50, message = "Name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> name = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<String> city = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<String> address = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> active = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> underMaintenance = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Double> latitude = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Double> longitude = OptionalFieldWrapper.unset();

    @WrappedSize(max = 1000, message = "Description cannot exceed 1000 characters")
    private OptionalFieldWrapper<String> description = OptionalFieldWrapper.unset();

    @WrappedMin(value = 0, message = "Number of stars cannot be less than 0")
    @WrappedMax(value = 5, message = "Number of stars cannot exceed 5")
    private OptionalFieldWrapper<Integer> stars = OptionalFieldWrapper.unset();

    @WrappedMin(value = 0, message = "User rating cannot be less than 0")
    @WrappedMax(value = 5, message = "User rating cannot exceed 5")
    private OptionalFieldWrapper<Double> userRating = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> amenities = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> nearbyServices = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> houseRules = OptionalFieldWrapper.unset();
}
