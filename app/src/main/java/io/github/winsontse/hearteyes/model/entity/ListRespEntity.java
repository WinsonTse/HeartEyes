package io.github.winsontse.hearteyes.model.entity;

import java.util.List;

/**
 * Created by winson on 2017/1/29.
 */

public class ListRespEntity<T> {
    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
