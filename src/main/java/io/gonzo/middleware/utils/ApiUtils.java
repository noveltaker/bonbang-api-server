package io.gonzo.middleware.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ApiUtils {

    public static boolean setYearYn(String codeName) {
        return codeName.contains("Year");
    }

    public static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    public static void resultCodeByException(Document doc) {

        String resultCode = doc.getElementsByTagName("resultCode")
                .item(0)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        String resultMsg = doc.getElementsByTagName("resultMsg")
                .item(0)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        log.info("result code :: >> {}", resultCode);

        log.info("resultMsg :: >> {}", resultMsg);

        if ("99".equals(resultCode)) {
            throw new NullPointerException();
        }

    }

    public static NodeList getByPublicApiItems(String publicUrl) {

        log.info("URI:: {}", publicUrl);

        NodeList nodeList = null;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(publicUrl);

            doc.getDocumentElement().normalize();

            resultCodeByException(doc);

            nodeList = doc.getElementsByTagName("item");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return nodeList;
        }
    }

    public static String createByTrendingDate(String dateStr, Long trendingNum) {
        String trendingDateStr = null;
        try {

            SimpleDateFormat dateParser = new SimpleDateFormat("yyyyMM");

            trendingDateStr = dateParser.parse(dateStr)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .minusMonths(trendingNum).format(DateTimeFormatter.ofPattern("yyyyMM"));

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return trendingDateStr;
        }
    }

}