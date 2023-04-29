package Project.aSpring.mcs;

import javax.annotation.PostConstruct;

/**
 * @author HRS-Computer
 */
public class ComponentA {

    /**
     * @Postcontruct’在依赖注入完成后自动调用
     */
    @PostConstruct
    public void postConstruct() {
    }
}