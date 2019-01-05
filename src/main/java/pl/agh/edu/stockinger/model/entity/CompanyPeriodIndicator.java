package pl.agh.edu.stockinger.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CompanyPeriodIndicator {

    @Id
    @Column(name = "INDICATOR_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;



}
