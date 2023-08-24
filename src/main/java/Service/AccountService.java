package Service;
import Model.Account;

public interface AccountService {
    Account createAcc(Account potentialAcc);
    boolean verifiedAcc(Account potentialAcc);
    boolean existingAcc(String username);
}