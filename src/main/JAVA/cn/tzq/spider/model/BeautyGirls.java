package cn.tzq.spider.model;


import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by tzq139 on 2017/3/21.
 */
@Data
@Entity
@Table(name = "beautygirls")
public class BeautyGirls implements Serializable {
    private static final long serialVersionUID = -8459755922288527580L;

    @Id
    @Column(name = "keyid")
    @GeneratedValue
    Integer keyid;

    @Column(name = "imagetheme")
    String imageTheme;

    @Column(name = "imageurl")
    String imageUrl;

    @Column(name = "download")
    Integer download;

    @Column(name = "deleted")
    Integer deleted;
}
