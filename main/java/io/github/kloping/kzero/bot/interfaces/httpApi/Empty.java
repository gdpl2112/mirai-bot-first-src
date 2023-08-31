package io.github.kzero.bot.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PathValue;
import org.jsoup.nodes.Document;

/**
 * @author github.kloping
 */
@HttpClient("")
public interface Empty {
    /**
     * path value
     *
     * @param path
     * @return
     */
    @GetPath("")
    Document empty(@PathValue String path);
}
