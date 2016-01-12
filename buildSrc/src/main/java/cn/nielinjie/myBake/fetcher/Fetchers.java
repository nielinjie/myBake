package cn.nielinjie.myBake.fetcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nielinjie on 1/9/16.
 */
public class Fetchers {
    //TODO make this thread safe?
    static DateFormat xmlDateSchemal =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    static <T> Stream<T> streamopt(Optional<T> opt) {
        if (opt.isPresent())
            return Stream.of(opt.get());
        else
            return Stream.empty();
    }

    public static List<Fetcher> fetcher(List<FetcherConfig> configs) {
        Stream<Optional<Fetcher>> f = configs.stream().map(js ->
                        AtomFetcher.get(js)
                    //.orElse(OtherFetcher.get())
        );
        return f.flatMap(Fetchers::streamopt).collect(Collectors.toList());
    }
}
