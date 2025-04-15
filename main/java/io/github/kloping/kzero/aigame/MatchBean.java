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

    public static <T extends MessageEvent> Exception operation(String input, MatchBean bean, T t) {
        if (input == null || bean == null) return null;

        try {
            switch (bean.matchType) {
                case EXACT_MATCH:
                    handleExactMatch(input, bean, t);
                    break;
                case STARTS_WITH:
                    handleWithMatch(input, bean, t, true);
                    break;
                case ENDS_WITH:
                    handleWithMatch(input, bean, t, false);
                    break;
            }
        } catch (RuleException e) {
            return e;
        }
        return null;
    }

    private static <T extends MessageEvent> void handleExactMatch(String input, MatchBean bean, T t) {
        if (bean.matchStr != null && bean.matchStr.equals(input)) {
            bean.action.onTouch(t, null);
        }
    }

    private static <T extends MessageEvent> void handleWithMatch(String input, MatchBean bean, T t, boolean isStart)
            throws RuleException {
        if (bean.matchStr == null) return;

        boolean isMatched = isStart ? input.startsWith(bean.matchStr) : input.endsWith(bean.matchStr);
        if (!isMatched) return;

        if (bean.rule == null) {
            bean.action.onTouch(t, null);
            return;
        }

        String arg = extractArgument(input, bean.matchStr, isStart);
        validateAndExecute(arg, bean, t);
    }

    private static String extractArgument(String input, String matchStr, boolean isStart) {
        return isStart ? input.substring(matchStr.length()) :
                input.substring(0, input.length() - matchStr.length());
    }

    private static <T extends MessageEvent> void validateAndExecute(String arg, MatchBean bean, T t)
            throws RuleException {

        arg = bean.rule.rule != MatchRule.REQUIRED_ARG ? arg.trim() : arg;
        if (arg.isEmpty()) {
            throw new RuleException(bean.rule.tips);
        }

        if (bean.rule.rule == MatchRule.REQUIRED_NUMBER_ARG) {
            try {
                Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                throw new RuleException(bean.rule.tips);
            }
        }
        bean.action.onTouch(t, arg);
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