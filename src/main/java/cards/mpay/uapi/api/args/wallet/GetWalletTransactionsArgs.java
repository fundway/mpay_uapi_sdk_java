package cards.mpay.uapi.api.args.wallet;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import cards.mpay.uapi.api.args.PaginationArgs;

@Getter
@SuperBuilder
public class GetWalletTransactionsArgs extends PaginationArgs {
    private String direction;
}