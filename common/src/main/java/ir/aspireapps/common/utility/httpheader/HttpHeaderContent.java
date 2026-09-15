package ir.aspireapps.common.utility.httpheader;

import lombok.Builder;

@Builder
public record HttpHeaderContent(
        String userId,
        String username,
        String roles
) {
}
