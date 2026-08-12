package cards.mpay.uapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Generic paginated list envelope returned by list-style endpoints.
 *
 * @param <T> the item type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {

    private long total;

    @JsonProperty("per_page")
    private long perPage;

    @JsonProperty("last_page")
    private long lastPage;

    @JsonProperty("current_page")
    private long currentPage;

    private List<T> items;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPerPage() {
        return perPage;
    }

    public void setPerPage(long perPage) {
        this.perPage = perPage;
    }

    public long getLastPage() {
        return lastPage;
    }

    public void setLastPage(long lastPage) {
        this.lastPage = lastPage;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Page{total=" + total + ", perPage=" + perPage + ", lastPage=" + lastPage
                + ", currentPage=" + currentPage + ", items=" + items + '}';
    }
}
