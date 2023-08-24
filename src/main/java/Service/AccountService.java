package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountDAO(AccountDAO accountDAO){


        this.accountDAO = accountDAO;

    }
    
    public Account createAcc(String username, String password){

    }

    public boolean verifiedAcc(String username, String password){

    }

    public boolean existingAcc(String username){

    }
    
}
