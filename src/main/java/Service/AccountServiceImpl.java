package Service;

import DAO.AccountDAO;
import Service.AccountService;
import Model.Account;

public class AccountServiceImpl implements AccountService{

    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO){

        this.accountDAO = accountDAO;

    }

@Override
public Account createAcc(Account potentialAcc){

    return accountDAO.createAcc(potentialAcc);
}

@Override
public boolean verifiedAcc(Account potentialAcc){

    return accountDAO.verifiedAcc(potentialAcc);
}

@Override
public boolean existingAcc(String username){

    return accountDAO.existingAcc(username);
}

}