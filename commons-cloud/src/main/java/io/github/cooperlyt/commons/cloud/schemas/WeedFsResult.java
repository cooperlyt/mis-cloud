package io.github.cooperlyt.commons.cloud.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeedFsResult {

    public WeedFsResult(WeedFsResult weedFsResult) {
        this.eTag = weedFsResult.getETag();
        this.fid = weedFsResult.getFid();
        this.size = weedFsResult.getSize();
        this.mime = weedFsResult.getMime();
        this.sha256 = weedFsResult.getSha256();
    }

    private String eTag;

    private String fid;

    private Integer size;

    private String mime;

    private String sha256;

}
