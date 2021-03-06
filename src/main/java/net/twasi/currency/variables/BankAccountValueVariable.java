package net.twasi.currency.variables;

import net.twasi.currency.database.CurrencyService;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.services.ServiceRegistry;

import java.util.Arrays;
import java.util.List;

public class BankAccountValueVariable extends TwasiVariable {
    private CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);

    public BankAccountValueVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("mypoints", "mpts");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        return String.valueOf(currencyService.getBankAccount(inf.getStreamer().getUser(), message.getSender().getTwitchId()).getAccountValue());
    }

}
