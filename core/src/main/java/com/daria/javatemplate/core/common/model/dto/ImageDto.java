package com.daria.javatemplate.core.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Integer width;
    private Integer height;
    private String url;
    private String fullUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDto image = (ImageDto) o;
        return width == image.width &&
            height == image.height &&
            Objects.equals(url, image.url) &&
            Objects.equals(fullUrl, image.fullUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, url, fullUrl);
    }
}
