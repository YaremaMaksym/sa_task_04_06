package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Company entity class
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Entity
@Table(name = "companies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    /**
     * Company id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Company name
     */
    private String name;

    /**
     * Company registration number
     */
    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    /**
     * Company address
     */
    private String address;

    /**
     * Company creation date
     */
    private LocalDate created_at;

    /**
     * Company reports
     */
    @OneToMany(mappedBy = "company")
    private Set<Report> reports = new LinkedHashSet<>();

    /**
     * Checks if the company is equal to another
     *
     * @param o the object to check equality with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(registrationNumber, company.registrationNumber) && Objects.equals(address, company.address) && Objects.equals(created_at, company.created_at);
    }

    /**
     * Calculates hash code for the company
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, registrationNumber, address, created_at);
    }
}
