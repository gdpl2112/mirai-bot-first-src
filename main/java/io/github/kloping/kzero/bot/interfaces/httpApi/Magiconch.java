package io.github.kloping.kzero.bot.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.Headers;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestBody;
import io.github.kloping.kzero.bot.commons.apiEntitys.magiconch.MagiconchNbnhhshRequest;
import io.github.kloping.kzero.bot.commons.apiEntitys.magiconch.MagiconchNbnhhshResponse;

import java.util.Map;

/**
 * @author github.kloping
 */
@HttpClient("https://lab.magiconch.com/")
public interface Magiconch {
    /**
     * guess about yyds xsl
     *
     * @param request
     * @param header
     * @return
     */
    @PostPath("api/nbnhhsh/guess")
    MagiconchNbnhhshResponse[] trans(
            @RequestBody MagiconchNbnhhshRequest request,
            @Headers Map<String, String> header
    );
}
