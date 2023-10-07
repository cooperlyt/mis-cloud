package io.github.cooperlyt.mis.service.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Schema(name = "District", title = "行政区")
@Table
@Data
@NoArgsConstructor
public class District implements java.io.Serializable {

    public final static int ROOT_LEVEL = 0;

    public District(int level) {
        this.level = level;
    }

    @Id
    private int id;

    @Schema(title = "行政级别")
    private int level;

    @Schema(title = "名称")
    private String name;

    @Schema(title = "地址")
    private String address;


    private String pyCode;

    @ReadOnlyProperty
    @Transient
    private List<District> children = new ArrayList<>(0);

    @ReadOnlyProperty
    @Transient
    @JsonIgnore
    public boolean putChild(District district){
        if (level == ROOT_LEVEL ||
                String.valueOf(district.id).startsWith(String.valueOf(id))
            ){
            if (level != (district.getLevel() - 1)) {
                for (District d : children) {
                    if (d.putChild(district)) {
                        return true;
                    }
                }
            }
            return children.add(district);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return id == district.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
