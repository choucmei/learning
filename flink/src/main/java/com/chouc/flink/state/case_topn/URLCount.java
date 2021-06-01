package com.chouc.flink.state.case_topn;

/**
 * @author chouc
 * @version V1.0
 * @Title: URLCount
 * @Package com.chouc.flink.state.case_topn
 * @Description:
 * @date 2021/3/9
 */
public class URLCount {
    private String url;
    private Long count;
    private Long windowEnd;

    public URLCount(String url, Long count, Long windowEnd) {
        this.url = url;
        this.count = count;
        this.windowEnd = windowEnd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Long windowEnd) {
        this.windowEnd = windowEnd;
    }
}
