package top.mnsx.my_spring.parser;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.paser
 * @CreateTime: 2022/7/10
 * @Description: XML解析工具
 */
public class XmlSpringConfigParser {

    public static String getBasePackage(String springConfig) {
        String basePackage = "";

        SAXReader reader = new SAXReader();
        InputStream inputStream = XmlSpringConfigParser.class.getClassLoader().getResourceAsStream(springConfig);
        try {
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element element = rootElement.element("component-scan");
            Attribute attribute = element.attribute("base-package");
            basePackage = attribute.getText();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return basePackage;
    }
}
