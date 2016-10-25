import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by zyj on 16/10/22.
 */
public class httpClientTest {
    @Test
    public void httpGetTest() throws IOException{
        String cityId = "310100";
//        String cityId = "310100";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://api.sit.ffan.com/ffan/v1/flashpay/zone?cityId="+cityId;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);

//        System.out.println("��Ӧ��:" + response.getStatusLine().getStatusCode());
//        ��ȡ��������Ӧ����
        String returnStr = EntityUtils.toString(response.getEntity());
        System.out.println("��Ӧ����:" + returnStr);

//        ת����json����
        JSONObject jsonObject = JSONObject.fromObject(returnStr);
        String code = jsonObject.getString("status");
        String message = jsonObject.getString("message");
        String strData = jsonObject.getString("data");
        System.out.println("code:"+code);
        System.out.println("message:"+message);
        System.out.println("data:"+strData);

        JSONObject jsonObjectData = JSONObject.fromObject(strData);
//        ��ȡ��Ŀ
        String strCategory = jsonObjectData.getString("cates");
        System.out.println("cates:"+strCategory);
//        ��ȡ�ŵ�
        String strStores = jsonObjectData.getString("stores");
        System.out.println("stores:"+strStores);
//        ��ȡ����ʽ
        String strSorts = jsonObjectData.getString("sorts");
        System.out.println(strSorts);

//        ��ĿУ��
        assertFalse(strCategory.equals("[]"));
        assertTrue(strCategory.indexOf("ȫ��")>0);
//        �ŵ�У��
        assertFalse(strStores.equals("[]"));
        assertTrue(strStores.indexOf("zyj�����ŵ�")>0);
//        ����У��
        assertFalse(strSorts.equals("[]"));
        assertTrue(strSorts.indexOf("�������")>0);

//        �ͷ�����
        httpGet.releaseConnection();
    }


    @Test
    public void httpPostTest() throws IOException{
        String postUrl = "http://api.sit.ffan.com/push/accounts";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("platform", "1"));
        params.add(new BasicNameValuePair("user_list", "[\"15000000000990856\"]"));
        params.add(new BasicNameValuePair("content", "ffapp_test"));
        params.add(new BasicNameValuePair("app_id", "1"));

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println(response.toString());

        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(entity, "utf-8");
        System.out.println(jsonStr);

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        String ret = jsonObject.getString("ret");
//        У�鷵����,0-���ͳɹ�
        assertEquals(ret,"0");



        httpPost.releaseConnection();

    }

}
