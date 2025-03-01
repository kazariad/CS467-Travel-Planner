package edu.oregonstate.cs467.travelplanner.experience.service.dto;

import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ValidExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.util.validation.NotBlankNull;
import jakarta.validation.*;
import jakarta.validation.constraints.*;

import java.lang.annotation.*;

@ValidExperienceSearchParams
public class ExperienceSearchParams {
    public record ExperienceSearchLocationParams(
            @Min(-90) @Max(90)
            double lat,
            @Min(-180) @Max(180)
            double lng,
            @PositiveOrZero
            double distanceMeters
    ) {}

    public enum ExperienceSearchSort {
        KEYWORD_MATCH, DISTANCE, RATING, NEWEST
    }

    @NotBlankNull
    @Size(max = 100)
    private String keywords;

    @Valid
    private ExperienceSearchLocationParams location;

    @NotNull
    private ExperienceSearchSort sort;

    @PositiveOrZero
    private int offset;

    @Positive
    private int limit;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public ExperienceSearchLocationParams getLocation() {
        return location;
    }

    public void setLocation(ExperienceSearchLocationParams location) {
        this.location = location;
    }

    public ExperienceSearchSort getSort() {
        return sort;
    }

    public void setSort(ExperienceSearchSort sort) {
        this.sort = sort;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = { ExperienceSearchParamsValidator.class })
    @interface ValidExperienceSearchParams {
        String message() default "has invalid sort";
        Class<?>[] groups() default { };
        Class<? extends Payload>[] payload() default { };
    }

    static class ExperienceSearchParamsValidator implements ConstraintValidator<ValidExperienceSearchParams, ExperienceSearchParams> {
        @Override
        public boolean isValid(ExperienceSearchParams params, ConstraintValidatorContext context) {
            if (params == null) return true;
            if (params.getKeywords() == null && params.getSort() == ExperienceSearchSort.KEYWORD_MATCH) return false;
            if (params.getLocation() == null && params.getSort() == ExperienceSearchSort.DISTANCE) return false;
            return true;
        }
    }
}
