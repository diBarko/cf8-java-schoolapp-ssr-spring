package gr.aueb.cf.schoolapp.dto;

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
public class UserInsertDTO {

    @NotNull(message = "{NotNull.userInsertDTO.username}")
    @Size(min = 2, max = 20, message = "{Size.userInsertDTO.username}")
    private String username;

    @NotNull(message = "{NotNull.userInsertDTO.username}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$", message = "{Size.userInsertDTO.username}")
    private String password;

    @NotNull
    private Long roleId;
}