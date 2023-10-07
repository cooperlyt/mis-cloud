package io.github.cooperlyt.mis.service.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Tag(name = "Word", description = "字典")
@Table("dictionary_word")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Word implements Comparable<Word>,java.io.Serializable{


    @Override
    public int compareTo(Word o) {
        return Integer.compare(this.seq,o.seq);
    }

    /**
     *
     * @Depreacted spring-data-r2dbc issues #574 https://github.com/spring-projects/spring-data-relational/issues/574
     */
    @Data
    @Deprecated
    public static class FakePK{
        private String category;
        private int value;
    }

    @JsonIgnore
    @EqualsAndHashCode.Include()
    private String category;

    @Schema(title = "标签", requiredMode = Schema.RequiredMode.REQUIRED)
    private String label;

    @Schema(title = "值", requiredMode = Schema.RequiredMode.REQUIRED)
    @EqualsAndHashCode.Include()
    private int value;

    @Schema(title = "描述",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(title = "拼音码")
    @JsonIgnore
    private String pin;

    @JsonIgnore
    private int seq;

    @JsonIgnore
    private String reportValue;

    @Column("_group")
    @JsonIgnore
    private String group;

    @JsonIgnore
    private boolean enabled;

    @Transient
    public boolean isOther(){
        return value % 100 == 0;
    }
}
