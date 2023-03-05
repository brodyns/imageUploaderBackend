package com.example.imageupload.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FileName: ImageInfo
 * author: gxs
 * Date: 2023/2/17  14:29
 */
@Data
@AllArgsConstructor
public class ImageInfo {
    private String url;
    private String label;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
