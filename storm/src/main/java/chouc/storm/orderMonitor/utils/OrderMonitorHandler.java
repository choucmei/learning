package chouc.storm.orderMonitor.utils;



import chouc.storm.orderMonitor.dao.OrderMonitorDao;
import chouc.storm.orderMonitor.domain.Condition;
import chouc.storm.orderMonitor.domain.PaymentInfo;
import chouc.storm.orderMonitor.domain.Trigger;

import java.util.*;

public class OrderMonitorHandler {

    //封装rule规则，该规则有很多判断条件，key为ruleId(uuid),value为多个条件组成的list
    private static Map<String, List<Condition>> ruleMap;

    //加载业务人员配置的所有规则信息，每个规则由多个条件组成
    static {
        loadRuleMap();
    }

    private static synchronized void loadRuleMap() {
        if (ruleMap == null || ruleMap.size() == 0) {
            ruleMap = loadRules();
        }
    }

    private static Map<String, List<Condition>> loadRules() {
        Map<String, List<Condition>> map = new HashMap<String, List<Condition>>();
        List<Condition> conditionList = new OrderMonitorDao().loadRules();
        for (Condition condition : conditionList) {
            String ruleId = condition.getRuleId();
            List<Condition> conditionsByRuleId = map.get(ruleId);
            if (conditionsByRuleId == null || conditionsByRuleId.size() == 0) {
                conditionsByRuleId = new ArrayList<Condition>();
                map.put(ruleId, conditionsByRuleId);
            }
            conditionsByRuleId.add(condition);
        }
        return map;
    }

    public static List<String> match(PaymentInfo paymentInfo) {
        List<String> triggerRuleIdList = new ArrayList<>();
        if (ruleMap == null || ruleMap.size() == 0) {
            loadRuleMap();
        }
        Set<String> ruleIds = ruleMap.keySet();
        for (String ruleId : ruleIds) {
            List<Condition> conditionList = ruleMap.get(ruleId);
            boolean isTrigger = ConditionMatch.match(paymentInfo, conditionList);
            if (isTrigger) {
                triggerRuleIdList.add(ruleId);
            }
        }
        return triggerRuleIdList;
    }

    public static void saveTrigger(String orderId, List<String> ruleIdList) {
        List<Trigger> list = new ArrayList<>();
        for (String ruleId : ruleIdList) {
            list.add(new Trigger(orderId, ruleId));
        }
        new OrderMonitorDao().saveTrigger(list);

    }

    public static void savePaymentInfo(PaymentInfo paymentInfo) {
        new OrderMonitorDao().savePayment(paymentInfo);
    }
}
