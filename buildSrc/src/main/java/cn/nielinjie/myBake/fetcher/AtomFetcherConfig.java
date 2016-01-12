package cn.nielinjie.myBake.fetcher;

import java.net.URI;

/**
 * Created by nielinjie on 1/12/16.
 */
public class AtomFetcherConfig implements FetcherConfig {
    private URI uri;

    public AtomFetcherConfig(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }




}
