package com.github.spikevlg.balanceofphone.persist;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import org.springframework.data.repository.Repository;

public interface PhoneUserRepository extends Repository<PhoneUser, Integer> {
    PhoneUser findById(Integer id);
    PhoneUser findByLogin(String login);
    PhoneUser save(PhoneUser user);
}
