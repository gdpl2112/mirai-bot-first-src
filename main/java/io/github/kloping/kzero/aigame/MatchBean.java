package io.github.kloping.kzero.aigame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author github kloping
 * @date 2025/4/14-17:47
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MatchBean {

    public interface TouchAction<T extends MessageEvent> {
        void onTouch(T t, String arg);
    }

    public static <T extends MessageEvent> Exception operation(String input, MatchBean bean, T t)  {
        if (input == null) return null;
        if (bean.matchType == MatchType.EXACT_MATCH) {
            if (input.equals(bean.matchStr)) bean.action.onTouch(t, null);
            else return null;
        } else if (bean.matchType == MatchType.STARTS_WITH) {
            String arg = null;
            if (input.startsWith(bean.matchStr)) {
                if (bean.rule != null) {
                    switch (bean.rule.rule) {
                        case REQUIRED_ARG:
                            arg = input.substring(bean.matchStr.length());
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                bean.action.onTouch(t, arg);
                            }
                            break;
                        case REQUIRED_NOT_EMPTY_ARG:
                            arg = input.substring(bean.matchStr.length()).trim();
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                bean.action.onTouch(t, arg);
                            }
                            break;
                        case REQUIRED_NUMBER_ARG:
                            arg = input.substring(bean.matchStr.length()).trim();
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                try {
                                    Integer.parseInt(arg);
                                    bean.action.onTouch(t, arg);
                                } catch (Exception e) {
                                    return new RuleException(bean.rule.tips);
                                }
                            }
                            break;
                        default:
                            return null;
                    }
                }
            }
        } else if (bean.matchType == MatchType.ENDS_WITH) {
            String arg = null;
            if (input.endsWith(bean.matchStr)) {
                if (bean.rule != null) {
                    switch (bean.rule.rule) {
                        case REQUIRED_ARG:
                            arg = input.substring(0, input.length() - bean.matchStr.length());
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                bean.action.onTouch(t, arg);
                            }
                            break;
                        case REQUIRED_NOT_EMPTY_ARG:
                            arg = input.substring(0, input.length() - bean.matchStr.length()).trim();
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                bean.action.onTouch(t, arg);
                            }
                            break;
                        case REQUIRED_NUMBER_ARG:
                            arg = input.substring(0, input.length() - bean.matchStr.length()).trim();
                            if (arg.length() == 0) {
                                return new RuleException(bean.rule.tips);
                            } else {
                                try {
                                    Integer.parseInt(arg);
                                    bean.action.onTouch(t, arg);
                                } catch (Exception e) {
                                    return new RuleException(bean.rule.tips);
                                }
                            }
                            break;
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }

    public enum MatchType {
        // 完全匹配，字符串相等
        EXACT_MATCH, // 开始匹配，字符串以某个前缀开始
        STARTS_WITH, // 结束匹配，字符串以某个后缀结束
        ENDS_WITH

    }

    public enum MatchRule {
        //需要参数
        REQUIRED_ARG, //需要参数不为空
        REQUIRED_NOT_EMPTY_ARG, //需要数字参数
        REQUIRED_NUMBER_ARG,

    }

    @AllArgsConstructor
    public static class MatchRulePack {
        private MatchRule rule;
        private String tips;
    }

    private MatchType matchType = MatchType.EXACT_MATCH;

    private String matchStr;

    private MatchRulePack rule;

    private TouchAction action;

    public static class RuleException extends RuntimeException {
        public RuleException(String message) {
            super(message);
        }
    }

    public static MatchBean create(String matchStr, TouchAction action) {
        MatchBean bean = new MatchBean();
        bean.matchStr = matchStr;
        bean.action = action;
        bean.matchType = MatchType.EXACT_MATCH;
        bean.rule = null;
        return bean;
    }

    public static MatchBean create(String matchStr, MatchRulePack rule, TouchAction action) {
        MatchBean bean = new MatchBean();
        bean.matchStr = matchStr;
        bean.action = action;
        bean.matchType = MatchType.STARTS_WITH;
        bean.rule = rule;
        return bean;
    }

    public static MatchBean create(String matchStr, String tips, TouchAction action) {
        MatchBean bean = new MatchBean();
        bean.matchStr = matchStr;
        bean.action = action;
        bean.matchType = MatchType.STARTS_WITH;
        bean.rule = new MatchBean.MatchRulePack(MatchBean.MatchRule.REQUIRED_NOT_EMPTY_ARG, tips);
        return bean;
    }

    public static MatchBean create(String matchStr, MatchType matchType, MatchRulePack rule, TouchAction action) {
        MatchBean bean = new MatchBean();
        bean.matchStr = matchStr;
        bean.action = action;
        bean.matchType = matchType;
        bean.rule = rule;
        return bean;
    }
}