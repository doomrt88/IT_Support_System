package utility;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@FacesConverter("localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_PATTERN));

        return LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null) {
            return null;
        }
        
        return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}

