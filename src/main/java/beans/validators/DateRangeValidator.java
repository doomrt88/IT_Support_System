package beans.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDateTime;

@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        LocalDateTime startDate = (LocalDateTime) component.getAttributes().get("startDate");
        LocalDateTime endDate = (LocalDateTime) value;

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            FacesMessage message = new FacesMessage("Invalid dates", "Start date cannot be after end date.");
            throw new ValidatorException(message);
        }
    }
}
