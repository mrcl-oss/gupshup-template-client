package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Response returned when a user is marked as opted-in. */
@Getter
@Setter
@ToString(callSuper = true)
public class OptInResponse extends BaseGupshupResponse {}
