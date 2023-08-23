package Project.controllers;

import Project.commons.SpGroup;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;

/**
 * @author github.kloping
 */
public class BaseController {
    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }
}
