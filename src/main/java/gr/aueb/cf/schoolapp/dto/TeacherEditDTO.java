package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TeacherEditDTO {

    @NotNull
    private String uuid;

    @NotNull
    @Size(min = 2)
    private String firstname;

    @NotNull
    @Size(min = 2)
    private String lastname;

    @Pattern(regexp = "\\d{9,}")
    private String vat;

    @NotNull
    private Long regionId;
}