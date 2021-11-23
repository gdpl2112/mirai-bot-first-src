package Project.interfaces;

import Entitys.apiEntitys.Songs;
import Project.Plugins.NetMain;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

@HttpClient(NetMain.rootPath)
public interface GetSongs {
    @GetPath("/api/search/song")
    Songs getSongs(@ParamName("keyword") String keyword,@ParamName("type") String type);
}
