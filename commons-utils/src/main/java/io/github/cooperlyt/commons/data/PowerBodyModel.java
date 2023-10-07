package io.github.cooperlyt.commons.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 权力主体 - 个人 或 机构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PowerBodyModel implements PowerBody,java.io.Serializable {

  @NotBlank
  @Size(max = 128)
  private String name;
  @NotNull
  private IdentityType idType;
  @NotBlank
  @Size(max = 64)
  private String idNumber;

  @Size(max = 32)
  private String tel;

}
