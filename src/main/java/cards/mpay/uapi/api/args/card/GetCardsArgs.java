package cards.mpay.uapi.api.args.card;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCardsArgs {
    private String status;
}