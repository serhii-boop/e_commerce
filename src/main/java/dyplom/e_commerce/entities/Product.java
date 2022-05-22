package dyplom.e_commerce.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "category", length = 50)
    private String category;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "type", length = 50)
    private String type;
    @Column(name = "series", length = 10)
    private String series;
    @Column(name = "power")
    private Double power;
    @Column(name = "resistance")
    private Double resistance;
    @Column(name = "capacity")
    private Double capacity;
    @Column(name = "voltage")
    private Double voltage;
    @Column(name = "max_temperature")
    private Double maxTemp;
    @Column(name = "diametr")
    private Integer diametr;
    @Column(name = "length")
    private Integer length;
    @Column(name = "height")
    private Integer height;
    @Column(name = "weight")
    private Integer weight;
    @Column(name = "stream")
    private Double stream;
    @Column(name = "corps", length = 50)
    private String corps;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "general_description")
    private String generalDescription;
    @Column(name = "price")
    private Double price;
    @Column(name = "photo_path")
    private String photoPath;

    @Transient
    public String getLogoImagePath() {
        if (photoPath == null || id == null) return null;

        return "/logo/" + id + "/" + photoPath;
    }

}
