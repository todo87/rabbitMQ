package ro.org.m2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
@EqualsAndHashCode
@ToString(includeFieldNames=true, doNotUseGetters = true)
public abstract class GenericModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    protected Long id;

}
