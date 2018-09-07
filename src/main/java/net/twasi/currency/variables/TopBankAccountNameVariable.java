package net.twasi.currency.variables;

import com.google.gson.JsonParser;
import net.twasi.currency.Plugin;
import net.twasi.currency.database.CurrencyService;
import net.twasi.currency.database.bank.BankEntity;
import net.twasi.currency.database.bankaccount.BankAccountRepository;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.config.ConfigService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopBankAccountNameVariable extends TwasiVariable {
    private static String clientId = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientId;
    private CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);
    private BankAccountRepository bankAccRepo = ServiceRegistry.get(DataService.class).get(BankAccountRepository.class);


    public TopBankAccountNameVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("toppointsname", "topcurrencyname");
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
        String twitchId = bankAccRepo.getBankAccountEntityByRanking(bank, pos).getTwitchId();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/vnd.twitchtv.v5+json");
            String username = Plugin.getApiContent("https://api.twitch.tv/kraken/users/" + twitchId + "?client_id=" + clientId, headers);
            username = new JsonParser().parse(username).getAsJsonObject().get("display_name").getAsString();
            return username;
        } catch (Exception e) {
            return "QUERY_ERROR";
        }
    }
}
