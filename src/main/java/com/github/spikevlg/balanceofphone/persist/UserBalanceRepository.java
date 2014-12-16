package com.github.spikevlg.balanceofphone.persist;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.model.UserBalance;
import org.springframework.data.repository.Repository;

public interface UserBalanceRepository extends Repository<UserBalance, Integer> {
    UserBalance findByUser(PhoneUser user);
    UserBalance save(UserBalance userBalance);
}
