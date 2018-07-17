package com.bpwizard.myresources.model;

import javax.persistence.*;

@Entity
@Table(name="usersc")
public class UserC {

    // PrimaryKey
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "usersc_generator", sequenceName = "usersc_sequence", allocationSize = 1)
	@GeneratedValue(generator = "usersc_generator")
    private long id;

    private String firstName;
    private String lastName;

    protected UserC() {}

    public UserC(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
