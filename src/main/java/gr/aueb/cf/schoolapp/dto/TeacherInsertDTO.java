package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherInsertDTO {

    // Bean validation by JpaHibernate
    //  notnull and blank messages are defined under :: resources\messages.properties
    @NotNull(message = "{teacher.firstname.notnull}")
    @NotBlank(message = "{teacher.firstname.notblank}")
    @Size(min = 2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον δύο χαρακτήρες.")
    private String firstname;

    @NotNull(message = "{teacher.lastname.notnull}")
    @NotBlank(message = "{teacher.lastname.notblank}")
    @Size(min = 2, message = "Το επώνυμο πρέπει να περιέχει τουλάχιστον δύο χαρακτήρες.")
    private String lastname;

    @NotNull(message = "{teacher.vat.notnull}")
    @NotBlank(message = "{teacher.vat.notblank}")
    @Pattern(regexp = "\\d{9,}", message = "Το ΑΦΜ δεν μπορεί να είναι μικρότερο από εννιά ψηφία.")
    private String vat;

    @NotNull(message = "{teacher.region.notnull}")
    @NotBlank(message = "{teacher.region.notblank}")
    private Long regionId;
}
