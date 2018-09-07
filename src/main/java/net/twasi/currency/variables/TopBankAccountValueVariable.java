package net.twasi.currency.variables;

import net.twasi.currency.database.CurrencyService;
import net.twasi.currency.database.bank.BankEntity;
import net.twasi.currency.database.bankaccount.BankAccountRepository;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

import java.util.Arrays;
import java.util.List;

public class TopBankAccountValueVariable extends TwasiVariable {
    private CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);
    private BankAccountRepository bankAccRepo = ServiceRegistry.get(DataService.class).get(BankAccountRepository.class);

    public TopBankAccountValueVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("toppointsvalue", "topcurrencyvalue");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        int pos;
        BankEntity bank = currencyService.getBank(inf.getStreamer().getUser());
        try {
            pos = Integer.parseInt(params[0]);
        } catch (Exception e) {
            pos = 0;
        }
        return String.valueOf(bankAccRepo.getBankAccountEntityByRanking(bank, pos).getAccountValue());
    }
}
