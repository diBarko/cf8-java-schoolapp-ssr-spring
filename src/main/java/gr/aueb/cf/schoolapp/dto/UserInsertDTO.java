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

    @NotNull(message = "{user.username.notnull}")
    @Size(min = 2, max = 20, message = "{user.username.size}")
    private String username;

    @NotNull(message = "{user.password.notnull}")
    @Size(min=8,  message = "{user.password.size}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$",
            message = "{user.password.pattern}")
    private String password;

    @NotNull
    private Long roleId;
}