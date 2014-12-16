package com.github.spikevlg.balanceofphone.model;


import javax.persistence.*;

@Entity
@Table(name = "phone_balance", uniqueConstraints = {  @UniqueConstraint(columnNames = "user_id") })
public class UserBalance {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private PhoneUser user;

    public UserBalance(Integer id, PhoneUser user, Double balance) {
        this.id = id;
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
}
