package de.merlinw.twasi.currency.database.bankaccount;

import de.merlinw.twasi.currency.database.bank.BankEntity;
import net.twasi.core.database.lib.Repository;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class BankAccountRepository extends Repository<BankAccountEntity> {

    public BankAccountEntity getBankAccountByBankAndTwitchId(BankEntity bank, String twitchId){
        Query<BankAccountEntity> bankAccountEntitieQuery = store.createQuery(BankAccountEntity.class);
        List<BankAccountEntity> bankAccountEntities = bankAccountEntitieQuery.field("bank").equal(bank).field("twitchId").equal(twitchId).asList();
        if (bankAccountEntities.size() > 0) return bankAccountEntities.get(0);
        else return null;
    }

}
