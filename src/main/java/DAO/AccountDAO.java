package DAO;

import Model.Account;

public interface AccountDAO {
    
    Account createAcc(Account potentialAcc);
    
    boolean verifiedAcc (Account potentntialAcc);
    
    boolean existingAcc (String username);
}
