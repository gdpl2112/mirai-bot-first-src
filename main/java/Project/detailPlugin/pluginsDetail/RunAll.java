package Project.detailPlugin.pluginsDetail;

import Project.detailPlugin.pluginsDetail.entitys.CodeEntity;
import Project.detailPlugin.pluginsDetail.entitys.CodeResponse;

import java.io.IOException;

import static Project.Controllers.ConUtils.runCode;


/**
 * @author github-kloping
 */
public class RunAll {
    public static CodeResponse runJava(CodeEntity entity) throws IOException {
        return runCode.runAny("java", entity, "latest");
    }

    public static CodeResponse runC(CodeEntity entity) throws IOException {
        return runCode.runAny("c", entity, "latest");
    }

    public static CodeResponse runPython(CodeEntity entity) throws IOException {
        return runCode.runAny("python", entity, "latest");
    }

    public static CodeResponse runAny(CodeEntity entity, String language) throws IOException {
        return runCode.runAny(language, entity, "latest");
    }
}
