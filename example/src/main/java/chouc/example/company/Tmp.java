package chouc.example.company;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author chouc
 * @version V1.0
 * @Title: Tmp
 * @Package chouc.example.company
 * @Description:
 * @date 8/6/19
 */
public class Tmp {
    /**
     * 拼接字符串,把(name,type)拼接成{name:type,name:type}的list
     * @param map 传递过来的(字段,类型)
     */
    static List<String> concatList(LinkedHashMap<String, String> map){
        List<String> colList = new ArrayList<>();
        if(map == null){
            return colList;
        }
        map.forEach((nameAndComment,type)-> colList.add(nameAndComment+":"+type));
        return colList;
    }

    static boolean intersectionWithName(List<String> current, String target, Boolean withComment, Boolean withType){
        String[] targetArray = target.split(":");
        for (String c:current){
            String[] cArrary = c.split(":");
            if (targetArray[0].equalsIgnoreCase(cArrary[0])){
                if (withComment || withType) {
                    if (targetArray[1].equalsIgnoreCase(cArrary[1])){
                        if (withType) {
                            if (targetArray[2].equalsIgnoreCase(cArrary[2])){
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    static boolean checkUse(LinkedHashMap<String, String> target, LinkedHashMap<String, String> current){
        List<String> targetList = concatList(target);
        List<String> currentList = concatList(current);
        List<String> reduce = targetList.stream().filter(item -> !currentList.contains(item)).collect(toList());
        if (reduce.size() == 0) {
            return false;
        }
        List<String> modifyCol = reduce.stream().filter(item -> intersectionWithName(currentList,item,false,false)).collect(toList());
        if (modifyCol.size() > 0 ){
            return false;
        }
        List<String> intersection = targetList.stream().filter(item -> currentList.contains(item)).collect(toList());
        String intersectionString = intersection.stream().collect(Collectors.joining(","));
        String currentListString = currentList.stream().collect(Collectors.joining(","));
        String targetListString = targetList.stream().collect(Collectors.joining(","));
        if (currentListString.toUpperCase().startsWith(intersectionString.toUpperCase()) && targetListString.toUpperCase().startsWith(intersectionString.toUpperCase())){
            return true;
        } else {
            return false;
        }
    }

    public static LinkedHashMap<String,String> getFiels(LinkedHashMap<String,String> target,LinkedHashMap<String,String> current){
        LinkedHashMap<String,String> result = new LinkedHashMap<>();
        result.putAll(current);
        result.putAll(target);
        return result;
    }

    public static void main(String[] args) {
        String a = "asdfasdf:1fasdfasdfs";
        String sql =a.substring(a.indexOf(":")+1);
        System.out.println(sql);
        System.out.println(getBizResponse("http://192.168.95.235:8089/apps/ad","post","debug","get_nodate_report","{\"condition\":{\"metric_id\":\"cnt_request,exposure_pv,click_pv,exposure_uv,click_uv,cnt_left_flow\",\"lat_type\":\"gender\",\"start_date\":\"2019-08-07\",\"end_date\":\"2019-08-07\",\"aid\":\"a1d2e9bd5d0c44bf8f6a5b85f623e8a0\",\"mediaid\":\"69712365a1e645a592ac6974d1a7c2ce\",\"orderby\":\"cnt_request\"},\"platform_id\":\"ad\",\"page_num\":1,\"page_size\":10}"));
    }


    public static Map<String, Object> getBizResponse(String url, String requestType,String model , String action,String param) {
        Map<String,Object> result = new HashMap<>();
        if (isEmpty(url.trim())){
            result.put("code", 500);
            result.put("msg", "url 不能为空");
        }
        if (isEmpty(requestType.trim())){
            result.put("code", 500);
            result.put("msg", "requestType 不能为空");
        }
        if (isEmpty(model.trim())){
            result.put("code", 500);
            result.put("msg", "model 不能为空");
        }
        if (isEmpty(action.trim())){
            result.put("code", 500);
            result.put("msg", "action 不能为空");
        }
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            String urlTemplate = "%s/%s?action=%s";
            HttpRequestBase httpRequest = null;
            switch (requestType) {
                case "post":
                    httpRequest = new HttpPost(String.format(urlTemplate,url,model,action));
                    if (!isEmpty(param.trim())){
                        httpRequest.setHeader("Content-Type", "application/json;charset=utf8");
                        StringEntity entity = new StringEntity(param.trim(), "UTF-8");
                        ((HttpPost) httpRequest).setEntity(entity);
                    }
                    break;
                case "get":
                    httpRequest = new HttpGet(String.format(urlTemplate,url,model,action));
                    break;
                case "put":
                    httpRequest = new HttpPut(String.format(urlTemplate,url,model,action));
                    if (!isEmpty(param.trim())){
                        httpRequest.setHeader("Content-Type", "application/json;charset=utf8");
                        StringEntity entity = new StringEntity(param.trim(), "UTF-8");
                        ((HttpPut) httpRequest).setEntity(entity);
                    }
                    break;
                case "delete":
                    httpRequest = new HttpDelete(String.format(urlTemplate,url,model,action));
                    break;
            }
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            HttpEntity responseEntity = httpResponse.getEntity();
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String responseMsg = EntityUtils.toString(responseEntity);
            System.out.println(responseMsg);
            result.put("code", responseCode);
            result.put("msg", responseMsg);
        }catch (Exception e){
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    public static boolean isEmpty(String string){
        if (string == null || string.trim().equals("")){
            return true;
        } else {
            return false;
        }
    }
}
