package cn.nielinjie.myBake.fetcher;

import com.mashape.unirest.http.Unirest;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nielinjie on 1/9/16.
 */
public class AtomFetcher implements Fetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomFetcher.class);


    //TODO filters
    private URI uri;

    public static Optional<Fetcher> get(FetcherConfig config) {
        AtomFetcher re = null;
        if (config instanceof AtomFetcherConfig) {
            AtomFetcherConfig atomFetcherConfig = (AtomFetcherConfig) config;
            try {
                re = new AtomFetcher(atomFetcherConfig.getUri());
            } catch (Exception e) {
                LOGGER.info("not config for AtomFetcher", e);
            }
        }
        return Optional.ofNullable(re);
    }

    public AtomFetcher(URI uri) {
        //feed://www.nietongxue.xyz/atom.xml
        this.uri = uri;
    }

    @Override
    public List<Doc> fetch() {
        try {
            URI u = toHttp();
            String body = Unirest.get(u.toString())
                    .asString().getBody();
            List<Doc> docs = getDocsFromString(body);
            return docs;
        } catch (Exception e) {
            LOGGER.error("error on fetching", e);
            return Arrays.asList();
        }
    }

    List<Doc> getDocsFromString(String body) {
        Document atom = Jsoup.parse(body);
        String author = atom.select("author name").text();
        Elements entries = atom.select("entry");
        return entries.stream().map(
                (Element e) ->
                {
                    Date date = null;
                    try {
                        String updated = e.select("updated").text();
                        date = Fetchers.xmlDateSchemal.parse(updated);
                    } catch (ParseException e1) {
                        LOGGER.error("illegal date format", e1);
                    }
                    List<String> tags = e.select("category").stream().map(x -> x.attr("term")).collect(Collectors.toList());
                    Doc re = new Doc(author, date == null ? new Date() : date,
                            tags, ridCData(e.select("title").text()),
                            e.select("content").text(), e.select("link").attr("href"));
                    return re;
                }
        ).collect(Collectors.toList());

    }

    private String ridCData(String xml) {
        return xml.replace("<![CDATA[", "").replace("]]>", "");
    }

    private URI toHttp() throws URISyntaxException {
        return new URI(this.uri.getScheme().equals("feed") ? "http" : this.uri.getScheme(),
                this.uri.getHost(),
                this.uri.getPath(),
                this.uri.getFragment());
    }
}
