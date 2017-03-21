package org.lff.kibana;

/**
 * Created by liuff on 2017/3/21 22:07
 */
public class TestMain {
    public static void main(String[] argu) {
        KibanaService service = new KibanaService();
        service.build("New-Dashboard");
    }
}
