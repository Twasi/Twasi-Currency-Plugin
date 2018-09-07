package net.twasi.currency.database.bank;

import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "Twasi.Currency.Bank", noClassnameStored = true)
public class BankEntity extends BaseEntity {

    @Reference
    private User user;

    private String currencyName = "Punkte";
    private String currencyNameSingle = "Punkt";
    private String currencyCommand = "!points";
    private boolean allowPublicRequests = true;
    private boolean allowModOperations = false;

    public BankEntity(){}

    public BankEntity(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyNameSingle() {
        return currencyNameSingle;
    }

    public void setCurrencyNameSingle(String currencyNameSingle) {
        this.currencyNameSingle = currencyNameSingle;
    }

    public String getCurrencyCommand() {
        return currencyCommand;
    }

    public void setCurrencyCommand(String currencyCommand) {
        this.currencyCommand = currencyCommand;
    }

    public boolean allowPublicRequests() {
        return allowPublicRequests;
    }

    public void setAllowPublicRequests(boolean allowPublicRequests) {
        this.allowPublicRequests = allowPublicRequests;
    }

    public boolean allowModOperations() {
        return allowModOperations;
    }

    public void setAllowModOperations(boolean allowModOperations) {
        this.allowModOperations = allowModOperations;
    }
}
