package io.github.cooperlyt.mis.service.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("dictionary_category")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;
    private String name;
    private String description;
    private boolean disable;
}
