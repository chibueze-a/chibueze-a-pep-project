package DAO;

import Model.Account;

public interface AccountDAO {
    
    boolean createAcc(Account potentialAcc);
    
    boolean verifiedAcc (Account potentntialAcc);
    
    boolean existingAcc (String username);
}
