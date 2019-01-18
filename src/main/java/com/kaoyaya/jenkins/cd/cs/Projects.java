package com.kaoyaya.jenkins.cd.cs;

import net.sf.json.JSONArray;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Projects {

    public String Name; // 应用名称
    public String Description; // 应用描述
    public String Template; // 应用Compose模板
    public String Version; // 应用版本
    public Date Created; // 应用创建时间
    public Date Updated; // 应用更新时间
    public String DesiredState; // 期望状态 （如果当前状态是中间状态时，期望状态指明变迁终态）
    public String CurrentState; // 当前状态
    public Map<String, String> Environment; // 环境变量key/value
    public Integer[] Services; // 服务列表

    public List<String> projectNameList = new ArrayList<String>();
    public String masterURL;
    public Core core;
    CloseableHttpClient httpClient;

    public Projects(String url, String caCertS, String clientCertS, String clientKeyS) {
        masterURL = url + "/projects/";
        core = new Core(caCertS, clientCertS, clientKeyS);
        httpClient = core.getHttpClient();
        GetProjectList();
    }

    public Projects() {}

    // 获取应用列表
    public Projects[] GetProjectList() {
        try {
            HttpGet httpGet = new HttpGet(masterURL);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String bodyAsString = EntityUtils.toString(response.getEntity());
            JSONArray array = JSONArray.fromObject(bodyAsString);
            for (int i = 0; i < array.size(); i++) {
                projectNameList.add(array.getJSONObject(i).get("name").toString());
            }
            response.close();
            httpGet.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Projects[]{};
    }

    // 单个应用详情
    public Projects GetProjectByName(String name) {
        return new Projects();
    }

    // 蓝绿部署

    // 蓝绿部署确认
}
