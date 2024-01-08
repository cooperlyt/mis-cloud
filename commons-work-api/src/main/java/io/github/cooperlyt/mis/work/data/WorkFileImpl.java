package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkFileImpl implements WorkFileInfo{

    private String fid;
    private String sha256;
    private Integer size;
    private String mime;
    private String eTag;

    private String filename;
}
