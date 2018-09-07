package net.twasi.currency.database.bank;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class BankRepository extends Repository<BankEntity> {

    public BankEntity getBankEntityByUser(User user){
        Query<BankEntity> bankEntitieQuery = store.createQuery(BankEntity.class);
        List<BankEntity> bankEntities = bankEntitieQuery.field("user").equal(user).asList();
        if (bankEntities.size() > 0) return bankEntities.get(0);
        else return null;
    }

}
