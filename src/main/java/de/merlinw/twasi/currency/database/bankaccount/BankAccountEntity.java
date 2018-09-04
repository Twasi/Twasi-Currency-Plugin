package de.merlinw.twasi.currency.database.bankaccount;

import de.merlinw.twasi.currency.database.bank.BankEntity;
import net.twasi.core.database.models.BaseEntity;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "Twasi.Currency.Bankaccount", noClassnameStored = true)
public class BankAccountEntity extends BaseEntity {

    private String twitchId;
    private int accountValue = 0;

    @Reference
    private BankEntity bank;

    public BankAccountEntity() {
    }

    public BankAccountEntity(String twitchId, int accountValue, BankEntity bank) {
        this.twitchId = twitchId;
        this.accountValue = accountValue;
        this.bank = bank;
    }

    public BankAccountEntity(BankEntity bank, String twitchId){
        this.bank = bank;
        this.twitchId = twitchId;
    }

    public String getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }

    public int getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(int accountValue) {
        this.accountValue = accountValue;
    }

    public BankEntity getBank() {
        return bank;
    }

    public void setBank(BankEntity bank) {
        this.bank = bank;
    }
}
