package cn.nielinjie.myBake.fetcher;

import org.jbake.app.Crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doc {
    private String author;
    private Date date;
    private List<String> tags;
    private String body;
    private String title;
    private String link;

    public Doc(String author, Date date, List<String> tags, String title, String body, String link) {
        this.author = author;
        this.date = date;
        this.tags = tags;
        this.body = body;
        this.title = title;
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    private String lastPart(String path){
        String[] parts = path.split("/");
        if (path.endsWith("/"))
            return parts[parts.length-1];
        else
            return parts[parts.length];
    }

    public String toFileName() {
        try {
            URL  url = new URL(this.link);
            String path = url.getPath();
            String fileName = lastPart(path);
            return url.getHost()+path+"/"+fileName+".html";
        } catch (MalformedURLException e) {
            return this.title;
        }
    }

    @Override
    public String toString() {
        return "Doc{" +
                "author='" + author + '\'' +
                ", date=" + date +
                ", tags=" + tags +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        //TODO need any encode?
        sb.append("title=" + this.title).append("\n");
        sb.append("author=" + this.author).append("\n");
        sb.append("type=" + "post").append("\n");
        sb.append("status=" + "published").append("\n");
        //TODO need any encode?
        sb.append("link=" + this.link).append("\n");
        sb.append("date=" + Fetchers.xmlDateSchemal.format(this.date)).append("\n");
        sb.append("tags=" + String.join(",", this.tags.toArray(new String[this.tags.size()]))).append("\n");
        sb.append("~~~~~~\n");
        sb.append("<html><body>").append(this.body).append("</body></html>");
        return sb.toString();
    }


}