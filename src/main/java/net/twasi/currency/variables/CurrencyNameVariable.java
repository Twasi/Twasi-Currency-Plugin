package net.twasi.currency.variables;

import net.twasi.currency.database.CurrencyService;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.services.ServiceRegistry;

import java.util.Arrays;
import java.util.List;

public class CurrencyNameVariable extends TwasiVariable {
    private CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);

    public CurrencyNameVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("currencyname", "cname");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        return currencyService.getBank(inf.getStreamer().getUser()).getCurrencyName();
    }

}