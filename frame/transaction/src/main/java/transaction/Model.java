package transaction;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Table
@Entity
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

}
