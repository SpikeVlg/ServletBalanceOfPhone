package com.github.spikevlg.balanceofphone.model;


import javax.persistence.*;

/**
 * Class represents balance of phone user.
 */
@Entity
@Table(name = "phone_balance", uniqueConstraints = {  @UniqueConstraint(columnNames = "user_id") })
public class UserBalance {
    /**
     * Record ID in database.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Balance value for user.
     */
    @Column(name = "balance", nullable = false)
    private Double balance;

    /**
     * Phone user.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private PhoneUser user;

    public UserBalance(Integer id, PhoneUser user, Double balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    public UserBalance(PhoneUser user, Double balance) {
        this.user = user;
        this.balance = balance;
    }

    public UserBalance(){}

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public PhoneUser getUser() {
        return user;
    }

    public void setUser(PhoneUser user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
