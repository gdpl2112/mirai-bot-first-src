package Project.interfaces;

import io.github.kloping.mirai0.Entitys.apiEntitys.Songs;
import Project.detailPlugin.NetMain;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github-kloping
 */
@HttpClient(NetMain.ROOT_PATH)
public interface GetSongs {
    /**
     * 获取歌曲
     *
     * @param keyword
     * @param type
     * @return
     */
    @GetPath("/api/search/song")
    Songs getSongs(@ParamName("keyword") String keyword, @ParamName("type") String type);
}
