package io.github.cooperlyt.commons.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

public interface EditableData {

  Long getVersion();
}
