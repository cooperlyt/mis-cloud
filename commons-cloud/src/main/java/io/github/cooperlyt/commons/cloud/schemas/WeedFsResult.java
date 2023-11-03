package io.github.cooperlyt.commons.cloud.schemas;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeedFsResult {

    private String eTag;

    private String fid;

    private int size;

    private String mime;

    private String sha256;

}
