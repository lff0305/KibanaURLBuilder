package org.lff.kibana;

import org.lff.kibana.vo.DateRangeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Created by liuff on 2017/3/21 22:07
 */
public class TestMain {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] argu) {
        KibanaService service = new KibanaService();
        String dashboard = "New-Dashboard";
        DateRangeVO dateRangeVO = new DateRangeVO();
        String url = service.build(dashboard, "http://10.16.33.175:9200", "http://10.16.33.175:5601", dateRangeVO);
        logger.info("Get URL for {} is {}", dashboard, url);
    }
}
