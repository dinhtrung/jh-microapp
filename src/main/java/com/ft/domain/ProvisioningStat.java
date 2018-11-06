package com.ft.domain;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date May 31, 2018, 6:23:31 AM
 * @Quote To code is human, to debug is coffee
 */
@Document(collection = "provisioning_stat")
public class ProvisioningStat implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Field("category")
    private String category;
    @Field("total")
    private Integer total;
    @Field("failed")
    private Integer failed;
    @Field("successful")
    private Integer successful;
    @Field("untried")
    private Integer untried;
    @Field("prov_date")
    @Indexed
    private Date provDate;
    @Field("stat_type")
    @Indexed
    private String statType; // category_based or product_based

    public ProvisioningStat() {
    }
    
    public ProvisioningStat(String category, Integer total, Integer failed, Integer successful, 
            Integer untried, Date provDate, String statType) {
        this.category = category;
        this.total = total;
        this.failed = failed;
        this.successful = successful;
        this.untried = untried;
        this.provDate = provDate;
        this.statType = statType;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public Integer getSuccessful() {
        return successful;
    }

    public void setSuccessful(Integer successful) {
        this.successful = successful;
    }

    public Integer getUntried() {
        return untried;
    }

    public void setUntried(Integer untried) {
        this.untried = untried;
    }

    public Date getProvDate() {
        return provDate;
    }

    public void setProvDate(Date provDate) {
        this.provDate = provDate;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

}
