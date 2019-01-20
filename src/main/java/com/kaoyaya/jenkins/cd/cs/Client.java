package com.kaoyaya.jenkins.cd.cs;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private String endPoint;
    private CloseableHttpClient httpClient;

    private static Integer OK = 200;
    private static Integer Accepted = 202;

    public Client(String endPoint, String caCertS, String clientCertS, String clientKeyS) {
        Core core = new Core(caCertS, clientCertS, clientKeyS);
        this.endPoint = endPoint;
        this.httpClient = core.getHttpClient();
    }

    private ReturnMsg get(String path) {
        HttpGet httpGet = new HttpGet(this.endPoint + path);
        return this.request(httpGet);
    }

    private ReturnMsg post(String path, HashMap<String, Object> params) {
        HttpPost httpPost = new HttpPost(this.endPoint + path);
        JSONObject jsonParam = new JSONObject();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            jsonParam.put(param.getKey(), param.getValue());
        }
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return this.request(httpPost);
    }

    private ReturnMsg request(HttpUriRequest request) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            returnMsg.setCode(response.getStatusLine().getStatusCode());
            returnMsg.setBody(EntityUtils.toString(response.getEntity()));
            response.close();
            request.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnMsg;
    }

    // 获取应用列表
    public List<Project> getProjectList() {
        String body = this.get("/projects/").getBody();
        return JSON.parseArray(body, Project.class);
    }

    // 单个应用详情
    public Project getProjectByName(String name) {
        String body = this.get("/projects/" + name).getBody();
        return JSON.parseObject(body, Project.class);
    }

    // 蓝绿部署
    public boolean updateProjectByBlueGreen(String name, String template, String version) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("update_method", "blue-green");
        params.put("template", template);
        params.put("version", version);
        params.put("latest_image", true);
        ReturnMsg returnMsg = this.post("/projects/" + name + "/update", params);
        return returnMsg.getCode().equals(Accepted);
    }

    // 蓝绿部署确认
    public boolean confirmUpdateProject(String name) {
        HashMap<String, Object> params = new HashMap<>();
        ReturnMsg returnMsg = this.post("/projects/" + name + "/confirm-update?force=true", params);
        return returnMsg.getCode().equals(Accepted) || returnMsg.getCode().equals(OK);
    }
}
