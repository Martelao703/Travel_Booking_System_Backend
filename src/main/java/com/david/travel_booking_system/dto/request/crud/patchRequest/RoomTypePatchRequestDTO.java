package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.annotation.WrappedMin;
import com.david.travel_booking_system.validation.annotation.WrappedSize;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class RoomTypePatchRequestDTO {
    @NotNullIfExplicitlySet
    @WrappedSize(max = 50, message = "Name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> name = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedMin(value = 0, message = "Price per night cannot be less than 0")
    private OptionalFieldWrapper<Double> pricePerNight = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedMin(value = 0, message = "Size cannot be less than 0")
    private OptionalFieldWrapper<Double> size = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedMin(value = 0, message = "Max capacity cannot be less than 0")
    private OptionalFieldWrapper<Integer> maxCapacity = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Getter(AccessLevel.NONE)
    private OptionalFieldWrapper<Boolean> hasPrivateBathroom = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Getter(AccessLevel.NONE)
    private OptionalFieldWrapper<Boolean> hasPrivateKitchen = OptionalFieldWrapper.unset();

    @WrappedSize(max = 1000, message = "Description cannot exceed 1000 characters")
    private OptionalFieldWrapper<String> description = OptionalFieldWrapper.unset();

    @WrappedSize(max = 50, message = "View cannot exceed 50 characters")
    private OptionalFieldWrapper<String> view = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> roomFacilities = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> bathroomFacilities = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> kitchenFacilities = OptionalFieldWrapper.unset();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private OptionalFieldWrapper<List<String>> roomRules = OptionalFieldWrapper.unset();

    public OptionalFieldWrapper<Boolean> hasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public OptionalFieldWrapper<Boolean> hasPrivateKitchen() {
        return hasPrivateKitchen;
    }
}
