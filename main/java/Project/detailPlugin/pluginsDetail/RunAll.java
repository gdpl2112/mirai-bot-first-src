package Project.detailPlugin.pluginsDetail;

import Project.detailPlugin.pluginsDetail.entitys.CodeEntity;
import Project.detailPlugin.pluginsDetail.entitys.CodeResponse;
import Project.interfaces.RunCode;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.IOException;


/**
 * @author github-kloping
 */
@Entity
public class RunAll {
    @AutoStand
    RunCode runCode;

    public CodeResponse runJava(CodeEntity entity) throws IOException {
        return runCode.runAny("java", entity, "latest");
    }

    public CodeResponse runC(CodeEntity entity) throws IOException {
        return runCode.runAny("c", entity, "latest");
    }

    public CodeResponse runPython(CodeEntity entity) throws IOException {
        return runCode.runAny("python", entity, "latest");
    }

    public CodeResponse runAny(CodeEntity entity, String language) throws IOException {
        return runCode.runAny(language, entity, "latest");
    }
}
