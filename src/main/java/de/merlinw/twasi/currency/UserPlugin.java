package de.merlinw.twasi.currency;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.merlinw.twasi.currency.database.CurrencyService;
import de.merlinw.twasi.currency.database.bank.BankEntity;
import de.merlinw.twasi.currency.database.bankaccount.BankAccountEntity;
import de.merlinw.twasi.currency.exceptions.NegativeBankAccountValueException;
import de.merlinw.twasi.currency.exceptions.TransactionIsNotPositiveException;
import net.twasi.core.database.models.User;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

public class UserPlugin extends TwasiUserPlugin {
    private CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);
    private static String clientId = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientId;

    @Override
    public void onInstall(TwasiInstallEvent e) {
        e.getModeratorsGroup().addKey("twasi.currency.mod.*");
        e.getAdminGroup().addKey("twasi.currency.streamer.*");
        e.getDefaultGroup().addKey("twasi.currency.default.*");
    }

    @Override
    public void onUninstall(TwasiInstallEvent e) {
        e.getModeratorsGroup().removeKey("twasi.currency.mod.*");
        e.getAdminGroup().removeKey("twasi.currency.streamer.*");
        e.getDefaultGroup().removeKey("twasi.currency.default.*");
    }

    @Override
    public void onMessage(TwasiMessageEvent e) {
        User streamer = e.getTwasiInterface().getTwasiInterface().getStreamer().getUser();
        BankEntity streamerBank = currencyService.getBank(streamer);
        TwasiMessage msg = e.getMessage();
        BankAccountEntity userBankAccount;
        String[] cmd = msg.getMessage().toLowerCase().split(" ");
        if (!cmd[0].equals(streamerBank.getCurrencyCommand()) && !cmd[0].equals("!currency")) return;

        try {
            switch (cmd[1]) {
                case "add":
                    if(cmd.length < 4) throw new ArrayIndexOutOfBoundsException();
                    if (!streamer.hasPermission(msg.getSender(), "twasi.currency.streamer.operate")) return;
                    try {
                        String api = Plugin.getApiContent("https://api.twitch.tv/kraken/users/" + cmd[2] + "?client_id=" + clientId);
                        JsonObject object = new JsonParser().parse(api).getAsJsonObject();
                        String id = object.get("_id").getAsString();
                        BankAccountEntity entity = currencyService.getBankAccount(streamer, id);
                        currencyService.addToBankAccount(entity, Integer.parseInt(cmd[3]));
                        msg.reply(getTranslation(
                                "twasi.currency.add.success",
                                object.get("display_name").getAsString(),
                                entity.getAccountValue(),
                                (entity.getAccountValue() == 1 ? streamerBank.getCurrencyNameSingle() : streamerBank.getCurrencyName()),
                                Integer.parseInt(cmd[3])
                        ));
                        return;
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException | TransactionIsNotPositiveException e1) {
                        msg.reply(getTranslation("twasi.currency.add.helptext", streamerBank.getCurrencyCommand()));
                        return;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        msg.reply(getTranslation("twasi.currency.requestfailed"));
                        return;
                    }
                case "remove":
                    if(cmd.length < 4) throw new ArrayIndexOutOfBoundsException();
                    if (!streamer.hasPermission(msg.getSender(), "twasi.currency.streamer.operate")) return;
                    BankAccountEntity entity = null;
                    String target = null;
                    try {
                        String api = Plugin.getApiContent("https://api.twitch.tv/kraken/users/" + cmd[2] + "?client_id=" + clientId);
                        JsonObject object = new JsonParser().parse(api).getAsJsonObject();
                        String id = object.get("_id").getAsString();
                        entity = currencyService.getBankAccount(streamer, id);
                        target = object.get("display_name").getAsString();
                        currencyService.removeFromBankAccount(entity, Integer.parseInt(cmd[3]));
                        msg.reply(getTranslation(
                                "twasi.currency.remove.success",
                                target,
                                entity.getAccountValue(),
                                (entity.getAccountValue() == 1 ? streamerBank.getCurrencyNameSingle() : streamerBank.getCurrencyName()),
                                Integer.parseInt(cmd[3])
                        ));
                        return;
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException | TransactionIsNotPositiveException e1) {
                        msg.reply(getTranslation("twasi.currency.remove.helptext", streamerBank.getCurrencyCommand()));
                        return;
                    } catch (NegativeBankAccountValueException e1){
                        String user = msg.getSender().getDisplayName();
                        int val = entity.getAccountValue();
                        msg.reply(getTranslation(
                                "twasi.currency.remove.belowzero",
                                user,
                                val,
                                val == 1 ? streamerBank.getCurrencyNameSingle() : streamerBank.getCurrencyName(),
                                target,
                                streamerBank.getCurrencyName()
                        ));
                        return;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        msg.reply(getTranslation("twasi.currency.requestfailed"));
                        return;
                    }
                default:
                    if (!streamerBank.allowPublicRequests()) return;
                    try {
                        String api = Plugin.getApiContent("https://api.twitch.tv/kraken/users/" + cmd[1] + "?client_id=" + clientId);
                        JsonObject object = new JsonParser().parse(api).getAsJsonObject();
                        String id = object.get("_id").getAsString();
                        entity = currencyService.getBankAccount(streamer, id);
                        msg.reply(getTranslation(
                                "twasi.currency.accountquery.public",
                                object.get("display_name").getAsString(),
                                entity.getAccountValue(),
                                (entity.getAccountValue() == 1 ? streamerBank.getCurrencyNameSingle() : streamerBank.getCurrencyName())
                        ));
                        return;
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e1) {
                        msg.reply(getTranslation("twasi.currency.remove.helptext"));
                        return;
                    } catch (Exception e1) {
                        msg.reply(getTranslation("twasi.currency.requestfailed"));
                        return;
                    }
            }
        } catch (ArrayIndexOutOfBoundsException e1) {
            userBankAccount = currencyService.getBankAccount(streamerBank, msg.getSender().getTwitchId());
            msg.reply(getQueryText(msg.getSender().getDisplayName(), userBankAccount.getAccountValue(), streamerBank));
            return;
        }
    }

    private String getQueryText(String name, int value, BankEntity entity) {
        return getTranslation("twasi.currency.accountquery.self", name, value, (value == 1 ? entity.getCurrencyNameSingle() : entity.getCurrencyName()));
    }

}
