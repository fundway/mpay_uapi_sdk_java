package cards.mpay.uapi.api.args;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PaginationArgs {
    private Integer page;
    private Integer limit;
}