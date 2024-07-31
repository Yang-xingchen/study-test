package sql;

import java.io.Serializable;

public class Order implements Serializable {

    private String id;
    private Long userId;
    private Long goodId;
    private Long count;

    public Order() {
    }

    public Order(String id, Long userId, Long goodId, Long count) {
        this.id = id;
        this.userId = userId;
        this.goodId = goodId;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
