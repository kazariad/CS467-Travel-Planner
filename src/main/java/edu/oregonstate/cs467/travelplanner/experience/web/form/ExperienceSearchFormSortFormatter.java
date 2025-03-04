package edu.oregonstate.cs467.travelplanner.experience.web.form;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class ExperienceSearchFormSortFormatter implements Formatter<ExperienceSearchFormSort> {
    @Override
    public String print(ExperienceSearchFormSort object, Locale locale) {
        return object.toString();
    }

    @Override
    public ExperienceSearchFormSort parse(String text, Locale locale) throws ParseException {
        try {
            return ExperienceSearchFormSort.fromValue(text);
        } catch (Exception e) {
            return null;
        }
    }
}
