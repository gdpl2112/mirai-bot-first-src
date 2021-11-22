package Project.interfaces;

import Project.Plugins.pluginsDetail.entitys.CodeEntity;
import Project.Plugins.pluginsDetail.entitys.CodeResponse;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestBody;

@HttpClient("https://glot.io")
public interface RunCode {

    @PostPath("run/java?version=latest")
    CodeResponse runJava(@RequestBody(type = "json") CodeEntity codeEntity);

    @PostPath("run/python?version=latest")
    CodeResponse runPython(@RequestBody(type = "json") CodeEntity codeEntity);

    @PostPath("run/c?version=latest")
    CodeResponse runC(@RequestBody(type = "json") CodeEntity codeEntity);
}







