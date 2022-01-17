package Project.interfaces;

import Project.detailPlugin.pluginsDetail.entitys.CodeEntity;
import Project.detailPlugin.pluginsDetail.entitys.CodeResponse;
import io.github.kloping.MySpringTool.annotations.PathValue;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestBody;

@HttpClient("https://glot.io")
public interface RunCode {

    @PostPath("run/")
    CodeResponse runAny(@PathValue String language,
                        @RequestBody(type = RequestBody.type.json) CodeEntity codeEntity,
                        @ParamName("version") String version);
}







