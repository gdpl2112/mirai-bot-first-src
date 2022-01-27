package io.github.kloping.mirai0.Entitys.apiEntitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Song {
    private String media_name;
    private String author_name;
    private String imgUrl;
    private String songUrl;
}
