package net.twasi.currency.database;

import net.twasi.currency.database.bank.BankEntity;
import net.twasi.currency.database.bank.BankRepository;
import net.twasi.currency.database.bankaccount.BankAccountEntity;
import net.twasi.currency.database.bankaccount.BankAccountRepository;
import net.twasi.currency.exceptions.NegativeBankAccountValueException;
import net.twasi.currency.exceptions.TransactionIsNotPositiveException;
import net.twasi.core.database.models.User;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

public class CurrencyService implements IService {
    private BankAccountRepository bankAccRepo = ServiceRegistry.get(DataService.class).get(BankAccountRepository.class);
    private BankRepository bankRepo = ServiceRegistry.get(DataService.class).get(BankRepository.class);

    public BankEntity getBank(User user) {
        BankEntity entity = bankRepo.getBankEntityByUser(user);
        if (entity == null) {
            entity = new BankEntity(user);
            bankRepo.add(entity);
            bankRepo.commitAll();
        }
        return entity;
    }

    public BankAccountEntity getBankAccount(BankEntity bank, String twitchId) {
        BankAccountEntity entity = bankAccRepo.getBankAccountByBankAndTwitchId(bank, twitchId);
        if (entity == null) {
            entity = new BankAccountEntity(bank, twitchId);
            bankAccRepo.add(entity);
            bankRepo.commitAll();
        }
        return entity;
    }

    public BankAccountEntity getBankAccount(User user, String twitchId) {
        return getBankAccount(getBank(user), twitchId);
    }

    public void addToBankAccount(BankAccountEntity bankAccount, int value) throws TransactionIsNotPositiveException {
        if (value < 1) throw new TransactionIsNotPositiveException();
        bankAccount.setAccountValue(bankAccount.getAccountValue() + value);
        if(!bankAccRepo.commit(bankAccount)) bankAccRepo.add(bankAccount);
        bankAccRepo.commitAll();
    }

    public void addToBankAccount(User user, String twitchId, int value) throws TransactionIsNotPositiveException {
        addToBankAccount(getBankAccount(user, twitchId), value);
    }

    public void removeFromBankAccount(BankAccountEntity bankAccount, int value) throws TransactionIsNotPositiveException, NegativeBankAccountValueException {
        if (value < 1) throw new TransactionIsNotPositiveException();
        if(bankAccount.getAccountValue() - value < 0) throw new NegativeBankAccountValueException();
        bankAccount.setAccountValue(bankAccount.getAccountValue() - value);
        if(!bankAccRepo.commit(bankAccount)) bankAccRepo.add(bankAccount);
        bankAccRepo.commitAll();
    }

    public void removeFromBankAccount(User user, String twitchId, int value) throws TransactionIsNotPositiveException, NegativeBankAccountValueException {
        removeFromBankAccount(getBankAccount(user, twitchId), value);
    }

    public void setBankAccountValue(BankAccountEntity bankAccount, int value) throws NegativeBankAccountValueException {
        if(value < 0) throw new NegativeBankAccountValueException();
        bankAccount.setAccountValue(value);
        if(!bankAccRepo.commit(bankAccount)) bankAccRepo.add(bankAccount);
        bankAccRepo.commitAll();
    }

    public void setBankAccountValue(User user, String twitchId, int value) throws NegativeBankAccountValueException {
        setBankAccountValue(getBankAccount(user, twitchId), value);
    }
}
