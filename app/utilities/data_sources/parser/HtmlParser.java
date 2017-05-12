package utilities.data_sources.parser;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public List<ParsedHtmlData> traverseDocument(String resourceUrl) throws IOException {
        Document doc = Jsoup.connect(resourceUrl).get();
        List<ParsedHtmlData> result = new ArrayList<>();
        Elements allElements = doc.getAllElements();
        for(Element element : allElements) {
            String contentType = element.className();
            String elementName = element.nodeName();
            String resourceLocation = element.attr("abs:href");
            long size = 0;
            try {
                URL url = new URL(resourceLocation);
                size = url.openConnection().getContentLength();
            } catch(IOException e) {
                size = 0;
            }
            if(!contentType.isEmpty()) {
                result.add(new ParsedHtmlData(contentType, elementName, resourceLocation, size));
            }
        }
        return result;
    }
}
