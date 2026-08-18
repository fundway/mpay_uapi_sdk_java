package cards.mpay.uapi.api.args.card;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import cards.mpay.uapi.api.args.PaginationArgs;

@Getter
@SuperBuilder
public class GetCardTransactionsArgs extends PaginationArgs {
    private String cardId;
}